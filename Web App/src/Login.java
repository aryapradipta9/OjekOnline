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
import org.json.JSONObject;
import util.Env;
import util.HttpRequest;

@WebServlet(name = "Login")
public class Login extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      Env env = new Env();
      // Panggil REST Login
      HttpRequest login = new HttpRequest("http://localhost:"+env.getIdentityPort()+"/login");
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
      String res = login.postRequest("username="+request.getParameter("username")+"&password="+request.getParameter("password")+"&ua="+browser);
      JSONObject json_result = new JSONObject(res);

      // Hasil JSON dicek
      if(json_result.getBoolean("status")){
        Encoder tokenEncoder = Base64.getEncoder();
        String token = new String(tokenEncoder.encode(json_result.getJSONObject("login_token").toString().getBytes()));
        Cookie login_token = new Cookie("login_token", token);
        response.addCookie(login_token);
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
