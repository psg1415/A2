package com.core;

/**
 * SQL 실행시 바인딩할 데이터 담는 클래스
 *
 */
public class DBField {
	
	private String type; // String, Integer, Double .... 
	private String value; // 바인딩할 데이터
	
	public DBField(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
}