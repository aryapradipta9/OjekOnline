package user;

import db.Database;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface = "user.PrefLocImpl")
public class PrefLocImpl implements PrefLoc {

    @Override
    public String addPrefLoc(String accessToken, String location, int id) {
        // Check Token
        JSONObject json_return = new JSONObject();
        try {
            // Check Access Token to Identity Service
            TokenValidator tokenValidator = new TokenValidator();
            if(tokenValidator.validateToken(accessToken)) {
                Database db = new Database();
                db.openConnection();
                ArrayList<ArrayList<String>> result;
                result = db.select("SELECT place FROM preferred_loc WHERE id=" + id + " AND place='"+ location +"'");

                if(result.size() == 0){
                    db.update("INSERT INTO preferred_loc VALUES (" + id + ", 0, '" + location + "')");
                    json_return.put("status", true);
                }else{
                    json_return.put("status", false);
                }

                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json_return.toString();
    }

    @Override
    public String deletePrefLoc(String accessToken, String location, int id) {
        // Check Token
        JSONObject json_return = new JSONObject();
        try {
            // Check Access Token to Identity Service
            TokenValidator tokenValidator = new TokenValidator();
            if(tokenValidator.validateToken(accessToken)) {
                Database db = new Database();
                db.openConnection();
                ArrayList<ArrayList<String>> result;
                result = db.select("SELECT place FROM preferred_loc WHERE id=" + id + " AND place='"+ location +"'");

                if(result.size() == 0){
                    json_return.put("status", false);
                }else{
                    db.update("DELETE FROM preferred_loc WHERE id=" + id + " AND place='" + location + "'");
                    json_return.put("status", true);
                }

                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json_return.toString();
    }

    @Override
    public String editPrefLoc(String accessToken, String oldLocation, String newLocation, int id) {
        // Check Token

        JSONObject json_return = new JSONObject();
        try {
            // Check Access Token to Identity Service
            TokenValidator tokenValidator = new TokenValidator();
            if(tokenValidator.validateToken(accessToken)) {
                Database db = new Database();
                db.openConnection();
                ArrayList<ArrayList<String>> result;
                result = db.select("SELECT place FROM preferred_loc WHERE id=" + id + " AND place='"+ oldLocation +"'");

                if(result.size() == 0){
                    json_return.put("status", false);
                }else{
                    db.update("UPDATE preferred_loc SET place='" + newLocation + "' WHERE id=" + id + " AND place='" + oldLocation + "'");
                    json_return.put("status", true);
                }

                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json_return.toString();
    }

    @Override
    public String getPrefLoc(String accessToken, int id){
        JSONObject json_return = new JSONObject();

        try {
            // Check Access Token to Identity Service
            TokenValidator tokenValidator = new TokenValidator();
            if(tokenValidator.validateToken(accessToken)) {
                Database db = new Database();
                db.openConnection();
                ArrayList<ArrayList<String>> result;
                result = db.select("SELECT place FROM preferred_loc WHERE id=" + id);

                JSONArray location = new JSONArray();
                if(result.size() != 0){
                    for (ArrayList<String> places:result) {
                        String place = places.get(0);
                        location.put(place);
                    }
                }

                json_return.put("place", location);
                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json_return.toString();
    }
}
