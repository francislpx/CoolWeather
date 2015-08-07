package com.lipingxin.app.coolweather.activity;

import com.lipingxin.app.coolweather.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年8月6日 下午3:24:50 
 * @description: 
*/
public class AboutUsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_us);
	}
	
}
