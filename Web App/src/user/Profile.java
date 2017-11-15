package user;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService()
public interface Profile {

  /**
   * Mengembalikan isi profile dari user yang sedang login.
   *
   * @param access_token access token user yang sedang login
   */
  @WebMethod
  String getProfile(String access_token, int id);

  /**
   * Menambahkan profile baru.
   */
  @WebMethod
  String addProfile(int id, String name, String email, String phone, String username, String password,
      boolean isDriver);

  /**
   * Melakukan update pada profile user.
   *
   * @param access_token access token user yang sedang login.
   * @return true jika berhasil mengubah
   */
  @WebMethod
  void updateProfile(String access_token, int id, String name, String phone, String profilePic,
      String status);
}