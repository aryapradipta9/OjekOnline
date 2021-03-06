<%@ page import="java.net.URL" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="user.History" %>
<%@ page import="user.Profile" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>

<%

    try {
        Env env = new Env();
        URL url = new URL(
                "http://localhost:" + env.getOjekPort() + "/services/user/history?wsdl");
        URL profile_url = new URL(
                "http://localhost:" + env.getOjekPort() + "/services/user/profile?wsdl");

        QName qname = new QName("http://user/", "HistoryImplService");
        QName qname_port = new QName("http://user/", "HistoryImplPort");
        Service service = Service.create(url, qname);
        History history = service.getPort(qname_port, History.class);

        // Create Profile Service Endpoint
        QName profile_qname = new QName("http://user/", "ProfileImplService");
        QName profile_qname_port = new QName("http://user/", "ProfileImplPort");
        Service profile_service = Service.create(profile_url, profile_qname);
        Profile profile = profile_service.getPort(profile_qname_port, Profile.class);

        Cookie[] cookies = request.getCookies();
        Cookie login_cookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login_token")) {
                    login_cookie = cookie;
                }
            }
        }

        JSONObject profile_json = null;
        if (login_cookie != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            // Get Profile
            String profile_string = profile.getProfile(login_cookie.getValue(), id);

            profile_json = new JSONObject(profile_string);
        } else {
            response.sendRedirect("/login.jsp");
            return;
        }
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>VROOOM! - A Solution for Your Transportation</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="container">
    <%out.print(Header.printHeader(Integer.parseInt(request.getParameter("id")),
            profile_json.getString("username"), response.getWriter()));%>
    <%out.print(Header.printNavbar(Integer.parseInt(request.getParameter("id")), 1,
            response.getWriter()));%>
    <div class="section">
        <div class="section-header">
            <div class="section-title">
                TRANSACTION HISTORY
            </div>
        </div>
        <div class="nav">
            <a href="history-orderhistory.jsp?id=<%= request.getParameter("id")%>">
                <div class="nav-item-transaction">MY PREVIOUS ORDERS</div>
            </a>
            <a href="history-driverhistory.jsp?id=<%= request.getParameter("id")%>">
                <div class="nav-item-transaction active">DRIVER HISTORY</div>
            </a>
        </div>
        <%
                if (login_cookie != null) {
                    JSONObject results = new JSONObject(
                            history.getDriverHistory(login_cookie.getValue(),
                                    Integer.parseInt(request.getParameter("id"))));
                    JSONArray history_json = results.getJSONArray("history");
                    int count = history_json.length();

                    if (count != 0) {
                        for (int cnt = 0; cnt < count; cnt++) {

                            int id_customer = history_json.getJSONObject(cnt).getInt("id_customer");
                            results = new JSONObject(
                                    profile.getProfile(login_cookie.getValue(), id_customer));
                            String name = results.getString("name");

                            out.print("<div class=\"row\" id=\"history- echo" + cnt + "\">"
                                    + "<div>" + Header
                                    .printProfile(results.getBoolean("isDriver"),
                                            results.getString("profilePic"),
                                            "profilepic-big")
                                    + "</div>"
                                    + "<div class=\"history-label\">"
                                    + "<div class=\"history-date-label\">" + history_json
                                    .getJSONObject(cnt).get("date") + "</div>");

                            out.print("<div class=\"history-driver-name-label\">" + name + "</div >"
                                    + "<div class=\"history-other-data\">" + history_json
                                    .getJSONObject(cnt).get("origin") + "&#8594" + history_json
                                    .getJSONObject(cnt).get("destination")
                                    + "<br> gave <span class=\"profile-rating\">" + history_json
                                    .getJSONObject(cnt).get("order_rating")
                                    + " </span> stars to this order <br> and left comment: <div class=\"history-comment\">"
                                    + history_json.getJSONObject(cnt).get("comment") + "</div>"
                                    + "</div>"
                                    + "</div>"
                                    + "<div>"
                                    + "<form method = \"post\" action = \"/hide\">"
                                    + "<input type = \"hidden\" name = \"id\" value = \"" + request
                                    .getParameter("id")
                                    + "\">"
                                    + "<input type = \"hidden\" name = \"order_id\"  value = \""
                                    + history_json.getJSONObject(cnt).get("order_id") + "\">"
                                    + "<input type = \"hidden\" name = \"type\" value = \"driver\" >"
                                    + "<button class=\"button-red\" > HIDE </input >"
                                    + "</form>"
                                    + "</div >"
                                    + "</div >");
                        }
                    } else {
                        out.print("<div class=\"section\">No History Yet!</div>");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
    </div>
</div>
<script src="js/hide.js"></script>
</body>
</html>