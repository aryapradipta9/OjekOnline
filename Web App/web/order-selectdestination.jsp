<%@ page import="util.Header" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="user.Profile" %>
<%@ page import="javax.xml.ws.Service" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="java.net.URL" %>
<%@ page import="util.Env" %>
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
        int id = Integer.parseInt(request.getParameter("id"));
        String profile_string = profile
                .getProfile(login_cookie.getValue(), id);

        // Get Preferred Location

        profile_json = new JSONObject(profile_string);
        if (profile_json.getBoolean("isDriver")) {
            response.sendRedirect("/order-driverview.jsp?id=" + id);
            return;
        }
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
    <% out.print(Header.printNavbar(Integer.parseInt(request.getParameter("id")),0,response.getWriter()));%>
    <div class="section">
        <div class="section-header">
            <div class="section-title">
                MAKE AN ORDER
            </div>
        </div>
        <div class="section-step row">
            <div class="step active">
                <div class="step-no-container"><div class="step-no">1</div></div>
                <div class="step-guide">Select Destination</div>
            </div>
            <div class="step">
                <div class="step-no-container"><div class="step-no">2</div></div>
                <div class="step-guide">Select A Driver</div>
            </div>
            <div class="step">
                <div class="step-no">3</div>
                <div class="step-guide active">Chat Driver</div>
            </div>
            <div class="step">
                <div class="step-no-container"><div class="step-no">4</div></div>
                <div class="step-guide">Complete your order</div>
            </div>
        </div>
        <form action="order-selectdriver.jsp?id=<%=request.getParameter("id")%>" name="order" method="post" onsubmit="return validateOrder('order')">
            <input type="hidden" name="userId" value="<%=request.getParameter("id")%>">
            <div class="input-set">
                <div class="label">Picking point</div>
                <div class="field"><input type="text" name="origin"></div>
            </div>
            <div class="input-set">
                <div class="label">Destination</div>
                <div class="field"><input type="text" name="destination"></div>
            </div>
            <div class="input-set">
                <div class="label">Preferred Driver</div>
                <div class="field"><input type="text" name="preferreddriver" placeholder="(optional)"></div>
            </div>
            <div class="row">
                <div class="align-middle"><input type="submit" value="NEXT" class="button-green"></div>
            </div>
        </form>
    </div>
</div>
<script src="js/validate.js"></script>
</body>
</html>