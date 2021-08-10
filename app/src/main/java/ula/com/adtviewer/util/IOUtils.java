package ula.com.adtviewer.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* Clase para procesar funciones de entrada/salida
 * @author carlos
 */
public class IOUtils {
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	/**
	 * Convierte  <code>input</code> a byte[].
	 * 
	 * @param input
	 * @return Array of Byte
	 * @throws java.io.IOException
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * Copia <code>length</code> del tamaño para <code>input</code> stream para <code>output</code> stream.
	 * Este metodo no cierra el buffer de entrada/salida
	 *
	 * @param input
	 * @param output
	 * @return long copied length
	 * @throws java.io.IOException
	 */
	private static long copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Copia el <code>length</code> tamaño de <code>input</code> stream para <code>output</code> stream.
	 *
	 * @param input
	 * @param output
	 * @return long copied length
	 * @throws java.io.IOException
	 */
	public static long copy(InputStream input, OutputStream output, int length) throws IOException {
		byte[] buffer = new byte[length];
		int count = 0;
		int n = 0;
		int max = length;
		while ((n = input.read(buffer, 0, max)) != -1) {
			output.write(buffer, 0, n);
			count += n;
			if (count > length) {
				break;
			}
			
			max -= n;
			if (max <= 0) {
				break;
			}
		}
		return count;
	}
	
	/**
	 * Cierra  <code>closeable</code>.
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		
		try {
			closeable.close();
		} catch (Throwable e) {
			// do nothing
		}		
	}



    /**
     * Metodo trimCache
     * @param context
     */
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /**
     * Metodo deleteDir
     * @param dir
     * @return boolean
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // El directorio esta vacio asi que se elimina
        return dir.delete();
    }
}
