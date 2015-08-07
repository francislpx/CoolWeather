package com.lipingxin.app.coolweather.activity;

import com.lipingxin.app.coolweather.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年8月6日 下午3:29:18 
 * @description: 
*/
public class SettingsActivity extends Activity {

	private TextView mBackButton;
	private Switch mSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_layout);
		
		SharedPreferences preferences = getApplicationContext().
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		final SharedPreferences.Editor editor = preferences.edit();
		
		boolean autoUpdate = preferences.getBoolean("auto_update", true);
		//从SharedPreferences读取配置
		mSwitch = (Switch) findViewById(R.id.auto_update_setting);
		mSwitch.setChecked(autoUpdate);
		
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				editor.remove("auto_update");
				if(isChecked) {
					editor.putBoolean("auto_update", true);
				} else {
					editor.putBoolean("auto_update", false);
				}
				editor.commit();
			}
		});
		
		mBackButton = (TextView) findViewById(R.id.settings_back);
		mBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
