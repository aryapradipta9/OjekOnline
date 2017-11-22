import java.util.Base64;
import java.util.Base64.Encoder;
import org.json.JSONObject;
import user.Profile;
import util.Env;
import util.HttpRequest;

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
            String user = userAgent.toLowerCase();
            String browser = null;
            if (user.contains("msie"))
            {
                String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
                browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
            } else if (user.contains("safari") && user.contains("version"))
            {
                browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if ( user.contains("opr") || user.contains("opera"))
            {
                if(user.contains("opera"))
                    browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
                else if(user.contains("opr"))
                    browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            } else if (user.contains("chrome"))
            {
                browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  || (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) || (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) )
            {
                //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";

            } else if (user.contains("firefox"))
            {
                browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if(user.contains("rv"))
            {
                browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
            } else
            {
                browser = "UnKnown, More-Info: "+userAgent;
            }
            String string_token = registerRequest.postRequest("username=" + username + "&password=" + password + "&email=" + email +"&ua=" +browser);
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
                response.getWriter().print("<script>alert('Registrasi Gagal, Username/Email sudah terdaftar')");
                response.getWriter().print("document.location = 'register.jsp'</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
