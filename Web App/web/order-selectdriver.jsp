<%@ page import="java.net.URL" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="order.Order" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="user.Profile" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>
<%@ page import="util.HttpRequest" %>
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
    <title>VROOOM! - A Solution for Your Transportation</title>
    <link rel="stylesheet" href="css/styles.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script>
        $.ajax({
            url:"http://localhost:8080/online.html",
            type:"GET",

            crossDomain:true,
            success:function(response) {
                console.log(response);
            }
        });
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
            <div class="step active">
                <div class="step-no">2</div>
                <div class="step-guide">Select A Driver</div>
            </div>
            <div class="step">
                <div class="step-no">3</div>
                <div class="step-guide active">Chat Driver</div>
            </div>
            <div class="step">
                <div class="step-no">4</div>
                <div class="step-guide">Complete your order</div>
            </div>
        </div>
        <div class="section-select-driver">
            &ensp;PREFERRED DRIVERS :
            <%
                if ((request.getParameter("preferreddriver") != null)) {
                    String order_string = order
                            .getDriver(login_cookie.getValue(), request.getParameter("origin"),
                                    request.getParameter("destination"),
                                    request.getParameter("preferreddriver"),
                                    Integer.parseInt(request.getParameter("id")));
                    order_json = new JSONObject(order_string);
                    // bersih bersih json

                    HttpRequest xhr = new HttpRequest("http://localhost:3000/addonline");

                    JSONObject onlineDriver = new JSONObject(xhr.getRequest(""));
                    JSONArray online = onlineDriver.getJSONArray("online");

                    JSONObject preferred = order_json.getJSONObject("preferred");

                    if (!preferred.toString().equals("{}")) {
                        boolean found = false;
                        for (int i = 0; i < online.length(); i++) {
                            System.out.println(online.getString(i));
                            if (online.getString(i).equals(order_json.getJSONObject("preferred").getString("username")))
                                found = true;
                        }
                        if (found){
//                        Cookie cook = new Cookie("usrnmdrv", order_json.getJSONObject("preferred").getString("username"));
//                        response.addCookie(cook);
            %>
            <form action="selectdriver?id=<%=request.getParameter("userId")%>" method="post">
                <div class="section-content">
                    <input type="hidden" name="origin" value="<%=request.getParameter("origin")%>">
                    <input type="hidden" name="destination"
                           value="<%=request.getParameter("destination")%>">
                    <input type="hidden" name="userId" value="<%=request.getParameter("userId")%>">
                    <input type="hidden" name="driverId"
                           value="<%=order_json.getJSONObject("preferred").get("id")%>">
                    <input type="hidden" name="driverUsername" value="<%=order_json.getJSONObject("preferred").get("username")%>">

                    <div class="section-profilepic">
                        <%
                            // Get Profile
                            //String profile_string = profile.getProfile(login_cookie.getValue(), order_json.getJSONObject("preferred").getInt("id"));

                            //JSONObject profile_json = new JSONObject(profile_string);
                            out.print(Header.printProfile(profile_json.getBoolean("isDriver"),
                                    profile_json.getString("profilePic"), "profilepic-big")); %>
                        <div class="section-stacked">
                            <div class="history-label align-left">
                                <div class="driver-name-label">
                                    <%=order_json.getJSONObject("preferred").getString("name")%>
                                </div>
                                <div class="profile-rating"> &#9734
                                    <%=order_json.getJSONObject("preferred").getInt("rating")%>
                                    <span class="profile-votes">(<%=order_json
                                            .getJSONObject("preferred").getInt("votes")%>)</span>
                                </div>
                            </div>
                            <div class="button-section align-right">
                                <input type="submit" class="button-green" value="I choose you">
                            </div>
                        </div>
                    </div>
            </form>
            <%
                        } else {
                            out.println(
                                    "<div class=\"section-no-results\"><br>No Results Found :( <br><br></div>");
                        }
                    } else {
                        out.println(
                                "<div class=\"section-no-results\"><br>No Results Found :( <br><br></div>");
                    }
                } else {
                    out.println(
                            "<div class=\"section-no-results\"><br>No Results Found :( <br><br></div>");
                }
            %>
        </div>
        <br>
        <div class="section-select-driver">
            OTHER DRIVERS :
            <%
                JSONArray jsonArray = order_json.getJSONArray("other");
                if (jsonArray.length() > 0) {
                    HttpRequest xhr = new HttpRequest("http://localhost:3000/addonline");

                    JSONObject onlineDriver = new JSONObject(xhr.getRequest(""));
                    JSONArray online = onlineDriver.getJSONArray("online");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        boolean found = false;
                        for (int j = 0; j < online.length(); j++) {
                            System.out.println(online.getString(j));
                            if (online.getString(j).equals(jsonArray.getJSONObject(i).getString("username")))
                                found = true;
                        }
                        if (found){
//                        Cookie cook = new Cookie("usrnmdrv", jsonArray.getJSONObject(i).getString("username"));
//                        response.addCookie(cook);
            %>

            <%--<form action="order-chatuser.jsp?id=<%=request.getParameter("userId")%>" method="post">--%>
            <form action="selectdriver?id=<%=request.getParameter("userId")%>" method="post">
                <!-- NAMPILIN USER YANG BISA*/ -->
                <div class="section-content">
                    <input type="hidden" name="origin" value="<%=request.getParameter("origin")%>">
                        <input type="hidden" name="destination"
                               value="<%=request.getParameter("destination")%>">
                        <input type="hidden" name="userId" value="<%=request.getParameter("userId")%>">
                        <input type="hidden" name="driverId"
                               value="<%=jsonArray.getJSONObject(i).get("id")%>">
                        <input type="hidden" name="driverUsername" value="<%=jsonArray.getJSONObject(i).get("username")%>">

                        <div class="section-profilepic">
                        <%// Get Profile
                        //String profile_string = profile.getProfile(login_cookie.getValue(), jsonArray.getJSONObject(i).getInt("id"));

                        //JSONObject profile_json = new JSONObject(profile_string);
                        out.print(Header.printProfile(profile_json.getBoolean("isDriver"),
                        profile_json.getString("profilePic"), "profilepic-big"));%></div>
                    <div class="section-stacked">
                        <div class="history-label align-left">
                            <div class="driver-name-label">
                                <%=jsonArray.getJSONObject(i).getString("name")%>
                            </div>
                            <div class="profile-rating"> &#9734
                                <%=jsonArray.getJSONObject(i).getDouble("rating")%>
                                <span class="profile-votes">(
                                <%=jsonArray.getJSONObject(i).getInt("votes")%> votes) </span>
                            </div>
                        </div>
                        <div class="button-section align-right">
                            <input type="submit" class="button-green" value="I choose you">
                        </div>
                    </div>
                </div>
            </form>

            <% } else { %>
            <div class="section-no-results"><br>No Results Found :( <br><br></div>
            <%  }}
            } else { %>
            <div class="section-no-results"><br>No Results Found :( <br><br></div>
            <%
                }
            %>

        </div>
    </div>
</div>
</body>
</html>