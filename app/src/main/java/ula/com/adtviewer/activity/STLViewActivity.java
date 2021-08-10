package ula.com.adtviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import ula.com.adtviewer.R;
import ula.com.adtviewer.renderer.STLRenderer;
import ula.com.adtviewer.util.Log;
import ula.com.adtviewer.view.STLView;

import java.io.File;

/**
 * Define la actividad para cargar el archivo stl desde un archivo local del dispositivo
 * @author Carlos
 * @version 1
 */
public class STLViewActivity extends Activity implements FileListDialog.OnFileListDialogListener {
	private STLView stlView;

    /**
     * Metodo onCreate
     * @param  savedInstanceState
     * @return  void
     * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PackageManager manager = getPackageManager();
		ApplicationInfo appInfo = null;
		try {
			appInfo = manager.getApplicationInfo(getPackageName(), 0);
			Log.setDebug((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE);
		} catch (NameNotFoundException e) {
			Log.d(e);
		}

        Parcelable stlFileName = getIntent().getParcelableExtra("STLFileName");
        if (stlFileName != null) {
            setUpViews((Uri) stlFileName);
        }
	}
    /**
     * Metodo onResume
     * @return  void
     * */
	@Override
	protected void onResume() {
		super.onResume();
		if (stlView != null) {
			Log.i("onResume");
			STLRenderer.requestRedraw();
			stlView.onResume();
		}
	}
    /**
     * Metodo onPause
     * @return  void
     * */
	@Override
	protected void onPause() {
		super.onPause();
		if (stlView != null) {
			Log.i("onPause");
			stlView.onPause();
		}
	}
    /**
     * Metodo onRestoreInstanceState
     * @param savedInstanceState
     * @return  void
     * */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.i("onRestoreInstanceState");
		Parcelable stlFileName = savedInstanceState.getParcelable("STLFileName");
		if (stlFileName != null) {
			setUpViews((Uri) stlFileName);
		}
		boolean isRotate = savedInstanceState.getBoolean("isRotate");
		ToggleButton toggleButton = (ToggleButton) findViewById(R.id.rotateOrMoveToggleButton);
		toggleButton.setChecked(isRotate);
	}

    /**
     * Metodo onSaveInstanceState
     * @param outState
     * @return  void
     * */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (stlView != null) {
			Log.i("onSaveInstanceState");
			outState.putParcelable("STLFileName", stlView.getUri());
			outState.putBoolean("isRotate", stlView.isRotate());
		}
	}

    /**
     * Metodo onClickFileList
     * @param file
     * @return  void
     * */
	@Override
	public void onClickFileList(File file) {
		if (file == null) {
			return;
		}

		SharedPreferences config = getSharedPreferences("PathSetting", Activity.MODE_PRIVATE);
		SharedPreferences.Editor configEditor = config.edit();
		configEditor.putString("lastPath", file.getParent());
		configEditor.commit();

		setUpViews(Uri.fromFile(file));
	}
    /**
     * Metodo setUpViews
     * @param uri
     * @return  void
     * */
	private void setUpViews(Uri uri) {
		setContentView(R.layout.stl);
		final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.rotateOrMoveToggleButton);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (stlView != null) {
					stlView.setRotate(isChecked);
				}
			}
		});

		final ImageButton loadButton = (ImageButton) findViewById(R.id.loadButton);
		loadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileListDialog fileListDialog = new FileListDialog(STLViewActivity.this, false, "Elige un archivo STL...", ".stl");
				fileListDialog.setOnFileListDialogListener(STLViewActivity.this);

				SharedPreferences config = getSharedPreferences("PathSetting", Activity.MODE_PRIVATE);
				fileListDialog.show(config.getString("lastPath", "/mnt/sdcard/"));
			}
		});

		final ImageButton preferencesButton = (ImageButton) findViewById(R.id.preferncesButton);
		preferencesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(STLViewActivity.this, PreferencesActivity.class);
				startActivity(intent);
			}
		});
		
		if (uri != null) {
			setTitle(uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1));
			
			FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.stlFrameLayout);
			stlView = new STLView(this, uri);
			relativeLayout.addView(stlView);
			
			toggleButton.setVisibility(View.VISIBLE);

			stlView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (preferencesButton.getVisibility() == View.INVISIBLE) {
						;
					}
				}
			});
		}
	}
}
