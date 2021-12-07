package com.models.goods;

import java.sql.*;

import com.models.*;

/**
 * 상품분류 Bean 
 *
 */
public class Category extends Dto<Category>{
	
	private int cateCd; // 분류 번호
	private String cateNm; // 분류명
	private boolean isShow; // 노출 여부
	private int listOrder; // 진열 가중치 
	private String regDt; // 분류 등록일시
	
	public Category() {}

	public Category(int cateCd, String cateNm, boolean isShow, int listOrder, String regDt) {
		this.cateCd = cateCd;
		this.cateNm = cateNm;
		this.isShow = isShow;
		this.listOrder = listOrder;
		this.regDt = regDt;
	}
	
	public Category(ResultSet rs) throws SQLException {
		this(
			rs.getInt("cateCd"),
			rs.getString("cateNm"),
			(rs.getInt("isShow") == 1)?true:false,
			rs.getInt("listOrder"),
			rs.getString("regDt")
		);	
	}
	
	public int getCateCd() {
		return cateCd;
	}

	public void setCateCd(int cateCd) {
		this.cateCd = cateCd;
	}

	public String getCateNm() {
		return cateNm;
	}

	public void setCateNm(String cateNm) {
		this.cateNm = cateNm;
	}
	
	public boolean getIsShow() {
		return isShow;
	}
	
	public void setIsShow(boolean isShow) {
		this.isShow = isShow;
	}

	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	@Override
	public Category setResultSet(ResultSet rs) throws SQLException {
		return new Category(rs);
	}
}
