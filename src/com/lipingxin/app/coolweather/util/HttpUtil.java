package com.lipingxin.app.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午3:09:21 
 * @description: 
*/
public class HttpUtil {

	/*public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					
					connection.setConnectTimeout(20000);
					connection.setReadTimeout(20000);
					
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if(listener != null) {
						listener.onFinish(response.toString());
					}
					
					reader.close();
					in.close();
					
				} catch (Exception e) {
					e.printStackTrace();
					if(listener != null) {
						listener.onError(e);
					}
				} finally {
					if(connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}*/

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					HttpClient httpClient = new DefaultHttpClient();  
					//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);//连接时间
					//httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,10000);//数据传输时间
					
					HttpGet getMethod = new HttpGet(address);
					HttpResponse response = httpClient.execute(getMethod);
					HttpEntity entity = response.getEntity();
					
					StringBuilder result = new StringBuilder();
					
					if (entity != null) {
	                    
						BufferedReader br = new BufferedReader(
	                    		new InputStreamReader(entity.getContent()));
						String line;
						while ((line = br.readLine()) != null) {
							result.append(line);
						}
						//System.out.println("**请求结果：" + result);
						br.close();
					}
					
					if(listener != null) {
						listener.onFinish(result.toString());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					if(listener != null) {
						listener.onError(e);
					}
				}
			}
		}).start();
	}
	
}
