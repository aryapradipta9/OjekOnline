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
import java.util.logging.Logger;

@WebServlet(name = "user.Validator")
public class Validator extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject auth_token = new JSONObject(request.getParameter("token"));
        boolean token_valid = false;

        // Validate token
        try {
            Database db = new Database();
            db.openConnection();
            ArrayList<ArrayList<String>> result = db.select("SELECT * FROM user WHERE id=" + auth_token.getInt("id"));
            Logger logger = Logger.getLogger(getClass().getName());
            logger.fine(result.toString());

            if(result.size() == 1) {
                // Get token code
                String user_token = result.get(0).get(3);
                long expiry_time = Long.parseLong(result.get(0).get(4));

                if (user_token != null && user_token.equals(auth_token.getString("token"))) {
                    // Renew Token
                    long unix_time = System.currentTimeMillis() / 1000L;
                    if (expiry_time < unix_time) {
                        expiry_time = unix_time + 900;
                        db.update("UPDATE user SET expiry=" + expiry_time + " WHERE id=" + auth_token.getInt("id"));
                    }
                    token_valid = true;
                }
            }

            db.closeConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Write response
        JSONObject return_json = new JSONObject();
        return_json.put("status", token_valid);

        PrintWriter out = response.getWriter();
        out.println(return_json.toString());
    }
}
