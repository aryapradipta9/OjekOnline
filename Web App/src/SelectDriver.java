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

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
      Env env = new Env();
      URL order_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/preferredlocation?wsdl");
      QName pref_loc_qname = new QName("http://user/", "PrefLocImplService");
      QName pref_loc_qname_port = new QName("http://user/", "PrefLocImplPort");
      Service pref_loc_service = Service.create(order_url, pref_loc_qname);
      PrefLoc pref_loc = pref_loc_service.getPort(pref_loc_qname_port, PrefLoc.class);

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
      if ((login_cookie != null) && (TokenChecker.checkToken(ua,login_cookie.getValue()))) {
        int id = Integer.parseInt(request.getParameter("id"));
        String pref_loc_string = pref_loc.getPrefLoc(login_cookie.getValue(), id);
        Cookie cook = new Cookie("location", pref_loc_string);
        response.addCookie(cook);
        RequestDispatcher rd = request.getRequestDispatcher("order-driverview.jsp");
        rd.forward(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}