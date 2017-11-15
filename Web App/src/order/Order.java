package order;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Order {

  @WebMethod
  public String getDriver(String access_token, String pick_point, String destination,
                          String preferred_driver, int user_id);

  @WebMethod
  public void completeOrder(String access_token, String string_parameter);

  @WebMethod
  public String getDriverById(String access_token, int id);

}
