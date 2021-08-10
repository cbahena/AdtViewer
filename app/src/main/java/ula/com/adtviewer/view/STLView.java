package ula.com.adtviewer.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.widget.Toast;

import ula.com.adtviewer.R;
import ula.com.adtviewer.object.STLObject;
import ula.com.adtviewer.renderer.STLRenderer;
import ula.com.adtviewer.util.IOUtils;
import ula.com.adtviewer.util.Log;

import java.io.IOException;
import java.io.InputStream;

/* Clase la visualización del modelo 3D
 * @author carlos
 */
public class STLView extends GLSurfaceView {

	private STLRenderer stlRenderer;
	private Uri uri;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    private boolean isRotate = true;

    // zoom rate (larger > 1.0f > smaller)
    private float pinchScale = 2.0f;

    private PointF pinchStartPoint = new PointF();

    private float pinchStartZ = 0.0f;
    private float pinchStartDistance = 0.0f;
    private float pinchMoveX = 0.0f;
    private float pinchMoveY = 0.0f;

    //  para el evento de click
    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_DRAG = 1;
    private static final int TOUCH_ZOOM = 2;
    private int touchMode = TOUCH_NONE;

    /**
     * Constructor
     * @param context
     * @param uri
     */
	public STLView(Context context, Uri uri) {
		super(context);

		this.uri = uri;

		byte[] stlBytes = null;
		try {
			stlBytes = getSTLBytes(context, uri);
		} catch (Exception e) {
		}

		if (stlBytes == null) {
			Toast.makeText(context, context.getString(R.string.error_fetch_data), Toast.LENGTH_LONG).show();
			return;
		}

		// Carga del modelo
		STLObject stlObject = new STLObject(stlBytes, context);

		SharedPreferences colorConfig = context.getSharedPreferences("colors", Activity.MODE_PRIVATE);
		STLRenderer.red = colorConfig.getFloat("red", 0.75f);
		STLRenderer.green = colorConfig.getFloat("green", 0.75f);
		STLRenderer.blue = colorConfig.getFloat("blue", 0.75f);
		STLRenderer.alpha = colorConfig.getFloat("alpha", 0.5f);

		// configura el renderisado
		setRenderer(stlRenderer = new STLRenderer(stlObject));
		STLRenderer.requestRedraw();
	}

	/**
     * Obtiene el modelo 3d
	 * @param context
     * @param uri
	 * @return byte[]
	 */
	private byte[] getSTLBytes(Context context, Uri uri) {
		byte[] stlBytes = null;
		InputStream inputStream = null;
		try {
			inputStream = context.getContentResolver().openInputStream(uri);
			stlBytes = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return stlBytes;
	}
    /**
     * Metodo para modificar la distancia del modelo
     * @param distance
     * @return void
     */
	private void changeDistance(float distance) {
		Log.i("distance:" + distance);
		stlRenderer.distanceZ = distance;
		STLRenderer.requestRedraw();
		requestRender();
	}
    /**
     * Metodo para rotar
     * @return boolean
     */
	public boolean isRotate() {
		return isRotate;
	}

    /**
     * Metodo para activar la rotacion
     * @param isRotate
     * @return void
     */
	public void setRotate(boolean isRotate) {
		this.isRotate = isRotate;
	}

    /**
     * Evento touch
     * @param event
     * @return boolean
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// starts pinch
			case MotionEvent.ACTION_POINTER_DOWN:
				if (event.getPointerCount() >= 2) {
					pinchStartDistance = getPinchDistance(event);
					pinchStartZ = stlRenderer.distanceZ;
					if (pinchStartDistance > 50f) {
						getPinchCenterPoint(event, pinchStartPoint);
						previousX = pinchStartPoint.x;
						previousY = pinchStartPoint.y;
						touchMode = TOUCH_ZOOM;
					}
				}
				break;
			
			case MotionEvent.ACTION_MOVE:
				if (touchMode == TOUCH_ZOOM && pinchStartDistance > 0) {
					// on pinch
					PointF pt = new PointF();
					
					getPinchCenterPoint(event, pt);
					pinchMoveX = pt.x - previousX;
					pinchMoveY = pt.y - previousY;
					float dx = pinchMoveX;
					float dy = pinchMoveY;
					previousX = pt.x;
					previousY = pt.y;
					
					if (isRotate) {
						stlRenderer.angleX += dx * TOUCH_SCALE_FACTOR;
						stlRenderer.angleY += dy * TOUCH_SCALE_FACTOR;
					} else {
						// change view point
						stlRenderer.positionX += dx * TOUCH_SCALE_FACTOR / 5;
						stlRenderer.positionY += dy * TOUCH_SCALE_FACTOR / 5;
					}
					STLRenderer.requestRedraw();
					
					pinchScale = getPinchDistance(event) / pinchStartDistance;
					changeDistance(pinchStartZ / pinchScale);
					invalidate();
				}
				break;
			
			// final pinch
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (touchMode == TOUCH_ZOOM) {
					touchMode = TOUCH_NONE;
					
					pinchMoveX = 0.0f;
					pinchMoveY = 0.0f;
					pinchScale = 1.0f;
					pinchStartPoint.x = 0.0f;
					pinchStartPoint.y = 0.0f;
					invalidate();
				}
				break;
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// comenzar arrastra
			case MotionEvent.ACTION_DOWN:
				if (touchMode == TOUCH_NONE && event.getPointerCount() == 1) {
					touchMode = TOUCH_DRAG;
					previousX = event.getX();
					previousY = event.getY();
				}
				break;
			
			case MotionEvent.ACTION_MOVE:
				if (touchMode == TOUCH_DRAG) {
					float x = event.getX();
					float y = event.getY();
					
					float dx = x - previousX;
					float dy = y - previousY;
					previousX = x;
					previousY = y;
					
					if (isRotate) {
						stlRenderer.angleX += dx * TOUCH_SCALE_FACTOR;
						stlRenderer.angleY += dy * TOUCH_SCALE_FACTOR;
					} else {
						// change view point
						stlRenderer.positionX += dx * TOUCH_SCALE_FACTOR / 5;
						stlRenderer.positionY += dy * TOUCH_SCALE_FACTOR / 5;
					}
					STLRenderer.requestRedraw();
					requestRender();
				}
				break;
			
			// end drag
			case MotionEvent.ACTION_UP:
				if (touchMode == TOUCH_DRAG) {
					touchMode = TOUCH_NONE;
					break;
				}
		}

		return true;
	}

	/**
	 * Obtiene la distancia del touch
	 * @param event
	 * @return Distancia
	 */
	private float getPinchDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return android.util.FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * Otiene el centro de gravedad
	 * @param event
	 * @param pt punto tocado
	 */
	private void getPinchCenterPoint(MotionEvent event, PointF pt) {
		pt.x = (event.getX(0) + event.getX(1)) * 0.5f;
		pt.y = (event.getY(0) + event.getY(1)) * 0.5f;
	}

    /**
     * Otiene el uri
     * @return  Uri
     */
	public Uri getUri() {
		return uri;
	}

}
