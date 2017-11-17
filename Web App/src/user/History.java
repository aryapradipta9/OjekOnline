package user;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService()
public interface History {
    /**
     * Mengembalikan daftar history sebagai driver.
     * @param access_token accessToken user yang sedang login
     * @return
     */
    @WebMethod
    String getDriverHistory(String access_token, int id);

    /**
     * Mengembalikan daftar history sebagai user.
     * @param access_token accessToken user yang sedang login
     * @return
     */
    @WebMethod
    String getOrderHistory(String access_token, int id);

    /**
     * Hide order history
     * @param user_id id dari user yang melakukan request untuk hide order history
     * @param order_id id dari order history yang akan di-hide
     * @return
     */
    @WebMethod
    void hideHistory(String access_token, int order_id, String type);
}
