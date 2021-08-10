
package ula.com.adtviewer.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

/* Metodos que se comunican con la api
 * @author Carlos
 * @version 1
 */
public class UserFunctions {
	
	private JSONParser jsonParser;

    /**
     * Metodo Constructor
     * */
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * Metodo para chequear las credenciales
	 * @param email
	 * @param password
     * @return JSONObject respuesta del inicio de sesion
	 * */
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", ServerSetting.login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(ServerSetting.api_url, params);
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * Metodo para modificar la contraseña
	 * @param user
	 * @param old_password
	 * @param new_password
     * @return  JSONObject respuesta en formato JSON
	 * */
	public JSONObject changePassword(String user, String old_password, String new_password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", ServerSetting.change_password_tag));
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("old_password", old_password));
		params.add(new BasicNameValuePair("new_password", new_password));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(ServerSetting.api_url, params);
		// return json
		return json;
	}

    /**
     * Metodo para solicitar los examenes pendientes del medico
     * @param userid
     * @return JSONArray Vector de examenes
     * */
    public JSONArray getDashboardByUserId(String userid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", ServerSetting.dashboard_tag));
        params.add(new BasicNameValuePair("userid", userid));
        JSONArray json = jsonParser.getJSONArrayFromUrl(ServerSetting.api_url, params);
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * Metodo para cerrar sesion php
     * @param userid
     * */
    public void logout(String userid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", ServerSetting.logout_tag));
        params.add(new BasicNameValuePair("userid", userid));

        jsonParser.callTagFromUrl(ServerSetting.api_url, params);
    }
}
