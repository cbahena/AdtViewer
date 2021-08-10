package ula.com.adtviewer.activity;

import ula.com.adtviewer.R;
import ula.com.adtviewer.library.FetchDataListener;
import ula.com.adtviewer.library.FetchDataTask;
import ula.com.adtviewer.library.LocalContext;
import ula.com.adtviewer.library.PatientTest;
import ula.com.adtviewer.library.PatientTestAdapter;
import ula.com.adtviewer.library.ServerSetting;
import ula.com.adtviewer.library.UserFunctions;
import ula.com.adtviewer.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

/* Define la actividad para mostrar la bandeja de examenes pendientes del medico sobre los pacientes
 * @author Carlos
 * @version 1
 */
public class DashboardActivity extends ListActivity implements FetchDataListener {
    private ProgressDialog dialog;
    private String userid;
    private String user_name;

    //progress bar
    private ProgressBar pb;
    private Dialog dialogProgress;
    private int downloadedSize = 0;
    private int totalSize = 0;
    private TextView cur_val;

    private String file_path;
    private File file;

    /**
     * Metodo onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.dashboard);

        this.userid = getIntent().getStringExtra("userid");
        this.user_name = getIntent().getStringExtra("username");
        Button textButtonExit = (Button)findViewById(R.id.btnLogout);
        textButtonExit.setText(textButtonExit.getText()+ " (" + this.user_name + ")");
        initView();
    }
    /**
     * Metodo initView
     */
    private void initView() {
        // muestra el progreso
        dialog = ProgressDialog.show(this, "", "Cargando...");

        FetchDataTask task = new FetchDataTask(this);
        task.execute(this.userid);

    }

    /**
     * Metodo llamado al terminar de cargar
     * @param data
     */
    @Override
    public void onFetchComplete(List<PatientTest> data) {
        // cierra la ventana de cargando
        if(dialog != null)  dialog.dismiss();
        // crea un nuevo examen
        PatientTestAdapter adapter = new PatientTestAdapter(this, data);
        // agrega el examen a la lista
        setListAdapter(adapter);        
    }

    /**
     * Metodo llamado ocurre un error cargando la bandeja
     * @param msg
     */
    @Override
    public void onFetchFailure(String msg) {
        // cierra la ventana
        if(dialog != null)  dialog.dismiss();
        // Muestra el mensaje de error
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();        
    }

    /**
     * Metodo onListItemClick
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String route = ((TextView) v.findViewById(R.id.testRoute)).getText().toString();

        this.file_path = ServerSetting.download_file_path + route;
        showProgress(this.file_path);

        new Thread(new Runnable() {
            public void run() {
                downloadFile();
            }
        }).start();
    }

    /**
     * Metodo ButtonOnClick para salir
     * @param v
     */
    public void ButtonOnClick(View v) {

        closeAlert();
    }

    private void exitSystem(){

        UserFunctions userFunction = new UserFunctions();
        userFunction.logout(this.userid);

        onDestroy();
        finish();
        System.exit(0);
    }

    /**
     * Metodo downloadFile
     */
    void downloadFile(){

        try {

            downloadedSize = 0;
            totalSize = 0;

            //ruta donde se guardara el archivo temporal descargado
            File SDCardRoot = this.getCacheDir();//Environment.getExternalStorageDirectory();
            //crea un nuevo archivo
            file = new File(SDCardRoot,"temp" + System.currentTimeMillis() + ".stl");

            FileOutputStream fileOutput = new FileOutputStream(file);

            //lee la data desde la red
            DefaultHttpClient httpClient = LocalContext.getHttpClient();
            HttpContext localContext = LocalContext.getLocalContext();

            HttpGet httpGet = new HttpGet(this.file_path);
            HttpResponse execute = httpClient.execute(httpGet, localContext);

            InputStream inputStream =  execute.getEntity().getContent();

            //tamaño total de descarga
            totalSize = (int)(execute.getEntity().getContentLength());//urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //crea el bufer
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // actualiza la barra de descarga
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Descargado " + downloadedSize + "Bytes / " + totalSize + "Bytes (" + (int)per + "%)" );
                    }
                });
            }
            //cierra
            fileOutput.close();

            runOnUiThread(new Runnable() {
                public void run() {
                    //pb.dismiss(); // if you want close it..
                }
            });

            //inicia la actividad de despliege 3D
            startViewer();

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Por favor revisa la conexión de red " + e);
        }

    }
    /**
     * Metodo startViewer
     */
    void startViewer(){
        //close dialog progress bar
        dialogProgress.dismiss();
        //execute viewer
        Intent stlview = new Intent(getApplicationContext(), STLViewActivity.class);

        stlview.putExtra("STLFileName", Uri.fromFile(file));
        startActivity(stlview);
    }

    /**
     * Metodo showError
     * @param err
     */
    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(DashboardActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Metodo showProgress
     * @param file_path
     */
    void showProgress(String file_path){
        dialogProgress = new Dialog(DashboardActivity.this);
        dialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProgress.setContentView(R.layout.progress_dialog);
        dialogProgress.setTitle("Progreso de descarga");

        TextView text = (TextView) dialogProgress.findViewById(R.id.tv1);
        text.setText("Descargando archivo desde ... " + file_path);
        cur_val = (TextView) dialogProgress.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Comenzando descarga...");
        dialogProgress.show();

        pb = (ProgressBar)dialogProgress.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }
    /**
     * Metodo para mostrar la alerta de salir
     */
    private void closeAlert(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // titulo
        alertDialogBuilder.setTitle("ULA ADT Viewer");

        // Mensaje
        alertDialogBuilder
                .setMessage("¿Seguro desea salir?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        exitSystem();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // Crea el mensaje de alerta
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show
        alertDialog.show();
    }
    /**
     * Metodo onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            IOUtils.trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
