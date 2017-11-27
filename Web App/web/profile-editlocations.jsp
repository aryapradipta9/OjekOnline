<%@ page import="user.PrefLoc" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.net.URL" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>
<%@ page import="user.Profile" %>

<!--php
require_once("include/SQLConnection.php");
require_once("include/User.php");

-->
<%
    // Get Profile
    Env env = new Env();
    URL profile_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/preferredlocation?wsdl");
    URL user_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/profile?wsdl");
    JSONObject location_json = null;
    JSONObject profile_json = null;

    // Create Profile Service Endpoint
    QName profile_qname = new QName("http://user/", "PrefLocImplService");
    QName profile_qname_port = new QName("http://user/", "PrefLocImplPort");
    Service service = Service.create(profile_url, profile_qname);
    PrefLoc prefLoc = service.getPort(profile_qname_port, PrefLoc.class);

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

    int id = Integer.parseInt(request.getParameter("id"));
    if (login_cookie != null) {
        // Get Preferred Location
        String location_string = prefLoc.getPrefLoc(login_cookie.getValue(), id);
        location_json = new JSONObject(location_string);
        String profile_string = user
                .getProfile(login_cookie.getValue(), Integer.parseInt(request.getParameter("id")));

        profile_json = new JSONObject(profile_string);
    } else {
        response.sendRedirect("login.jsp");
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
    <% out.print(Header.printHeader(Integer.parseInt(request.getParameter("id")),profile_json.getString("username"),response.getWriter()));%>
    <% out.print(Header.printNavbar(Integer.parseInt(request.getParameter("id")),2,response.getWriter()));%>
    <div class="section">
        <div class="section-header">
            <div class="section-title">
                EDIT PREFERRED LOCATIONS
            </div>
        </div>
        <div class="row">
            <div class="section-table">
                <table class="location-table">
                    <tr>
                        <th> No
                        <th> Location
                        <th> Actions
                    </tr>
                    <%
                    //$conn = new SQLConnection();
                        for (int i = 0;i<location_json.getJSONArray("place").length();i++) {
                            out.println("<tr>" +
                                        "<td> " + (i+1) + " </td>" +
                                        "<td id=\"location-" + (i+1) + "\">" +
                                        "<form action=\"/updatelocation\" name=\"location-form-" + (i+1) + "\" method=\"post\">" +
                                        "<input type=\"hidden\" name=\"id\" value=\"" + request.getParameter("id") + "\">" +
                                        "<input type=\"hidden\" name=\"oldlocation\" id=\"location-oldvalue-" + (i+1) + "\" value=\"" + location_json.getJSONArray("place").get(i) + "\">" +
                                        "<input type=\"hidden\" name=\"newlocation\" id=\"location-newvalue-" + (i+1) + "\" value=\"" + location_json.getJSONArray("place").get(i) + "\">" +
                                        "</form>" +
                                        "<div id=\"location-value-" + (i+1) + "\">" + location_json.getJSONArray("place").get(i) +
                                        "</div>" +
                                        "</td>");
                            out.println("<td class=\"section-actions-column\">" +
                                        "<div class=\"section-edit-button-set\">" +
                                        "<div class=\"actions-save\" id=\"location-save-" + (i+1) + "\" onclick=\"saveloc(" + (i+1) + ")\">&#10004;</div>" +
                                        "<div class=\"actions-edit\" id=\"location-edit-" + (i+1) + "\" onclick=\"editloc(" + (i+1) + ")\">&#x270E;</div>" +
                                        "<div class=\"actions-delete\" onclick=\"deleteloc(" + (i+1) + ")\">&#10060;</div>" +
                                        "</div>" +
                                        "</td>");
                            out.println("</tr>");
                        }

                    %>
                </table>
            </div>
        </div>
        <div class="section-subheader">
            <div class="section-title">
                ADD NEW LOCATION
            </div>
        </div>
        <form action="/addlocation" class="edit" method="post">
            <div class="input-set">
                <input type="hidden" name="id" value=<%= request.getParameter("id")%>>
                <div class="field"><input type="text" name="newlocation"></div>
                <input type="submit" value="ADD" class="button-green" name="button">
            </div>
        </form>
        <div class="button-red">
            <a href="profile.jsp?id=<%= id %>">
                <button type="button" class="button-red">BACK</button>
            </a>
        </div>
    </div>
</div>
<script src="js/updateloc.js"></script>
</body>
</html>