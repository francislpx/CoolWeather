package com.lipingxin.app.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.lipingxin.app.coolweather.model.City;
import com.lipingxin.app.coolweather.model.District;
import com.lipingxin.app.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午2:30:07 
 * @description: 
*/
public class CoolWeatherDB {

	//数据库名称
	public static final String DB_NAME = "coolweather";	
	//数据库版本
	public static final int VERSION = 1;	
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	public CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, 
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	//单例
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if(coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	//Province存到数据库 
	public void saveProvince(Province province) {
		if(province != null) {
			ContentValues values = new ContentValues();
			values.put("name", province.getName());
			values.put("code", province.getCode());
			db.insert("province", null, values);
		}
	}
	//City存到数据库 
	public void saveCity(City city) {
		if(city != null) {
			ContentValues values = new ContentValues();
			values.put("name", city.getName());
			values.put("code", city.getCode());
			values.put("parentId", city.getParentId());
			db.insert("city", null, values);
		}
	}
	//District存到数据库 
	public void saveDistrict(District district) {
		if(district != null) {
			ContentValues values = new ContentValues();
			values.put("name", district.getName());
			values.put("code", district.getCode());
			values.put("parentId", district.getParentId());
			db.insert("district", null, values);
		}
	}
	
	//读取Province
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("province", null, null, null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setName(cursor.getString(cursor.getColumnIndex("name")));
				province.setCode(cursor.getString(cursor.getColumnIndex("code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if(cursor != null) {
			cursor.close();
		}
		return list;
	}
		
	//读取Province
	public List<City> loadCity() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("city", null, null, null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setName(cursor.getString(cursor.getColumnIndex("name")));
				city.setCode(cursor.getString(cursor.getColumnIndex("code")));
				city.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		if(cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	//读取Province
	public List<District> loadDistrict() {
		List<District> list = new ArrayList<District>();
		Cursor cursor = db.query("district", null, null, null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				District district = new District();
				district.setId(cursor.getInt(cursor.getColumnIndex("id")));
				district.setName(cursor.getString(cursor.getColumnIndex("name")));
				district.setCode(cursor.getString(cursor.getColumnIndex("code")));
				district.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
				list.add(district);
			} while (cursor.moveToNext());
		}
		if(cursor != null) {
			cursor.close();
		}
		return list;
	}
	
}
