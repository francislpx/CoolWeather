package com.lipingxin.app.coolweather.receiver;

import com.lipingxin.app.coolweather.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年8月4日 上午10:40:35 
 * @description: 
*/
public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}

}
