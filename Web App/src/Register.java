import java.util.Base64;
import java.util.Base64.Encoder;
import org.json.JSONObject;
import user.Profile;
import util.Env;
import util.HttpRequest;
import util.TokenChecker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;

@WebServlet(name = "Register")
public class Register extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        boolean isDriver = request.getParameter("isDriver") != null;

        try {
            Env env = new Env();

            URL url = new URL("http://localhost:" + env.getOjekPort() + "/services/user/profile?wsdl");
            QName qname = new QName("http://user/", "ProfileImplService");
            QName qname_port = new QName("http://user/", "ProfileImplPort");
            Service service = Service.create(url, qname);
            Profile profile = service.getPort(qname_port, Profile.class);

            HttpRequest registerRequest = new HttpRequest(
                    "http://localhost:" + env.getIdentityPort() + "/register");
            String userAgent = request.getHeader("User-Agent");
            String browser = TokenChecker.parseUA(userAgent);
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if(ipAddress == null){
                ipAddress = request.getRemoteAddr();
            }
            String string_token = registerRequest.postRequest("username=" + username + "&password=" + password + "&email=" + email + "&ua=" + browser + "&ip=" + ipAddress);
            JSONObject json_token = new JSONObject(string_token);

            if (json_token.getBoolean("status")) {
                int id = json_token.getJSONObject("login_token").getInt("id");
                Encoder tokenEncoder = Base64.getEncoder();
                String token = new String(tokenEncoder.encode(json_token.getJSONObject("login_token").toString().getBytes()));
                Cookie login_token = new Cookie("login_token", token);
                response.addCookie(login_token);
                response.sendRedirect("profile.jsp?id=" + id);

                profile.addProfile(id , name, email, phone, username, password, isDriver);
            } else {

                response.getWriter().print("<script>alert('Registrasi Gagal, Username/Email sudah terdaftar');");
                response.getWriter().print("document.location = 'signup.jsp'</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}