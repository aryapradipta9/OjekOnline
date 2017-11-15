package user;

import javax.jws.WebService;

import db.Database;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@WebService(endpointInterface = "user.HistoryImpl")
public class HistoryImpl implements History {
    @Override
    public String getDriverHistory(String access_token, int id) {

        JSONObject history = new JSONObject();
        JSONArray all = new JSONArray();

        try {
            TokenValidator tokenValidator = new TokenValidator();

            if(tokenValidator.validateToken(access_token)) {
                Database db = new Database();
                db.openConnection();

                ArrayList<ArrayList<String>> result = db.select("SELECT * FROM `order` WHERE id_driver=" + id + " AND hidden_driver=false");

                for(ArrayList<String> orders : result) {
                    JSONObject order = new JSONObject();
                    order.put("order_id", orders.get(0));
                    order.put("date", new Date(Long.parseLong(orders.get(1)) * 1000L));
                    order.put("origin", orders.get(2));
                    order.put("destination", orders.get(3));
                    order.put("order_rating", orders.get(4));
                    order.put("comment", orders.get(5));
                    order.put("id_customer", orders.get(6));

                    all.put(order);
                }

                db.closeConnection();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        history.put("history", all);
        return history.toString();
    }

    @Override
    public String getOrderHistory(String access_token, int id) {

        JSONObject history = new JSONObject();
        JSONArray all = new JSONArray();

        try {
            TokenValidator tokenValidator = new TokenValidator();

            if(tokenValidator.validateToken(access_token)) {
                Database db = new Database();
                db.openConnection();

                ArrayList<ArrayList<String>> result = db.select("SELECT * FROM `order` WHERE id_customer=" + id + " AND hidden_customer=false");

                for(ArrayList<String> orders : result) {
                    JSONObject order = new JSONObject();
                    order.put("order_id", orders.get(0));
                    order.put("date", new Date(Long.parseLong(orders.get(1)) * 1000L));
                    order.put("origin", orders.get(2));
                    order.put("destination", orders.get(3));
                    order.put("order_rating", orders.get(4));
                    order.put("comment", orders.get(5));
                    order.put("id_driver", orders.get(7));

                    all.put(order);
                }

                db.closeConnection();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        history.put("history", all);
        return history.toString();
    }

    @Override
    public void hideHistory(String access_token, int order_id, String type) {

        try {
            TokenValidator tokenValidator = new TokenValidator();

            if(tokenValidator.validateToken(access_token)) {
                Database db = new Database();
                db.openConnection();

                if(type.equals("driver")) {
                    db.update("UPDATE `order` SET hidden_driver=true WHERE order_id=" + order_id);
                }
                else if(type.equals("user")) {
                    db.update("UPDATE `order` SET hidden_customer=true WHERE order_id=" + order_id);
                }

                db.closeConnection();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}