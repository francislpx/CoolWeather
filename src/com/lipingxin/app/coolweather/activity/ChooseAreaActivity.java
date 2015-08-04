package com.lipingxin.app.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.lipingxin.app.coolweather.R;
import com.lipingxin.app.coolweather.db.CoolWeatherDB;
import com.lipingxin.app.coolweather.model.City;
import com.lipingxin.app.coolweather.model.District;
import com.lipingxin.app.coolweather.model.Province;
import com.lipingxin.app.coolweather.util.HttpCallbackListener;
import com.lipingxin.app.coolweather.util.HttpUtil;
import com.lipingxin.app.coolweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午4:16:22 
 * @description: 
*/
public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_DISTRICT = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<District> districtList;
	
	private Province selectedProvince;
	private City selectedCity;
	
	private int currentLevel;
	private boolean isFromWeatherActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences preferences = PreferenceManager.
				getDefaultSharedPreferences(this);
		
		if(preferences.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		
		adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, dataList);
	
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if(currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryDistricts();
				} else if(currentLevel == LEVEL_DISTRICT) {
					String districtCode = districtList.get(position).getCode();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("district_code", districtCode);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();
	}

	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if(provinceList.size() > 0) {
			dataList.clear();
			for(Province province : provinceList) {
				dataList.add(province.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	private void queryCities() {
		cityList = coolWeatherDB.loadCity(selectedProvince.getId());
		//System.out.println("数据库读取CityList大小：" + cityList.size());
		
		if(cityList.size() > 0) {
			dataList.clear();
			for(City city : cityList) {
				dataList.add(city.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getCode(), "city");
		}
	}

	private void queryDistricts() {
		districtList = coolWeatherDB.loadDistrict(selectedCity.getId());
		if(districtList.size() > 0) {
			dataList.clear();
			for(District district : districtList) {
				dataList.add(district.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getName());
			currentLevel = LEVEL_DISTRICT;
		} else {
			queryFromServer(selectedCity.getCode(), "district");
		}
	}
	
	private void queryFromServer(final String code, final String type) {
		String address;
		if(!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code +".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
			
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			public void onFinish(String response) {
				boolean result = false;
				if("province".equals(type)) {
					result = Utility.handleProvinceResponse(coolWeatherDB, response);
				
				} else if("city".equals(type)) {
					result = Utility.handleCityResponse(coolWeatherDB, response, 
							selectedProvince.getId());
				
				} else if("district".equals(type)){
					result = Utility.handleDistrictResponse(coolWeatherDB, response, 
							selectedCity.getId());
				}
				
				if(result) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if("province".equals(type)) {
								queryProvinces();
							} else if("city".equals(type)) {
								queryCities();
							} else if("district".equals(type)){
								queryDistricts();
							}
						}
					});
				}
			}
			
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						//System.out.println("加载失败!");
						Toast.makeText(ChooseAreaActivity.this, "加载失败！", 
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showProgressDialog() {
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载···");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		if(progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if(currentLevel == LEVEL_DISTRICT) {
			queryCities();
		} else if(currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			if(isFromWeatherActivity) {
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
}
