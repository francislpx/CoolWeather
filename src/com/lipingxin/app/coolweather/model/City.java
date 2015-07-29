package com.lipingxin.app.coolweather.model;

/*
 * @author: pingxinli
 * @email: pingxinli@hengtiansoft.com
 * @version: 2015年7月29日 下午2:28:21 
 * @description: 
*/
public class City {

	private int id;
	private String name;
	private String code;
	private int parentId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
}
