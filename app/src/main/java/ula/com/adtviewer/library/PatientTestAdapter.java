package ula.com.adtviewer.library;

import ula.com.adtviewer.R;
import ula.com.adtviewer.library.PatientTest;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* Define el detalle de cada examen en la bandeja de entrada
 * @author Carlos
 * @version 1
 */
public class PatientTestAdapter extends ArrayAdapter<PatientTest>{
    private List<PatientTest> items;

    /**
     * Metodo constructor
     * @param context
     * @param items
     * */
    public PatientTestAdapter(Context context, List<PatientTest> items) {
        super(context, R.layout.app_custom_list, items);
        this.items = items;
    }

    /**
     * Obtiene la cantidad de items
     * @return cantidad
     * */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * Metodo para retornar detalle de cada item
     * @param position
     * @param convertView
     * @param parent
     * @return  View
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        
        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.app_custom_list, null);            
        }

        PatientTest app = items.get(position);
        
        if(app != null) {
            ImageView icon = (ImageView)v.findViewById(R.id.appIcon);
            TextView titleText = (TextView)v.findViewById(R.id.titleText);
            TextView codeText = (TextView)v.findViewById(R.id.codeText);
            TextView dateText = (TextView)v.findViewById(R.id.dateText);
            TextView typeText = (TextView)v.findViewById(R.id.typeText);
            TextView testId = (TextView)v.findViewById(R.id.testId);
            TextView testRoute = (TextView)v.findViewById(R.id.testRoute);

            if(icon != null) {
                Resources res = getContext().getResources();
                String sIcon = "ula.com.adtviewer:drawable/" + app.getTipoIcono();
                icon.setImageDrawable(res.getDrawable(res.getIdentifier(sIcon, null, null)));
            }
            
            if(titleText != null) titleText.setText(app.getNombrePaciente());
            if(codeText != null) codeText.setText(app.getCodigoPaciente());
            if(dateText != null) dateText.setText(app.getFechaExamen());
            if(typeText != null) typeText.setText(app.getTipoExamen());
            if(testId != null) testId.setText(app.getIdExamen());
            if(testRoute != null) testRoute.setText(app.getRutaExamen());
        }
        
        return v;
    }
}
