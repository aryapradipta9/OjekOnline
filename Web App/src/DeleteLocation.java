import user.PrefLoc;
import util.Env;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;

@WebServlet(name = "DeleteLocation")
public class DeleteLocation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Cookie[] cookies = request.getCookies();
            Cookie login_cookie = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("login_token")) {
                        login_cookie = cookie;
                    }
                }
            }

            Env env = new Env();
            URL url = new URL("http://localhost:" + env.getOjekPort() + "/services/user/preferredlocation?wsdl");
            QName qname = new QName("http://user/", "PrefLocImplService");
            QName qname_port = new QName("http://user/", "PrefLocImplPort");

            Service service = Service.create(url, qname);
            PrefLoc prefloc = service.getPort(qname_port, PrefLoc.class);
            String location = request.getParameter("oldlocation");
            int id = Integer.parseInt(request.getParameter("id"));

            if(!location.equals("")){
                prefloc.deletePrefLoc(login_cookie.getValue(), location, id);
            }

            response.sendRedirect("profile-editlocations.jsp?id=" + id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

/*
<?php
require_once("include/SQLConnection.php");

if ($_SERVER['REQUEST_METHOD'] == "POST") {
    $newlocation = $_POST["newlocation"];
    $id = $_POST["id"];
    $location = $_POST["oldlocation"];

    $new = new SQLConnection();
    $new->runQuery("DELETE FROM preferred_loc where id=$id and place='$location'");

    header("location: profile-editlocations.php?id=" . $id);
}
?>*/