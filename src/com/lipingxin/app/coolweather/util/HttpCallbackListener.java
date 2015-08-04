package com.lipingxin.app.coolweather.util;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午3:14:02 
 * @description: 
*/
public interface HttpCallbackListener {

	void onFinish(String response);
	void onError(Exception e);
}
