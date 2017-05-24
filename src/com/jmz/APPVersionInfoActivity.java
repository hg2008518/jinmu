package com.jmz;

import com.jmz.uitl.Logger;
import com.jmz.uitl.ReferCallBack;
import com.jmz.uitl.RefreUtile;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class APPVersionInfoActivity extends ParentActivity{
	
	private TextView currentVersion;
	private ImageView newVersionImg;
	private RefreUtile refreUtile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTittleText(R.string.app_version_title);
		
		initcenterView(LayoutInflater.from(APPVersionInfoActivity.this).inflate(
				R.layout.activity_app_version_info, null));
		
		currentVersion = (TextView) findViewById(R.id.current_version);
		newVersionImg = (ImageView) findViewById(R.id.new_version_img);
		
		try {
			currentVersion.setText(getString(R.string.app_version_msg) + this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			currentVersion.setText(getString(R.string.app_version_msg) + "1.0.3");
		}
		
		refreUtile = new RefreUtile(this, true);
		refreUtile.refreSubmit();
		refreUtile.setCallBack(new ReferCallBack() {
			@Override
			public void callBack(boolean value) {
				// TODO Auto-generated method stub
				Logger.i("-->APPVersionInfoActivity", "callback:" + value);
				if (value) {
					newVersionImg.setVisibility(View.VISIBLE);
				}else {
					newVersionImg.setVisibility(View.INVISIBLE);
				}
			}
		});
		
	}

}
