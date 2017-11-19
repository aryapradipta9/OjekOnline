import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import user.Profile;
import util.Env;

@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
    * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "UpdateProfile")
public class UpdateProfile extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
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

      Env env = new Env();
      URL url = new URL("http://localhost:" + env.getOjekPort() + "/services/user/profile?wsdl");

      QName qname = new QName("http://user/", "ProfileImplService");
      QName qname_port = new QName("http://user/", "ProfileImplPort");
      Service service = Service.create(url, qname);
      Profile profile = service.getPort(qname_port, Profile.class);

      int id = Integer.parseInt(request.getParameter("id"));
      String name = request.getParameter("name");
      String phone = request.getParameter("phone");
      String status = request.getParameter("status");

      Part filePart = request.getPart("profpic");
      String raw_image = "";

      if (filePart.getSize() != 0) {
        byte[] bytes = new byte[(int) filePart.getSize()];
        filePart.getInputStream().read(bytes);

        raw_image = Base64.getEncoder().encodeToString(bytes);
      }

      profile.updateProfile(login_cookie.getValue(), id, name, phone, raw_image, status);
      response.sendRedirect("profile.jsp?id=" + id);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}