<%@ page import="java.net.URL" %>
<%@ page import="util.Env" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="user.Profile" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="util.TokenChecker" %>
<%@ page import="user.PrefLoc" %>
<%@ page import="util.Header" %>
<%
    Env env = new Env();
    URL profile_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/profile?wsdl");
    URL order_url = new URL("http://localhost:"+env.getOjekPort()+"/services/user/preferredlocation?wsdl");

    // Create Profile Service Endpoint
    QName profile_qname = new QName("http://user/", "ProfileImplService");
    QName profile_qname_port = new QName("http://user/", "ProfileImplPort");
    Service profile_service = Service.create(profile_url, profile_qname);
    Profile profile = profile_service.getPort(profile_qname_port, Profile.class);

    // Create Preffered Location Service Endpoint
    QName pref_loc_qname = new QName("http://user/", "PrefLocImplService");
    QName pref_loc_qname_port = new QName("http://user/", "PrefLocImplPort");
    Service pref_loc_service = Service.create(order_url, pref_loc_qname);
    PrefLoc pref_loc = pref_loc_service.getPort(pref_loc_qname_port, PrefLoc.class);

    Cookie[] cookies = request.getCookies();
    Cookie login_cookie = null;
    Cookie username = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login_token")) {
                login_cookie = cookie;
            } else if (cookie.getName().equals("username")) {
              username = cookie;
            }
        }
    }

    JSONObject profile_json = null;
    JSONObject pref_loc_json = null;
    String ua = request.getHeader("User-Agent");
    String ipAddress = request.getHeader("X-FORWARDED-FOR");
    if(ipAddress == null){
        ipAddress = request.getRemoteAddr();
    }
    if ((login_cookie != null) && (TokenChecker.checkToken(ua,login_cookie.getValue(),ipAddress))) {
        int id = Integer.parseInt(request.getParameter("id"));
        // Get Profile
        String pref_loc_string = pref_loc.getPrefLoc(login_cookie.getValue(), id);
        String profile_string = profile.getProfile(login_cookie.getValue(), id);

        if(profile_string.equals("{}")){
            // tidak terdapat profil orang dengan id sekian
            response.sendRedirect("/login.jsp");
            return;
        }else {
            // cek apakah cookie driver username ada dan sama dengan id sekarang
          // Get Preferred Location


            profile_json = new JSONObject(profile_string);
            if (profile_json.getString("username").equals(username.getValue())) {
                if (!profile_json.getBoolean("isDriver")) {
                    response.sendRedirect("/order-selectdestination.jsp?id=" + id);
                    return;
                } else {
                    pref_loc_json = new JSONObject(pref_loc_string);

                }
            } else {
                response.sendRedirect("/login.jsp");
                return;
            }
        }

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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" ></script>
    <!-- Angularjs script>
    <script src="bower_components/angular/angular.js"></script>
    <script src="bower_components/rltm/web/rltm.js"></script>
    <script src="angular-chat.js"></script>
    <!-->
</head>
<body>
<div class="container">
    <% out.print(Header.printHeader(Integer.parseInt(request.getParameter("id")),profile_json.getString("username"),response.getWriter()));%>
    <% out.print(Header.printNavbar(Integer.parseInt(request.getParameter("id")),0,response.getWriter()));%>
    <iframe src="http://localhost:8080/" height="500px" width="100%" style="border:none;"> </iframe>

    <div class="section" id="content">
        <!--<html>
            <body>
                TEST
            </body>
        </html>
        <!--<div class="section-header">
            <div class="section-title">
                LOOKING FOR AN ORDER
            </div>
        </div>
            
        <button class="find-order" onclick="showFO()">FIND ORDER</button>

        <div class="finding-order">
            <h2>Finding Order.....</h2>
            <button class="chat-close">CANCEL</button>
        </div>!-->
    </div>

</div>


</body>
</html>