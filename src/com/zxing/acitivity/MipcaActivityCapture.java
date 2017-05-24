package com.zxing.acitivity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jmz.HomeActivity;
import com.jmz.PostOutActivity;
import com.jmz.ProductActivity;
import com.jmz.R;
import com.jmz.StartActivity;
import com.jmz.TabBaseAcitivity;
import com.jmz.WebViewAcitivity;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private TextView title;
	private String id = "id=";
	private String rid = "&rid=";
	private String pf = "&pf=";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		title = (TextView) findViewById(R.id.scan_title);
		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MipcaActivityCapture.this.finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		Logger.i("zwt", "--->result:" + resultString);
		if (resultString != null && resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else if (resultString != null && resultString.contains("http")) {
			if (resultString.contains("/product/info")
					&& resultString.contains(id)) {
				// http://m.jmzgo.com/product/info.aspx?id=202&rid=378&pf=android
				Intent intent = new Intent(MipcaActivityCapture.this,
						ProductActivity.class);
				intent.putExtra(Config.TAG_PUODUCTID,
						resultString.substring(resultString.indexOf(id) + 3,
								resultString.indexOf(rid)));
				if (resultString.contains(pf)) {
					intent.putExtra(Config.TAG_REFERRERUSERID, resultString
							.substring(resultString.indexOf(rid) + 5,
									resultString.indexOf(pf)));
				} else if (resultString.contains("#")) {
					intent.putExtra(Config.TAG_REFERRERUSERID, resultString
							.substring(resultString.indexOf(rid) + 5,
									resultString.indexOf("#")));
				}
				startActivity(intent);
				
			}else if (resultString.contains("http://weixin.qq.com")) {
				//此方法在微信5.0后已失效
				/*Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setPackage("com.tencent.mm");//直接打开微信
				intent.putExtra(Intent.EXTRA_SUBJECT, "share");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// 这是你公共帐号的二维码的实际内容。可以用扫描软件扫一下就得到了。这是我的公共帐号地址。这一句的位置挺关键                
				intent.setData(Uri.parse(resultString));
				startActivity(intent);*/
				Toast.makeText(MipcaActivityCapture.this,
						"关注微信公众号请打开微信使用扫一扫功能进行关注",
						Toast.LENGTH_LONG).show();
			}else {
				Intent intent = new Intent(MipcaActivityCapture.this,
						WebViewAcitivity.class);
				intent.putExtra(PostOutActivity.URLSTRING, resultString);
				startActivity(intent);
			}
		} else {
			Toast.makeText(MipcaActivityCapture.this,
					"二维码内容:" + resultString + "  金拇指购无法识别此内容",
					Toast.LENGTH_LONG).show();
		}
		/*
		 * else if(resultString.startsWith("ProductID=")){ Intent intent = new
		 * Intent(MipcaActivityCapture.this, ProductActivity.class);
		 * intent.putExtra(Config.TAG_PUODUCTID,
		 * resultString.replace("ProductID=", "")); startActivity(intent);
		 * }else{ Toast.makeText(MipcaActivityCapture.this,
		 * getString(R.string.product_exist), 1000).show(); }
		 */
		this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}