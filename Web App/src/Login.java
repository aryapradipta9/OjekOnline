import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Base64.Encoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdk.nashorn.internal.parser.Token;
import org.json.JSONObject;
import util.Env;
import util.HttpRequest;
import util.TokenChecker;

@WebServlet(name = "Login")
public class Login extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      Env env = new Env();
      // Panggil REST Login
      HttpRequest login = new HttpRequest("http://localhost:"+env.getIdentityPort()+"/login");
      String userAgent = request.getHeader("User-Agent");
      String browser = TokenChecker.parseUA(userAgent);
      String ipAddress = request.getHeader("X-FORWARDED-FOR");
      if(ipAddress == null){
        ipAddress = request.getRemoteAddr();
      }
      String res = login.postRequest("username="+request.getParameter("username")+"&password="+request.getParameter("password")+"&ua="+browser+"&ip="+ipAddress);
      JSONObject json_result = new JSONObject(res);

      // Hasil JSON dicek
      if(json_result.getBoolean("status")){
        Encoder tokenEncoder = Base64.getEncoder();
        String token = new String(tokenEncoder.encode(json_result.getJSONObject("login_token").toString().getBytes()));
        Cookie login_token = new Cookie("login_token", token);
        Cookie cook = new Cookie("username", request.getParameter("username"));
        response.addCookie(login_token);
        response.addCookie(cook);
        response.sendRedirect("profile.jsp?id="+json_result.getJSONObject("login_token").getInt("id"));
      }
      else{
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('Username atau Password Salah')");
        out.println("window.location='login.jsp'");
        out.println("</script>");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
