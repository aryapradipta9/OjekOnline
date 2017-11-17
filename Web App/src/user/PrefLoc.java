package user;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface PrefLoc {
    /**
     * Menambahkan preferredLocation.
     * @param accessToken accessToken dari user yang sedang login
     * @param location nama lokasi yang akan ditambahkan
     * @return
     */
    @WebMethod
    String addPrefLoc(String accessToken, String location, int id);

    /**
     * Menghapus lokasi preferredLocation.
     * @param accessToken accessToken dari user yang sedang login
     * @param location nama lokasi yang akan dihapus
     * @return
     */
    @WebMethod
    String deletePrefLoc(String accessToken, String location, int id);

    /**
     * Mengubah preferredLocation yang sudah ada.
     * @param accessToken accessToken dari user yang sedang login
     * @param oldLocation nama lokasi yang akan diubah
     * @param newLocation nama lokasi baru
     * @return
     */
    @WebMethod
    String editPrefLoc(String accessToken, String oldLocation, String newLocation, int id);

    @WebMethod
    String getPrefLoc(String accessToken, int id);

}
