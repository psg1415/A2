package com.models.order;

import java.sql.*;

import com.models.*;
import com.models.goods.*;

/**
 * 장바구니 Bean
 *
 */
public class Cart extends Dto<Cart>{
	private int idx;
	private int memNo;
	private int goodsNo;
	private Goods goodsInfo;
	private int goodsCnt;
	private boolean isBuy;
	private String regDt;
	
	public Cart() {}
	
	public Cart(int idx, int memNo, int goodsNo, int goodsCnt, boolean isBuy, String regDt) {
		this.idx = idx;
		this.memNo = memNo;
		this.goodsNo = goodsNo;
		this.goodsCnt = goodsCnt;
		this.isBuy = isBuy;
		this.regDt = regDt;
		this.goodsInfo = GoodsDao.getInstance().get(goodsNo);
	}
	
	public Cart(ResultSet rs) throws SQLException {
		this(
			rs.getInt("idx"),
			rs.getInt("memNo"),
			rs.getInt("goodsNo"),
			rs.getInt("goodsCnt"),
			(rs.getInt("isBuy") == 1)?true:false,
			rs.getString("regDt")
		);
	}
	
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getMemNo() {
		return memNo;
	}

	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}

	public int getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(int goodsNo) {
		this.goodsNo = goodsNo;
	}

	public int getGoodsCnt() {
		return goodsCnt;
	}

	public void setGoodsCnt(int goodsCnt) {
		this.goodsCnt = goodsCnt;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public Goods getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(Goods goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	@Override
	public Cart setResultSet(ResultSet rs) throws SQLException {
		return new Cart(rs);
	}
}
