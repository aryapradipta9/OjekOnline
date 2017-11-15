import order.Order;
import org.json.JSONObject;
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

@WebServlet(name = "SaveOrder")
public class SaveOrder extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Env env = null;
        try {
            env = new Env();
        } catch (Exception e) {
            e.printStackTrace();
        }
        URL order_url = new URL("http://localhost:"+env.getOjekPort()+"/services/order?wsdl");

        // Create PrefLoc Service Endpoint
        QName order_qname = new QName("http://order/", "OrderImplService");
        QName order_qname_port = new QName("http://order/", "OrderImplPort");
        Service order_service = Service.create(order_url, order_qname);
        Order order = order_service.getPort(order_qname_port, Order.class);

        Cookie[] cookies = request.getCookies();
        Cookie login_cookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login_token")) {
                    login_cookie = cookie;
                }
            }
        }

        JSONObject order_json = null;

        if (login_cookie == null) {
            response.sendRedirect("login.jsp");
        } else {
            order.getDriver(login_cookie.getValue(), request.getParameter("origin"),
                    request.getParameter("destination"),
                    request.getParameter("preferreddriver"),
                    Integer.parseInt(request.getParameter("userId")));
            JSONObject json_parameter = new JSONObject();
            json_parameter.put("origin", request.getParameter("origin"));
            json_parameter.put("destination", request.getParameter("destination"));
            json_parameter.put("rating", Float.parseFloat(request.getParameter("rating")));
            json_parameter.put("comment", request.getParameter("comment"));
            json_parameter.put("driverId", Integer.parseInt(request.getParameter("driverId")));
            json_parameter.put("userId", Integer.parseInt(request.getParameter("userId")));
            order.completeOrder(login_cookie.getValue(), json_parameter.toString());

            response.sendRedirect("profile.jsp?id=" + Integer.parseInt(request.getParameter("userId")));
        }
    }
}
