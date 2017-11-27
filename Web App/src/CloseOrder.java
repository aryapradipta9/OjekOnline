import util.HttpRequest;
import util.TokenChecker;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 13515017 / Putu Arya Pradipta.
 * Tanggal 11/27/2017.
 * FileName : CloseOrder.java.
 */
public class CloseOrder extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      Cookie[] cookies = request.getCookies();
      Cookie login_cookie = null;
      Cookie username = null;
      Cookie usrnmdrv = null;

      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("login_token")) {
            login_cookie = cookie;
          } else if (cookie.getName().equals("username")) {
            username = cookie;
          } else if (cookie.getName().equals("usrnmdrv")) {
            usrnmdrv = cookie;
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
        String target = "http://localhost:3000/chat/c/" + username.getValue() + '/' + usrnmdrv.getValue();
        HttpRequest rv = new HttpRequest(target);
        rv.getRequest("");
        RequestDispatcher rd = request.getRequestDispatcher("order-complete.jsp?id=" + id);
        rd.forward(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
