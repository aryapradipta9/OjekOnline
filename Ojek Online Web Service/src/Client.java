import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.json.JSONObject;
import user.Profile;
import util.Env;
import util.HttpRequest;

public class Client {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8082/services/user/profile?wsdl");

        QName qname = new QName("http://user/","ProfileImplService");
        QName qname_port = new QName("http://user/","ProfileImplPort");
        Service service = Service.create(url,qname);
        Profile profile = service.getPort(qname_port, Profile.class);

        JSONObject json = new JSONObject();

        json.put("id",1);
        json.put("token","token");

        System.out.println(profile.getProfile(json.toString(),1));

        /*HttpRequest validation = new HttpRequest(HttpRequest.identityService+"validate");
        System.out.println(validation.postRequest("token={\"id\":1,\"token\":\"token\"}"));*/

        Env env = new Env();
        HttpRequest get = new HttpRequest("http://localhost:" + Integer.toString(env.getIdentityPort())+"/profile");
        System.out.println(get.getRequest("?id=1"));
    }
}
