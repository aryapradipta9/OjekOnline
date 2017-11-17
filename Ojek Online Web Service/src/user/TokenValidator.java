package user;

import java.util.Base64;
import java.util.Base64.Decoder;
import org.json.JSONObject;
import util.Env;
import util.HttpRequest;

public class TokenValidator {

    public boolean validateToken(String token) throws Exception {
        // Check Access Token to Identity Service
        Env env = new Env();
        Decoder decoder = Base64.getDecoder();
        String json_token = new String(decoder.decode(token));
        HttpRequest validation = new HttpRequest("http://localhost:" + env.getIdentityPort()+"/validate");
        JSONObject validation_result = new JSONObject(validation.postRequest("token=" + json_token));

        return validation_result.getBoolean("status");
    }
}