package ula.com.adtviewer.util;

/* Clase para registro de bitacora del prototipo
 * @author kshoji
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
public class Log {
	private static boolean isDebug = false;

    public static final String TAG = "STLViewer";
    public static final String THROWABLE = "throwable occured.";

    /**
     * activa la depuracion
     * @param isDebug
     * @return void
     */
	public static void setDebug(boolean isDebug) {
		Log.isDebug = isDebug;
		Log.d("isDebug:" + isDebug);
	}

    /**
     * registra la excepcion
     * @param message
     * @return void
     */
	public static final void e(String message) {
		if (isDebug) {
			android.util.Log.e(TAG, message);
		}
	}
    /**
     * registra la excepcion
     * @param t
     * @return void
     */
	public static final void e(Throwable t) {
		if (isDebug) {
			e(THROWABLE, t);
		}
	}
    /**
     * registra la excepcion
     * @param message
     * @param t
     * @return void
     */
	public static final void e(String message, Throwable t) {
		if (isDebug) {
			android.util.Log.e(TAG, message, t);
		}
	}
    /**
     * registra la excepcion
     * @param message
     * @return void
     */
	public static final void w(String message) {
		if (isDebug) {
			android.util.Log.w(TAG, message);
		}
	}
    /**
     * registra la excepcion
     * @param t
     * @return void
     */
	public static final void w(Throwable t) {
		if (isDebug) {
			w(THROWABLE, t);
		}
	}
    /**
     * registra la excepcion
     * @param message
     * @param t
     * @return void
     */
	public static final void w(String message, Throwable t) {
		if (isDebug) {
			android.util.Log.w(TAG, message, t);
		}
	}

    /**
     * registra la excepcion
     * @param message
     * @return void
     */
	public static final void i(String message) {
		if (isDebug) {
			android.util.Log.i(TAG, message);
		}
	}
    /**
     * registra la excepcion
     * @param t
     * @return void
     */
	public static final void i(Throwable t) {
		if (isDebug) {
			i(THROWABLE, t);
		}
	}
    /**
     * registra la excepcion
     * @param message
     * @param t
     * @return void
     */
	public static final void i(String message, Throwable t) {
		if (isDebug) {
			android.util.Log.i(TAG, message, t);
		}
	}
    /**
     * registra la excepcion
     * @param message
     * @return void
     */
	public static final void d(String message) {
		android.util.Log.d(TAG, message);
	}
    /**
     * registra la excepcion
     * @param t
     * @return void
     */
	public static final void d(Throwable t) {
		d(THROWABLE, t);
	}
    /**
     * registra la excepcion
     * @param message
     * @param t
     * @return void
     */
	public static final void d(String message, Throwable t) {
		android.util.Log.d(TAG, message, t);
	}
    /**
     * registra la excepcion
     * @param message
     * @return void
     */
	public static final void v(String message) {
		android.util.Log.v(TAG, message);
	}
    /**
     * registra la excepcion
     * @param t
     * @return void
     */
	public static final void v(Throwable t) {
		v(THROWABLE, t);
	}
    /**
     * registra la excepcion
     * @param message
     * @param t
     * @return void
     */
	public static final void v(String message, Throwable t) {
		android.util.Log.v(TAG, message, t);
	}
}
