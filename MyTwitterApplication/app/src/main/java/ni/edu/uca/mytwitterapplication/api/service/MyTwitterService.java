package ni.edu.uca.mytwitterapplication.api.service;

import ni.edu.uca.mytwitterapplication.api.ApiTwitter;
import ni.edu.uca.mytwitterapplication.domain.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz concreta que representa los endpoint de los servicios de Twitter.
 */
public interface MyTwitterService {

    /**
     * Metodo que representa al endpoint de Twitter para mostrar informacion del usuario.
     * <a href="https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-show">
     *     Endpoint GET users/show
     * </a>
     * @param screenName es el nombre de usuario de Twitter.
     * @return User objeto que contiene los datos del usuairo.
     */
    @GET(ApiTwitter.RESOURCE_USER_SHOW)
    Call<User> getUserByScreenName(
            @Query(ApiTwitter.QUERY_PARAMS_SCREEN_NAME) String screenName
    );

}
