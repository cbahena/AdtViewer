package ula.com.adtviewer.library;

/* Parametros de la bandeja de examenes de los pacientes para el medico
 * @author Carlos
 * @version 1
 */
public class PatientTest {
    private String idExamen;
    private String codigoPaciente;
    private String nombrePaciente;
    private String tipoExamen;
    private String fechaExamen;
    private String rutaExamen;
    private String tipoIcono;

    /**
     * Obiente el codigo del examen
     * @return  codigo del examen
     * */
    public String getIdExamen() { return idExamen; }

    /**
     * Modifica el codigo del examen
     * @param  idExamen
     * */
    public void setIdExamen(String idExamen) { this.idExamen = idExamen; }
    /**
     * Obiente el codigo del paciente
     * @return  codigo del pacinete
     * */
    public String getCodigoPaciente() {
        return codigoPaciente;
    }
    /**
     * Modifica el codigo del paciente
     * @param  codigoPaciente
     * */
    public void setCodigoPaciente(String codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }
    /**
     * Obiente el nombre del paciente
     * @return  nombre del paciente
     * */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /**
     * Modifica el nombre del paciente
     * @param  nombrePaciente
     * */
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }
    /**
     * Obiente el tipo de examen
     * @return  tipo de examen
     * */
    public String getTipoExamen() {
        return tipoExamen;
    }

    /**
     * Modifica el tipo de examene
     * @param  tipoExamen
     * */
    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }

    /**
     * Obiente la fecha del examen
     * @return fecha del examen
     * */
    public String getFechaExamen() {
        return fechaExamen;
    }

    /**
     * Modifica la fecha del examen
     * @param  fechaExamen
     * */
    public void setFechaExamen(String fechaExamen) {
        this.fechaExamen = fechaExamen;
    }

    /**
     * Obtiente la ruta en el servidor del modelo 3D
     * @return  ruta en el servidor del modelo 3D
     * */
    public String getRutaExamen() { return rutaExamen; }

    /**
     * Modifica la ruta en el servidor del modelo 3D
     * @param  rutaExamen
     * */
    public void setRutaExamen(String rutaExamen) {
        this.rutaExamen = rutaExamen;
    }
    /**
     * Obtiente el tipo de icono
     * @return tipo de icono
     * */
    public String getTipoIcono() { return tipoIcono; }

    /**
     * Modifica el icono
     * @param  tipoIcono
     * */
    public void setTipoIcono(String tipoIcono) {
        this.tipoIcono = tipoIcono;
    }
}
