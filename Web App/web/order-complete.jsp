<%@ page import="java.net.URL" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="order.Order" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="user.Profile" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>
<%@ page import="user.Profile" %>
<%
    Env env = new Env();
    URL order_url = new URL("http://localhost:"+env.getOjekPort()+"/services/order?wsdl");
    URL user_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/profile?wsdl");

    URL profile_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/profile?wsdl");

    // Create Profile Service Endpoint
    QName profile_qname = new QName("http://user/", "ProfileImplService");
    QName profile_qname_port = new QName("http://user/", "ProfileImplPort");
    Service profile_service = Service.create(profile_url, profile_qname);
    Profile profile = profile_service.getPort(profile_qname_port, Profile.class);

    // Create PrefLoc Service Endpoint
    QName order_qname = new QName("http://order/", "OrderImplService");
    QName order_qname_port = new QName("http://order/", "OrderImplPort");
    Service order_service = Service.create(order_url, order_qname);
    Order order = order_service.getPort(order_qname_port, Order.class);

    QName user_qname = new QName("http://user/", "ProfileImplService");
    QName user_qname_port = new QName("http://user/", "ProfileImplPort");
    Service user_service = Service.create(user_url, user_qname);
    Profile user = user_service.getPort(user_qname_port, Profile.class);

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
    JSONObject profile_json = null;

    if (login_cookie == null) {
        response.sendRedirect("login.jsp");
        return;
    } else {
        String profile_string = user
                .getProfile(login_cookie.getValue(), Integer.parseInt(request.getParameter("id")));

        profile_json = new JSONObject(profile_string);
        String order_string = order.getDriverById(login_cookie.getValue(),
                Integer.parseInt(request.getParameter("driverId")));
        order_json = new JSONObject(order_string);
    }
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>VROOOM! - A Solution for Your Transportation</title>
    <link rel="stylesheet" href="css/styles.css">
    <meta charset="UTF-8">
</head>
<body>
<div class="container">
    <% out.print(Header.printHeader(Integer.parseInt(request.getParameter("id")),profile_json.getString("username"),response.getWriter()));%>
    <% out.print(Header.printNavbar(Integer.parseInt(request.getParameter("id")),0,response.getWriter()));%>

    <div class="section">
        <div class="section-header">
            <div class="section-title">
                MAKE AN ORDER
            </div>
        </div>
        <div class="section-step row">
            <div class="step">
                <div class="step-no">1</div>
                <div class="step-guide">Select Destination</div>
            </div>
            <div class="step">
                <div class="step-no">2</div>
                <div class="step-guide">Select A Driver</div>
            </div>
            <div class="step">
                <div class="step-no">3</div>
                <div class="step-guide active">Chat Driver</div>
            </div>
            <div class="step active">
                <div class="step-no">4</div>
                <div class="step-guide active">Complete your order</div>
            </div>
        </div>
        <div class="section-header">
            <div class="section-title">
                HOW WAS IT?
            </div>
        </div>
        <!-- form begins here -->
        <form action="/saveorder" method="post">
            <!--filled variable-->
            <input type="hidden" name="origin" value="<%= request.getParameter("origin")%>">
            <input type="hidden" name="destination"
                   value="<%= request.getParameter("destination")%>">
            <input type="hidden" name="driverId" value="<%= request.getParameter("driverId")%>">
            <input type="hidden" name="userId" value="<%= request.getParameter("userId")%>">

            <div class="driver-review">
                <div class="driver-profile">
                    <div class="section-profilepic">
                        <%
                        //String profile_string = profile.getProfile(login_cookie.getValue(), Integer.parseInt(request.getParameter("driverId")));

                        //JSONObject profile_json = new JSONObject(profile_string);
                        out.print(Header.printProfile(profile_json.getBoolean("isDriver"),
                        profile_json.getString("profilePic"), "profilepic-big-round"));
                        %>
                    </div>
                    <div class="driver-label">
                        <div class="driver-label-id">
                            <div class='driver-eval-username'>
                                <%= order_json.getString("username")%>
                            </div>
                            <div class="driver-eval-name">
                                <%= order_json.getString("name")%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="input-grading">
                    <div class="driver-star-rating">
                        <div class="driver-rate">
                            <input type="radio" id="rating1" name="rating" value="1">
                            <label for="rating1"> ★ </label>
                            <input type="radio" id="rating2" name="rating" value="2">
                            <label for="rating2"> ★ </label>
                            <input type="radio" id="rating3" name="rating" value="3" checked>
                            <label for="rating3"> ★ </label>
                            <input type="radio" id="rating4" name="rating" value="4">
                            <label for="rating4"> ★ </label>
                            <input type="radio" id="rating5" name="rating" value="5">
                            <label for="rating5"> ★ </label>
                        </div>
                    </div>
                    <div class="driver-eval-comment">
                        <div class="field  wide">
                            <input type="text" name="comment"
                                   action="order-complete.jsp?id=<%= request.getParameter("username")%>"
                                   placeholder="Your comment...">
                        </div>
                        <div class="row align-right"><input type="submit" class="button-green"
                                                            value="Complete Order">
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>