package ni.edu.uca.mytwitterapplication;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ni.edu.uca.mytwitterapplication.api.ApiTwitter;
import ni.edu.uca.mytwitterapplication.api.service.MyTwitterService;
import ni.edu.uca.mytwitterapplication.domain.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// implementamos OnClickListener para eventos del boton y Callback<User> para obtener la peticion
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<User> {

    private final String TAG = MainActivity.class.getSimpleName();

    private MyTwitterService myTwitterService;

    private TextInputEditText textInputEditTextScreenName;
    private TextView textViewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyTwitterApp myTwitterApp = (MyTwitterApp) getApplication(); // obtenemos la clase application

        myTwitterService = myTwitterApp.getMyTwitterService(); // obtenemos el servicio que se conecta al API de Twitter

        textInputEditTextScreenName = findViewById(R.id.textInputEditTextScreenName);
        AppCompatButton appCompatBtnUserShow = findViewById(R.id.appCompatBtnUserShow);
        appCompatBtnUserShow.setOnClickListener(this); // hacemos referencia de cualquier evento a este boton
        textViewObject = findViewById(R.id.textViewObject);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appCompatBtnUserShow:
                String screenName = textInputEditTextScreenName.getText().toString(); // obtenemos el usuario twitter
                if (!screenName.trim().isEmpty()) {
                    // preparamos la peticion para acceder al API de Twitter
                    Call<User> userCall = myTwitterService.getUserByScreenName(screenName);
                    userCall.enqueue(this); // ejecutamos la peticion y hacemos referencia a cualquier devolucion en esta clase
                }
                break;
        }
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        // obtenemos el codigo de respuesta para procesar los resultados de la peticion
        switch (response.code()) {
            case ApiTwitter.HttpCodes.OK: // si la peticion es satisfactoria mostramos el resultado de la peticion
                final User user = response.body();
                if (user != null) {
                    textViewObject.setText(user.toString());
                }
                break;
            case ApiTwitter.HttpCodes.BAD_REQUEST:
                if (response.message() != null && !response.message().isEmpty()) {
                    Snackbar.make(textViewObject, response.message(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(textViewObject, "Petición incorrecta", Snackbar.LENGTH_LONG).show();
                }
                break;
            case ApiTwitter.HttpCodes.UNAUTHORIZED:
                if (response.message() != null && !response.message().isEmpty()) {
                    Snackbar.make(textViewObject, response.message(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(textViewObject, "No tiene Autorización para ver información del usuario " +
                            textInputEditTextScreenName.getText().toString(), Snackbar.LENGTH_LONG).show();
                }
                break;
            case ApiTwitter.HttpCodes.NOT_FOUND:
                if (response.message() != null && !response.message().isEmpty()) {
                    Snackbar.make(textViewObject, response.message(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(textViewObject, "No se encontró el recurso", Snackbar.LENGTH_LONG).show();
                }
                break;
            case ApiTwitter.HttpCodes.REQUEST_TIMEOUT:
                if (response.message() != null && !response.message().isEmpty()) {
                    Snackbar.make(textViewObject, response.message(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(textViewObject, "La petición duro demasiado tiempo", Snackbar.LENGTH_LONG).show();
                }
                break;
            case ApiTwitter.HttpCodes.INTERNAL_SERVER_ERROR:
                if (response.message() != null && !response.message().isEmpty()) {
                    Snackbar.make(textViewObject, response.message(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(textViewObject, "Hubo un error en el servidor", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        Log.e(TAG, t.getMessage(), t);
    }
}
