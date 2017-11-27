import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Encoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import jdk.nashorn.internal.parser.Token;
import org.json.JSONObject;
import user.PrefLoc;
import util.Env;
import util.HttpRequest;
import util.TokenChecker;

@WebServlet(name = "SelectDriver")
public class SelectDriver extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /*try {
      // ambil nama driver yg diselect
      Env env = new Env();
      // Panggil REST Login
      HttpRequest login = new HttpRequest("http://localhost:"+env.getIdentityPort()+"/login");
      String userAgent = request.getHeader("User-Agent");
      String browser = TokenChecker.parseUA(userAgent);
      String res = login.postRequest("username="+request.getParameter("username")+"&password="+request.getParameter("password")+"&ua="+browser);
      JSONObject json_result = new JSONObject(res);

      // Hasil JSON dicek
      Encoder tokenEncoder = Base64.getEncoder();
      String token = new String(tokenEncoder.encode(json_result.getJSONObject("login_token").toString().getBytes()));
      Cookie login_token = new Cookie("login_token", token);
      Cookie cook = new Cookie("username", request.getParameter("username"));
      response.addCookie(login_token);
      response.addCookie(cook);
    } catch (Exception e) {
      e.printStackTrace();
    }
  */
    try {
      Cookie[] cookies = request.getCookies();
      Cookie login_cookie = null;

      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("login_token")) {
            login_cookie = cookie;
          }
        }
      }

      String ua = request.getHeader("User-Agent");
      String ipAddress = request.getHeader("X-FORWARDED-FOR");
      if(ipAddress == null){
        ipAddress = request.getRemoteAddr();
      }
      if ((login_cookie != null) && (TokenChecker.checkToken(ua,login_cookie.getValue(),ipAddress))) {
        int id = Integer.parseInt(request.getParameter("id"));
        Cookie cook = new Cookie("usrnmdrv", request.getParameter("driverUsername"));
        response.addCookie(cook);
        RequestDispatcher rd = request.getRequestDispatcher("order-chatuser.jsp?id=" + id);
        rd.forward(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}