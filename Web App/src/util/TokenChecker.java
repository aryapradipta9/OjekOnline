package util;

import org.json.JSONObject;

import java.util.Base64;

/**
 * Created by 13515017 / Putu Arya Pradipta.
 * Tanggal 11/22/2017.
 * FileName : TokenChecker.java.
 */
public class TokenChecker {

  public static boolean checkToken(String currentUA, String token, String ipAddr) {
    // parse UA
    String browser = parseUA(currentUA);
    Base64.Decoder decoder = Base64.getDecoder();
    String json_token = new String(decoder.decode(token));
    JSONObject obj = new JSONObject(json_token);
    String tokenBrowser = obj.getString("browser");
    String tokenIP = obj.getString("ip");
    boolean temp = tokenBrowser.equals(browser);
    boolean temp2 = tokenIP.equals(ipAddr);
    System.out.println(temp);
    if ((!temp)&&(!temp2)){
      return false;
    }
    return true;
  }

  public static String parseUA(String userAgent ) {
    String user = userAgent.toLowerCase();
    String browser = null;
    if (user.contains("msie"))
    {
      String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
      browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
    } else if (user.contains("safari") && user.contains("version"))
    {
      browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
    } else if ( user.contains("opr") || user.contains("opera"))
    {
      if(user.contains("opera"))
        browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
      else if(user.contains("opr"))
        browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
    } else if (user.contains("chrome"))
    {
      browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
    } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  || (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) || (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) )
    {
      //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
      browser = "Netscape-?";

    } else if (user.contains("firefox"))
    {
      browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
    } else if(user.contains("rv"))
    {
      browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
    } else
    {
      browser = "UnKnown, More-Info: "+userAgent;
    }
    return browser;
  }
}
