package ula.com.adtviewer.library;

import java.util.List;

/* Interfaz para lsa carga del archivo .stl
 * @author Carlos
 * @version 1
 */
public interface FetchDataListener {
    /**
     * Metodo onFetchComplete
     * @param  data
     * @return  void
     * */
    public void onFetchComplete(List<PatientTest> data);
    /**
     * Metodo onFetchFailure
     * @param  msg
     * @return  void
     * */
    public void onFetchFailure(String msg);
}
