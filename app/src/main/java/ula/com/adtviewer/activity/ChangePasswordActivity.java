package ula.com.adtviewer.activity;

import ula.com.adtviewer.R;

import org.json.JSONException;
import org.json.JSONObject;

import ula.com.adtviewer.library.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Define la actividad para el cambio de contraseña del usuario
 * @author Carlos
 * @version 1
 */
public class ChangePasswordActivity extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputName;
    EditText inputOldPassword;
	EditText inputNewPassword1;
	EditText inputNewPassword2;
	TextView registerMsg;
	
	// JSON repuesta
	private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR_MSG = "error_msg";

    /**
     * Metodo onCreate()
     * @param savedInstanceState
     * @return  void
     * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);

		// importa la data del formulario
        inputName = (EditText) findViewById(R.id.changePasswordUserName);
        inputOldPassword = (EditText) findViewById(R.id.changePasswordOldPassword);
        inputNewPassword1 = (EditText) findViewById(R.id.changePasswordNewPassword1);
        inputNewPassword2 = (EditText) findViewById(R.id.changePasswordNewPassword2);
		btnRegister = (Button) findViewById(R.id.changePasswordBtn);
		btnLinkToLogin = (Button) findViewById(R.id.changePasswordBtnLinkToLoginScreen);
        registerMsg = (TextView) findViewById(R.id.changePasswordMessage);
		
		// registra el evento del boton
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				String user = inputName.getText().toString();
				String old_password = inputOldPassword.getText().toString();
				String new_password1 = inputNewPassword1.getText().toString();
                String new_password2 = inputNewPassword2.getText().toString();

                if(!new_password1.equals(new_password2)) {
                    registerMsg.setText("Las claves ingresadas son diferentes");
                    return;
                }

				UserFunctions userFunction = new UserFunctions();
				JSONObject json = userFunction.changePassword(user, old_password, new_password2);

                if(json == null) {
                    registerMsg.setText("Error inesperado");
                    return;
                }
				// chequea la respuesta de la api
				try {
					if (json.getString(KEY_SUCCESS) != null) {
                        registerMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
                            registerMsg.setText("Actualizacion exitosa");
                            //limpia la pantalla
                            clearScreen();
							// Cierra
							//finish();
						}else{
							// Error en la actualizacion
                            registerMsg.setText(json.getString(KEY_ERROR_MSG));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		// boton para volver
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}

    /**
     * Metodo para limpiar el formulario
     * @return  void
     * */
    private void clearScreen(){
        inputName.setText("");
        inputOldPassword.setText("");
        inputNewPassword1.setText("");
        inputNewPassword2.setText("");
    }
}
