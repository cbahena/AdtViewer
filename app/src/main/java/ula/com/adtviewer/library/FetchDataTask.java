package ula.com.adtviewer.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

/* Despliea el archivo 3D cargado
 * @author Carlos
 * @version 1
 */
public class FetchDataTask extends AsyncTask<String, Void, String>{
    private final FetchDataListener listener;
    private String msg;

    /**
     * Metodo constructor
     * @param  listener
     * */
    public FetchDataTask(FetchDataListener listener) {
        this.listener = listener;
    }

    /**
     * Metodo doInBackground
     * @param  params
     * @return  String
     * */
    @Override
    protected String doInBackground(String... params) {
        if(params == null) return null;

        String userid = params[0];

        UserFunctions userFunction = new UserFunctions();

        JSONArray json = userFunction.getDashboardByUserId(userid);

        // check for response
            if (json != null) {
                return json.toString();
            }

        return null;
    }
    /**
     * Metodo onPostExecute
     * @param  sJson
     * @return  void
     * */
    @Override
    protected void onPostExecute(String sJson) {
        if(sJson == null) {
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }        
        
        try {
            // convert json string to json array
            JSONArray aJson = new JSONArray(sJson);
            // create apps list
            List<PatientTest> apps = new ArrayList<PatientTest>();
            
            for(int i=0; i<aJson.length(); i++) {
                JSONObject json = aJson.getJSONObject(i);
                PatientTest app = new PatientTest();

                app.setIdExamen(json.getString("idExamen"));
                app.setCodigoPaciente(json.getString("codigoPaciente"));
                app.setFechaExamen(json.getString("fechaExamen"));
                app.setNombrePaciente(json.getString("nombrePaciente"));
                app.setTipoExamen(json.getString("tipoExamen"));
                app.setRutaExamen(json.getString("rutaExamen"));
                app.setTipoIcono(json.getString("tipoIcono"));
                // add the app to apps list
                apps.add(app);
            }
            
            //notify the activity that fetch data has been complete
            if(listener != null) listener.onFetchComplete(apps);
        } catch (JSONException e) {
            msg = "Respuesta invalida";
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }        
    }
}
