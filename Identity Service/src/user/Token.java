package user;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Random;

public class Token {

    public JSONObject generateToken(String id, String username, String browser, String ipAdress) {
        JSONObject login_token = new JSONObject();

        // Generate magic string
        long unix_millis = System.currentTimeMillis();
        Random random = new Random(unix_millis);
        int magic_number = random.nextInt();
        String magic_string = username + Integer.toString(magic_number);
        byte[] bytes = magic_string.getBytes();

        try {
            // Get MD5 hash of magic string
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(bytes);

            // Convert to hex string
            StringBuffer hex = new StringBuffer();

            for(int i=0; i<hash.length; i++) {
                if ((0xFF & hash[i]) < 0x10) {
                    hex.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hex.append(Integer.toHexString(0xFF & hash[i]));
                }
            }

            String token = hex.toString().substring(0,8);
            login_token.put("id", id);
            login_token.put("token", token);
            login_token.put("browser", browser);
            login_token.put("ip",ipAdress);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return login_token;
    }

    public JSONObject getEmptyToken() {
        return new JSONObject();
    }
}