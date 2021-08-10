package ula.com.adtviewer.library;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * Clase para mantener el hilo de conexion
 * @author  carlos
 */
public class LocalContext {

    private static DefaultHttpClient httpclient;
    private static HttpContext  localContext;

    /**
     * Metodo para retornar el cliente http
     */
    public static DefaultHttpClient getHttpClient()
    {
        if (httpclient == null)
        {
            httpclient = new DefaultHttpClient();
        }
        return httpclient;
    }

    /**
     * Metodo para retornar el contexto http
     */
    public static HttpContext getLocalContext()
    {
        if (localContext == null)
        {
            // Crea una instancia local
            CookieStore cookieStore = new BasicCookieStore();
            // Crea el contexto HTTP local
            localContext = new BasicHttpContext();
            // setea las cookies
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
        return localContext;
    }
}
