package com.lipingxin.app.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import com.lipingxin.app.coolweather.db.CoolWeatherDB;
import com.lipingxin.app.coolweather.model.City;
import com.lipingxin.app.coolweather.model.District;
import com.lipingxin.app.coolweather.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午3:48:19 
 * @description: 
*/
public class Utility {

	//处理返回的省份数据
	public synchronized static boolean handleProvinceResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		
		try {
			if(!TextUtils.isEmpty(response)) {
				String[] allProvinces = response.split(",");
				if(allProvinces != null && allProvinces.length > 0) {
					for(String p : allProvinces) {
						String[] array = p.split("\\|");
						Province province = new Province();
						province.setCode(array[0]);
						province.setName(array[1]);
						coolWeatherDB.saveProvince(province);
					}
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("*********存储——省份——失败!");
			e.printStackTrace();
		}
		return false;
	}
	
	//处理返回的市数据
	public static boolean handleCityResponse(
			CoolWeatherDB coolWeatherDB, String response, int parentId) {
		
		try {
			if(!TextUtils.isEmpty(response)) {
				String[] allCities = response.split(",");
				if(allCities != null && allCities.length > 0) {
					for(String p : allCities) {
						String[] array = p.split("\\|");
						City city = new City();
						city.setCode(array[0]);
						city.setName(array[1]);
						city.setParentId(parentId);
						coolWeatherDB.saveCity(city);
					}
					return true;
				}
			}
		} catch (Exception e) {
			//System.out.println("*********存储——市——失败!");
			e.printStackTrace();
		}
		return false;
	}
	
	//处理返回的区/县份数据
	public static boolean handleDistrictResponse(
			CoolWeatherDB coolWeatherDB, String response, int parentId) {
		
		try {
			if(!TextUtils.isEmpty(response)) {
				String[] allProvinces = response.split(",");
				if(allProvinces != null && allProvinces.length > 0) {
					for(String p : allProvinces) {
						String[] array = p.split("\\|");
						District district = new District();
						district.setCode(array[0]);
						district.setName(array[1]);
						district.setParentId(parentId);
						coolWeatherDB.saveDistrict(district);
					}
					return true;
				}
			}
		} catch (Exception e) {
			//System.out.println("*********存储——区县——失败!");
			e.printStackTrace();
		}
		return false;
	}
	
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject json = new JSONObject(response);
			JSONObject weatherInfo = json.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, 
					weatherDesp, publishTime);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
			String temp2, String weatherDesp, String publishTime) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.
				getDefaultSharedPreferences(context).edit();
		
		editor.putBoolean("city_selected", true);
		
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", format.format(new Date()));

		editor.commit();
	}
	
}
