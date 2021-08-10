package ula.com.adtviewer.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ula.com.adtviewer.library.UserFunctions;

import ula.com.adtviewer.R;

/* Clase para inicio de sesion del usuario
 * @author carlos
 * @version 1
 */
public class LoginActivity extends Activity {
	Button btnLogin;
	Button btnLinkToChangePassword;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_USERID = "id";

    /**
     * Metodo onCreate()
     * @param savedInstanceState
     * @return  void
     * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Importa todos los valores del formulario
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToChangePassword = (Button) findViewById(R.id.btnLinkToChangePassword);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// evento click para el login
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				Log.d("Button", "Login");
				JSONObject json = userFunction.loginUser(email, password);

				// chequea la respuesta
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							// exito al inicio de sesion
							JSONObject json_user = json.getJSONObject("user");
                            //Obtiene el codigo del usuario
                            String userid = json.getString(KEY_USERID);
							// Inicia la pantalla de bandeja de entrada
                            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                            //agrea el id del usuario
                            dashboard.putExtra("userid", userid);
                            dashboard.putExtra("username", email);
                            // cierra las demas ventanas
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							
							// cierra
							finish();
						}else{
							// Error en login
							loginErrorMsg.setText("Nombre de usuario o contraseña incorrecta");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		// Enlace para cambiar la contraseña
        btnLinkToChangePassword.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
}
