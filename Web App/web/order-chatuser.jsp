<%@ page import="java.net.URL" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="order.Order" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="user.Profile" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>
<%@ page import="user.Profile" %>
<%
    Env env = new Env();

    URL order_url = new URL("http://localhost:"+env.getOjekPort()+"/services/order?wsdl");
    URL user_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/profile?wsdl");

    QName order_qname = new QName("http://order/", "OrderImplService");
    QName order_qname_port = new QName("http://order/", "OrderImplPort");
    Service order_service = Service.create(order_url, order_qname);
    Order order = order_service.getPort(order_qname_port, Order.class);

    QName user_qname = new QName("http://user/", "ProfileImplService");
    QName user_qname_port = new QName("http://user/", "ProfileImplPort");
    Service user_service = Service.create(user_url, user_qname);
    Profile profile = user_service.getPort(user_qname_port, Profile.class);

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
    }else{
        String profile_string = profile
                .getProfile(login_cookie.getValue(), Integer.parseInt(request.getParameter("id")));

        profile_json = new JSONObject(profile_string);

    }
%>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>VROOOM! - A Solution for Your Transportation</title>
    <link rel="stylesheet" href="css/styles.css">
    <script type="text/javascript">
        function close() {
            alert("HAHA");
        }

    </script>
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
            <div class="step active">
                <div class="step-no">3</div>
                <div class="step-guide active">Chat Driver</div>
            </div>
            <div class="step">
                <div class="step-no">4</div>
                <div class="step-guide">Complete your order</div>
            </div>
        </div>

        <iframe id="chat-frame" src="http://localhost:8080/chat-user" style="width:100%; height:300px; border:0px;"></iframe>
        <form id="form-chat" action="closeorder?id=<%=request.getParameter("userId")%>" method="post">
            <input type="hidden" name="origin" value="<%= request.getParameter("origin")%>">
            <input type="hidden" name="destination"
                   value="<%= request.getParameter("destination")%>">
            <input type="hidden" name="driverId" value="<%= request.getParameter("driverId")%>">
            <input type="hidden" name="userId" value="<%= request.getParameter("userId")%>">
            <input type="submit" value="Close">
        </form>
    </div>
</div>

</body>
</html>