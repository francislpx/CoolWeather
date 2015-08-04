package com.lipingxin.app.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午2:10:08 
 * @description: 
*/
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	public static final String CREATE_PROVINCE_SQL = "create table province ("
			+ "id integer primary key autoincrement,"
			+ "name text,"
			+ "code text)";
	
	public static final String CREATE_CITY_SQL = "create table city ("
			+ "id integer primary key autoincrement,"
			+ "name text,"
			+ "code text,"
			+ "parent_id integer)";
	
	public static final String CREATE_DISTRICT_SQL = "create table district ("
			+ "id integer primary key autoincrement,"
			+ "name text,"
			+ "code text,"
			+ "parent_id integer)";
	
	public CoolWeatherOpenHelper(Context context, String name, 
			CursorFactory factory, int version) {
		
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE_SQL);
		db.execSQL(CREATE_CITY_SQL);
		db.execSQL(CREATE_DISTRICT_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
