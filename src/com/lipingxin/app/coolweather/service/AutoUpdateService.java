package com.lipingxin.app.coolweather.service;

import com.lipingxin.app.coolweather.receiver.AutoUpdateReceiver;
import com.lipingxin.app.coolweather.util.HttpCallbackListener;
import com.lipingxin.app.coolweather.util.HttpUtil;
import com.lipingxin.app.coolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年8月4日 上午10:35:50 
 * @description: 
*/
public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SharedPreferences preferences = getApplicationContext().
				getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		boolean autoUpdate = preferences.getBoolean("auto_update", true);
		//如果开启了自动更新
		if(autoUpdate) {
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					updateWeather();
				}
			}).start();
			AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			int anHour = 8 * 60 * 60 * 1000;
			long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
			Intent i = new Intent(this, AutoUpdateReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
			mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	protected void updateWeather() {
		SharedPreferences preferences = PreferenceManager.
				getDefaultSharedPreferences(this);
		
		String weatherCode = preferences.getString("weather_code", "");
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}
}
