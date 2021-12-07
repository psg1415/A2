package com.models;

import java.sql.*;

public abstract class Dto<T> {
	/** 
	 * ResultSet으로 값 지정
	 * @param rs
	 */
	public abstract T setResultSet(ResultSet rs) throws SQLException;
}