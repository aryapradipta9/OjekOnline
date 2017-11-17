package util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    public static String identityService;
    private String urlString;

    /**
     * Constructor HTTPRequest.
     *
     * @param url URL yang akan diakses
     */
    public HttpRequest(String url) {
        this.urlString = url;
    }

    /**
     * Melakukan postRequest pada URL yang sudah diset.
     *
     * @param data data yang akan dikirim dalam postRequest
     * @return String hasil eksekusi postRequest
     */
    public String postRequest(String data) {
        String output = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length()));
            conn.setDoOutput(true);
            conn.getOutputStream().write(data.getBytes());

            output = getOutput(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public String getRequest(String parameter) {
        String output = "";
        try {
            URL url = new URL(urlString + parameter);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            output = getOutput(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    private String getOutput(HttpURLConnection conn) throws IOException {
        StringBuilder out_string = new StringBuilder();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String in = input.readLine();
            while (in != null) {
                out_string.append(in);
                in = input.readLine();
            }
        }
        return out_string.toString();
    }
}
