package user;

import db.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Base64.Decoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "user.Logout")
public class Logout extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();
    Cookie login_cookie = null;
    JSONObject cookie_json = null;
    JSONObject return_json = new JSONObject();

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("login_token")) {
          login_cookie = cookie;
        }
      }
      Decoder decoder = Base64.getDecoder();
      String json_token = new String(decoder.decode(login_cookie.getValue()));
      cookie_json = new JSONObject(json_token);

      try {
        Database db = new Database();
        db.openConnection();

        boolean is_exist = db
            .update("UPDATE user SET token=NULL WHERE id=" + cookie_json.getInt("id"));
        return_json.put("logout", is_exist);
        db.closeConnection();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    // Write response
    PrintWriter out = response.getWriter();
    out.println(return_json.toString());
    response.sendRedirect("http://localhost:8100/");
  }
}