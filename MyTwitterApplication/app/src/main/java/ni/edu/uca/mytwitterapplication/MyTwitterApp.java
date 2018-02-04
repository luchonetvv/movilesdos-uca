package ni.edu.uca.mytwitterapplication;

import android.app.Application;

import ni.edu.uca.mytwitterapplication.api.ApiTwitter;
import ni.edu.uca.mytwitterapplication.api.HeaderInterceptor;
import ni.edu.uca.mytwitterapplication.api.service.MyTwitterService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase Application que esta en el contexto de toda la app.
 */
public class MyTwitterApp extends Application {

    // variable myTwitterService
    private MyTwitterService myTwitterService;

    @Override
    public void onCreate() {
        super.onCreate();

        // definimos un cliente para interceptar la peticion y establecer las cabeceras
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor()) // clase HeaderInterceptor
                .build();
        // empezamos a construir nuestro servicio para conertarnos al API Twitter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiTwitter.URL_BASE) // le pasamos la url base https://api.twitter.com/1.1/
                .client(okHttpClient) // le pasamos el cliente para interceptar la peticion
                .addConverterFactory(GsonConverterFactory.create()) // agregamos una fabrica para converteir de json a objeto java
                .build();

        // creamos nuestro servicio que se conecta al API de Twitter
        myTwitterService = retrofit.create(MyTwitterService.class);
    }

    /**
     * Obtener una instancia del servicio para conectarse con el API Twitter.
     * @return MyTwitterService clase que contiene los metodos para el llamado del API Twitter.
     */
    public MyTwitterService getMyTwitterService() {
        return myTwitterService;
    }
}
