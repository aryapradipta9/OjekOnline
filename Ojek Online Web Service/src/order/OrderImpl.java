package order;

import db.Database;
import java.util.ArrayList;
import java.util.Date;
import javax.jws.WebService;
import org.json.JSONArray;
import org.json.JSONObject;
import user.TokenValidator;

@WebService(endpointInterface = "order.OrderImpl")
public class OrderImpl implements Order {

  public String getDriver(String access_token, String pick_point, String destination,
      String preferred_driver, int user_id) {
    JSONObject json_return = new JSONObject();
    try {
      TokenValidator tokenValidator = new TokenValidator();
      if (tokenValidator.validateToken(access_token)) {
        Database db = new Database();
        db.openConnection();
        ArrayList<ArrayList<String>> result;
        result = db.select(
            "select distinct id, username, name, rating, profilePic, votes from user natural join driver natural join preferred_loc where (place = '"
                + pick_point + "' or place = '" + destination
                + "') and id <> "+ user_id +" and isDriver=1");

        boolean foundPref = false;

        JSONArray other = new JSONArray();
        JSONObject driver_json = null;

        for (ArrayList<String> driver : result) {
          String id = driver.get(0);
          driver_json = new JSONObject();
          driver_json.put("id", Integer.parseInt(driver.get(0)));
          driver_json.put("username", driver.get(1));
          driver_json.put("name", driver.get(2));
          driver_json.put("rating", Float.parseFloat(driver.get(3)));
          driver_json.put("profilePic", driver.get(4));
          driver_json.put("votes", Float.parseFloat(driver.get(5)));

          if (driver.get(1).equals(preferred_driver)) {
            json_return.put("preferred", driver_json);
            foundPref = true;
          } else {
            other.put(driver_json);
          }
        }

        if (!foundPref) {
          result = db.select(
              "select id, username, name, rating, profilePic, votes from user natural join driver where username = '" + preferred_driver
                  + "' and id <> " + user_id + " and isDriver=1");
          driver_json = new JSONObject();
          if(result.size() != 0) {
            driver_json.put("id", Integer.parseInt(result.get(0).get(0)));
            driver_json.put("username", result.get(0).get(1));
            driver_json.put("name", result.get(0).get(2));
            driver_json.put("rating", Float.parseFloat(result.get(0).get(3)));
            driver_json.put("profilePic", result.get(0).get(4));
            driver_json.put("votes", Integer.parseInt(result.get(0).get(5)));
          }
          json_return.put("preferred", driver_json);
        }

        json_return.put("other", other);

        db.closeConnection();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return json_return.toString();
  }

  public void completeOrder(String access_token, String string_parameter) {
    TokenValidator tokenValidator = new TokenValidator();

    try {
      Database db = new Database();
      if (tokenValidator.validateToken(access_token)) {
        db.openConnection();
        JSONObject json_parameter = new JSONObject(string_parameter);

        long date = System.currentTimeMillis()/1000L;
        String origin = json_parameter.getString("origin");
        String destination = json_parameter.getString("destination");
        Double rating = json_parameter.getDouble("rating");
        String comment = json_parameter.getString("comment");
        int driverId = json_parameter.getInt("driverId");
        int userId = json_parameter.getInt("userId");

        db.update(
            "insert into `order` values (0,"
                + date + ", '" + origin + "', '" + destination + "', " + rating + ", '" + comment
                + "', " + userId + ", " + driverId + ",0,0)");
        int count = Integer.parseInt(db.select(
            "select count(order_id) as 'count' from `order` where id_driver=" + driverId
                + " group by id_driver").get(0).get(0));
        float avg = Float.parseFloat(db.select(
            "select avg(order_rating) as 'average' from `order` where id_driver=" + driverId
                + " group by id_driver").get(0).get(0));
        db.update("update driver set rating=" + avg + ", votes=" + count + " where id=" + driverId);
      }
      db.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getDriverById(String access_token, int id) {
    JSONObject json_result = new JSONObject();
    try {
      TokenValidator tokenValidator = new TokenValidator();
      Database db = new Database();
      db.openConnection();
      if (tokenValidator.validateToken(access_token)) {
        ArrayList<ArrayList<String>> res = db.select(
            "select id, username, name, rating, profilePic, votes from driver natural join user where id = " + id);

        if (res.size() == 1) {
          json_result.put("id", Integer.parseInt(res.get(0).get(0)));
          json_result.put("username", res.get(0).get(1));
          json_result.put("name", res.get(0).get(2));
          json_result.put("rating", Float.parseFloat(res.get(0).get(3)));
          json_result.put("profilePic", res.get(0).get(4));
          json_result.put("votes", Integer.parseInt(res.get(0).get(5)));
        }
      }
      db.closeConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return json_result.toString();
  }
}
