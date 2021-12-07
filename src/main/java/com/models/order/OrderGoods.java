package com.models.order;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.models.*;
import com.models.goods.*;

/**
 * 주문상품 
 *
 */
public class OrderGoods extends Dto<OrderGoods>{
	
	private int idx; // 주문상품 등록번호
	private long orderNo; // 주문번호
	private int goodsNo; // 주문상품번호
	private String goodsNm; // 주문상품명
	private int goodsCnt; // 주문상품갯수
	private String regDt; // 주문일시
	private Goods goods; // 상품정보
	
	public OrderGoods() {}
	
	public OrderGoods(int idx, long orderNo, int goodsNo, String goodsNm, int goodsCnt, String regDt) {
		this.idx = idx;
		this.orderNo = orderNo;
		this.goodsNo = goodsNo;
		this.goodsNm = goodsNm;
		this.goodsCnt = goodsCnt;
		this.regDt = regDt;
		
		this.goods = GoodsDao.getInstance().get(goodsNo);
	}
	
	public OrderGoods(ResultSet rs) throws SQLException {
		this(
			rs.getInt("idx"),
			rs.getLong("orderNo"),
			rs.getInt("goodsNo"),
			rs.getString("goodsNm"),
			rs.getInt("goodsCnt"),
			rs.getString("regDt")
		);
	}
	
	public int getIdx() {
		return idx;
	}
	
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public int getGoodsNo() {
		return goodsNo;
	}
	
	public void setGoodsNo(int goodsNo) {
		this.goodsNo = goodsNo;
	}
	
	public String getGoodsNm() {
		return goodsNm;
	}
	
	public void setGoodsNm(String goodsNm) {
		this.goodsNm = goodsNm;
	}
	
	public int getGoodsCnt() {
		return goodsCnt;
	}
	
	public void setGoodsCnt(int goodsCnt) {
		this.goodsCnt = goodsCnt;
	}
	
	public String getRegDt() {
		return regDt;
	}
	
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	@Override
	public OrderGoods setResultSet(ResultSet rs) throws SQLException {
		return new OrderGoods(rs);
	}
}
