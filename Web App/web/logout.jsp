<%@ page import="util.Env" %>
<%
    try {
        Env env = new Env();
        response.sendRedirect("http://localhost:" + env.getIdentityPort() + "/logout");
    }
    catch(Exception e) {
        e.printStackTrace();
    }
%>