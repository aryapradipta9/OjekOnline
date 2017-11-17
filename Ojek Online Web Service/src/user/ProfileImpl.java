package user;

import db.Database;
import org.json.JSONObject;

import javax.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface = "user.ProfileImpl")
public class ProfileImpl implements Profile {

    @Override
    public String getProfile(String access_token, int id) {

        JSONObject json_return = new JSONObject();

        try {
            TokenValidator tokenValidator = new TokenValidator();
            if (tokenValidator.validateToken(access_token)) {
                //ambil dari Database user + rating + votes
                Database db = new Database();
                db.openConnection();

                ArrayList<ArrayList<String>> result = db
                        .select("SELECT * FROM user WHERE id=" + Integer.toString(id));
                json_return.put("id", result.get(0).get(0));
                json_return.put("name", result.get(0).get(1));
                json_return.put("email", result.get(0).get(2));
                json_return.put("phone", result.get(0).get(3));
                String profilePic = result.get(0).get(4) == null? "." : result.get(0).get(4);
                json_return.put("profilePic", profilePic);
                json_return.put("username", result.get(0).get(5));

                boolean isDriver = Integer.parseInt(result.get(0).get(7)) == 1;
                json_return.put("isDriver", Boolean.toString(isDriver));

                if (isDriver) {
                    result = db.select("SELECT * FROM driver WHERE id=" + Integer.toString(id));
                    json_return.put("rating", result.get(0).get(1));
                    json_return.put("votes", result.get(0).get(2));
                }

                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json_return.toString();
    }

    @Override
    public String addProfile(int id, String name, String email, String phone,
                             String username, String password, boolean isDriver) {
        String json_return = "{}";
        try {
            // Check Access Token to Identity Service
            Database db = new Database();
            db.openConnection();
            ArrayList<ArrayList<String>> result;
            result = db.select("SELECT * FROM user WHERE username='" + username + "'");

            if (result.size() == 0) {
                // do nothing
                int driverStatus = isDriver ? 1 : 0;
                db.update(
                        "INSERT INTO user VALUES ("+id+",'" + name + "','" + email + "','" + phone + "',NULL,'"
                                + username + "','" + password + "'," + driverStatus + ")");
                result = db.select("SELECT id FROM user WHERE username='" + username + "'");
                db.update("INSERT INTO driver (id, rating, votes) values (" + result.get(0).get(0)
                        + ", 0, 0)");
                result = db.select("SELECT id FROM user WHERE username='" + username + "'");
                json_return = "{\"status\":true, \"id\":" + result.get(0).get(0) + "}";
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json_return;
    }

    @Override
    public void updateProfile(String access_token, int id, String name, String phone,
                              String profilePic, String status) {
        try {
            // Check Access Token to Identity Service
            TokenValidator tokenValidator = new TokenValidator();
            if (tokenValidator.validateToken(access_token)) {
                int isDriver = status != null ? 1 : 0;
                profilePic = profilePic.equals("") ? "NULL" : "'"+profilePic+"'";

                Database db = new Database();
                db.openConnection();
                ArrayList<ArrayList<String>> result;
                result = db.select("SELECT * FROM user WHERE id=" + id);

                if (result.size() != 0) {
                    db.update("UPDATE user SET name='" + name + "', phone='" + phone + "', profilePic="
                            + profilePic + ", isDriver=" + isDriver + " WHERE id=" + id);
                }

                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}