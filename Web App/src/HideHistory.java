import user.History;
import util.Env;

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

@WebServlet(name = "HideHistory")
public class HideHistory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            URL url = new URL("http://localhost:" + env.getOjekPort() + "/services/user/history?wsdl");

            QName qname = new QName("http://user/", "HistoryImplService");
            QName qname_port = new QName("http://user/", "HistoryImplPort");
            Service service = Service.create(url, qname);
            History history = service.getPort(qname_port, History.class);

            int order_id = Integer.parseInt(request.getParameter("order_id"));
            String type = request.getParameter("type");
            int id = Integer.parseInt(request.getParameter("id"));
            history.hideHistory(login_cookie.getValue(), order_id, type);

            if(type.equals("driver")) response.sendRedirect("history-driverhistory.jsp?id=" + id);
            else response.sendRedirect("history-orderhistory.jsp?id=" + id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}