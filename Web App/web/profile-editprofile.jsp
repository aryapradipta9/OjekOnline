<%@ page import="user.Profile" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="java.net.URL" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>

<!--require_once("include/User.php");
require_once("include/Common.php");
require_once("include/FileUpload.php");

$userdata = getUserbyId($_GET["id"]);-->
<%
    Env env = new Env();
    URL profile_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/profile?wsdl");
    JSONObject profile_json = null;

    QName profile_qname = new QName("http://user/", "ProfileImplService");
    QName profile_qname_port = new QName("http://user/", "ProfileImplPort");
    Service service = Service.create(profile_url, profile_qname);
    Profile profile = service.getPort(profile_qname_port, Profile.class);

    Cookie[] cookies = request.getCookies();
    Cookie login_cookie = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login_token")) {
                login_cookie = cookie;
            }
        }
    }

    if (login_cookie != null) {
        // Get Profile
        String profile_string = profile
                .getProfile(login_cookie.getValue(), Integer.parseInt(request.getParameter("id")));

        // Get Preferred Location

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
                EDIT PROFILE INFORMATION
            </div>
        </div>
        <form class="edit" action="/update" name="profileedit" enctype="multipart/form-data"
              onsubmit="validateUpdateprofile('profileedit')" method="post">
            <input type="hidden" name="id" value="<%= request.getParameter("id")%>">
            <div class="edit-profilepic">
                <div><% //printProfile($userdata, "profilepic-big") %></div>
                <div class="field"><input type="file" name="profpic" onchange="fileGet()"></div>
            </div>
            <div class="input-set">
                <div class="label">Your Name</div>
                <div class="field"><input type="text" name="name" value="<%= profile_json.getString("name") %>"></div>
            </div>
            <div class="input-set">
                <div class="label">Phone</div>
                <div class="field"><input type="text" name="phone" value="<%= profile_json.getString("phone") %>"></div>
            </div>
            <div class="input-set">
                <div class="label">Status Driver</div>
                <div class="status field">
                    <div class="slider-check">
                        <input type="checkbox" id="status" name="status" <%
                        if (profile_json.getBoolean("isDriver")) {
                            out.print("checked");
                        }
                        %>>
                        <label for="status" class="slider"></label>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="align-left">
                    <a href="profile.jsp?id=<%= request.getParameter("id")%>" ><button type="button" class="button-red">BACK</button></a>
                </div>
                <div class="align-right"><input type="submit" name="submit" value="SAVE" class="button-green"></div>
            </div>
        </form>
    </div>
</div>
<script src="js/validate.js"></script>
<script src="js/updatePic.js"></script>
</body>
</html>
