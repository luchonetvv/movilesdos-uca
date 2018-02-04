package ni.edu.uca.mytwitterapplication.api;

/**
 * Clase ayuda para definir endpoint recursos y estados de la peticion.
 */
public class ApiTwitter {

    public static final String URL_BASE = "https://api.twitter.com/1.1/";

    public static final String RESOURCE_USER_SHOW = "users/show.json";
    public static final String QUERY_PARAMS_SCREEN_NAME = "screen_name";

    public static class HttpCodes {
        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int NOT_FOUND = 404;
        public static final int REQUEST_TIMEOUT = 408;
        public static final int INTERNAL_SERVER_ERROR = 500;
    }

}
