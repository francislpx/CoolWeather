package com.lipingxin.app.coolweather.activity;

import com.lipingxin.app.coolweather.R;
import com.lipingxin.app.coolweather.service.AutoUpdateService;
import com.lipingxin.app.coolweather.util.HttpCallbackListener;
import com.lipingxin.app.coolweather.util.HttpUtil;
import com.lipingxin.app.coolweather.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年8月3日 下午4:11:29 
 * @description: 
*/
public class WeatherActivity extends Activity {

	private LinearLayout mWeatherInfoLayout;
	
	private TextView mCityNameText;
	private TextView mPublishTimeText;
	private TextView mWeatherDespText;
	private TextView mTemp1Text;
	private TextView mTemp2Text;
	private TextView mCurrentDateText;
	
	private Button mSwitchButton;
	private Button mRefreshButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		mWeatherInfoLayout = (LinearLayout) findViewById(R.id.wather_info_layout);
		mCityNameText = (TextView) findViewById(R.id.city_name);
		mPublishTimeText = (TextView) findViewById(R.id.publish_text);
		mWeatherDespText = (TextView) findViewById(R.id.weather_desp);
		mTemp1Text = (TextView) findViewById(R.id.temp1);
		mTemp2Text = (TextView) findViewById(R.id.temp2);
		mCurrentDateText = (TextView) findViewById(R.id.current_date);
		
		String districtCode = getIntent().getStringExtra("district_code");
		if(!TextUtils.isEmpty(districtCode)) {
			
			mPublishTimeText.setText("同步中···");
			mWeatherInfoLayout.setVisibility(View.INVISIBLE);
			mCityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(districtCode);
		} else {
			showWeather();
		}
		
		mSwitchButton = (Button) findViewById(R.id.switch_city_button);
		mRefreshButton = (Button) findViewById(R.id.refresh_weather_button);
		
		mSwitchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
				intent.putExtra("from_weather_activity", true);
				startActivity(intent);
				finish();
			}
		});
		
		mRefreshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPublishTimeText.setText("同步中···");
				SharedPreferences preferences = PreferenceManager.
						getDefaultSharedPreferences(WeatherActivity.this);
				
				String weatherCode = preferences.getString("weather_code", "");
				//Log.e("weatherCode", "weatherCode:" + weatherCode);
				if(!TextUtils.isEmpty(weatherCode)) {
					queryWeatherInfo(weatherCode);
				}
			}
		});
		
		// 广告条接口调用（适用于应用）
		//将广告条adView添加到需要展示的layout控件中
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		adLayout.addView(adView);
	
	}

	private void queryWeatherCode(String districtCode) {
		//System.out.println("******查询参数districtCode：" + districtCode);
		String address = "http://www.weather.com.cn/data/list3/city" + districtCode + ".xml";
		queryFromServer(address, "districtCode");
	}

	private void queryWeatherInfo(String weatherCode) {
		//System.out.println("******查询参数weatherCode：" + weatherCode);
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	
	private void queryFromServer(final String address, final String type) {
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("districtCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|"); 
						if(array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
					
				} else if("weatherCode".equals(type)) {
					
					//System.out.println("******查询结果：" + response);
					
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mPublishTimeText.setText("同步失败！");
					}
				});
			}
		});
	}
	
	private void showWeather() {
		
		SharedPreferences preferences = PreferenceManager.
				getDefaultSharedPreferences(this);
		
		mCityNameText.setText(preferences.getString("city_name", ""));
		mTemp1Text.setText(preferences.getString("temp1", ""));
		mTemp2Text.setText(preferences.getString("temp2", ""));
		mWeatherDespText.setText(preferences.getString("weather_desp", ""));
		mPublishTimeText.setText("今天" + preferences.getString("publish_time", "") + "发布");
		mCurrentDateText.setText(preferences.getString("current_date", ""));
		
		mWeatherInfoLayout.setVisibility(View.VISIBLE);
		mCityNameText.setVisibility(View.VISIBLE);
		
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		Intent intent;
		
		switch (id) {
		case R.id.about_us:
			intent = new Intent(WeatherActivity.this, AboutUsActivity.class);
			startActivity(intent);
			break;
		
		case R.id.action_settings:
			intent = new Intent(WeatherActivity.this, SettingsActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
