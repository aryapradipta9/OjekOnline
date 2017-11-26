package user;

import db.Database;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "user.Register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String browser = request.getParameter("ua");
        String ipAddress = request.getParameter("ip");

        try{
            Database db = new Database();
            db.openConnection();
            ArrayList<ArrayList<String>> result;
            result = db.select("SELECT id FROM user WHERE username='" + username + "' OR email='"+ email +"'");

            Token token = new Token();
            JSONObject return_json = new JSONObject();

            if(result.size() == 0){
                db.update("INSERT INTO user VALUES (0, '" + username + "','" + password + "', NULL, 0, '" + email + "')");

                result = db.select("SELECT id FROM user WHERE username='" + username + "'");
                String id = result.get(0).get(0);

                return_json.put("status", true);
                return_json.put("login_token", token.generateToken(id, username, browser, ipAddress));

                String token_value = return_json.getJSONObject("login_token").getString("token");
                long unix_time = System.currentTimeMillis() / 1000L;
                long expiry_time = unix_time + 900; // expiry time 15 minutes
                db.update("UPDATE user SET token='" + token_value + "', expiry=" + expiry_time + " WHERE id=" + id);
                // TODO: SET TOKEN
            }else {
                return_json.put("status", false);
                return_json.put("login_token", token.getEmptyToken());
            }

            db.closeConnection();

            // Write response
            PrintWriter out = response.getWriter();
            out.println(return_json.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
