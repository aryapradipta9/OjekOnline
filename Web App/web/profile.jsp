<%@ page import="java.net.URL" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="user.PrefLoc" %>
<%@ page import="user.Profile" %>
<%@ page import="util.Env" %>
<%@ page import="util.Header" %>
<%@ page import="util.TokenChecker" %>
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

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login_token")) {
                login_cookie = cookie;
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
    System.out.println(ua);
    if ((login_cookie != null) && (TokenChecker.checkToken(ua,login_cookie.getValue(),ipAddress))) {
        int id = Integer.parseInt(request.getParameter("id"));
        // Get Profile
        String pref_loc_string = pref_loc.getPrefLoc(login_cookie.getValue(), id);
        String profile_string = profile.getProfile(login_cookie.getValue(), id);
        if(profile_string.equals("{}")){
            response.sendRedirect("/login.jsp");
            return;
        }else {
            // Get Preferred Location


            profile_json = new JSONObject(profile_string);
            pref_loc_json = new JSONObject(pref_loc_string);
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
</head>
<body>
<div class="container">
    <%
        try{
            out.print(Header.printHeader(Integer.parseInt(request.getParameter("id")),profile_json.getString("username"),response.getWriter()));
        }catch(Exception e){
            e.printStackTrace();
        }
    %>
    <% out.print(Header.printNavbar(Integer.parseInt(request.getParameter("id")),2,response.getWriter()));%>
    <div class="section">
        <div class="section-header">
            <div class="section-title">
                MY PROFILE
            </div>
            <div class="section-edit-button">
                <a href="profile-editprofile.jsp?id=<%= profile_json.getInt("id")%>">&#x270E;</a>
            </div>
        </div>
        <div class="section-profile">
            <div><% out.print(Header.printProfile(profile_json.getBoolean("isDriver"),profile_json.getString("profilePic"),"profilepic-big-round")); %></div>
            <div class="profile-username">@<%= profile_json.getString("username")%>
            </div>
            <div class="profile-name"><%= profile_json.getString("name")%>
            </div>
            <div class="profile-status">
					<span class="profile-type">
                        <%
                            if (profile_json.getBoolean("isDriver")) {
                                out.print("Driver");
                            } else {
                                out.print("Non-Driver");
                            }
                        %>
					</span>
                <%
                    if (profile_json.getBoolean("isDriver")) {
                        out.print("| <span class=\"profile-rating\">  &#9734 " + Double
                                .toString(profile_json.getDouble("rating")));
                        out.print("(" + Integer.toString(profile_json.getInt("votes"))
                                + "votes) </span>");
                    }
                %>
            </div>
            <div class="profile-email"> &#x2709;<%= profile_json.getString("email")%>
            </div>
            <div class="profile-phone">&#x260F;<%= profile_json.getString("phone")%>
            </div>
        </div>
        <%
            if (profile_json.getBoolean("isDriver")) {
        %>
        <div class="section-subheader">
            <div class="section-title">Preferred Locations</div>
            <div class="section-edit-button">
                <a href="profile-editlocations.jsp?id=<%= profile_json.getInt("id")%>">&#x270E;</a>
            </div>
        </div>

        <%
            JSONArray pref_loc_array = pref_loc_json.getJSONArray("place");
            if (pref_loc_array.length() == 0) {
                out.println("No Preferred Location Yet");
            } else {
                out.println("<div class=\"section-location\">");
                for (int i = 0; i < pref_loc_array.length(); i++) {
                    out.println("<ul><li>");
                    out.println(pref_loc_array.getString(i));
                    out.println("</li>");
                }
                for (int i = 0; i < pref_loc_array.length(); i++) {
                    out.println("</ul>");
                }
                out.println("<div>");
            }
        %>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
