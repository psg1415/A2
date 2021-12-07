package com.models.goods;

import java.sql.*;
import java.util.*;

import com.models.*;
import com.models.file.*;

/**
 * 상품 Bean 
 *
 */
public class Goods extends Dto<Goods>{
	
	private int goodsNo; // 상품번호
	private long gid; // 그룹 ID -> 이미지 등록시 필요
	private String goodsNm; // 상품명
	private int goodsPrice; // 상품가격
	private int cateCd; // 상품 분류
	private String goodsExplain; // 상품 상세 설명
	private String cateNm; // 분류명 
	private int listOrder; // 진열 가중치  
	private String regDt; // 등록일시
	private ArrayList<FileInfo> mainImages;
	private ArrayList<FileInfo> listImages;
	private ArrayList<FileInfo> explainImages;
	private String listImageUrl; // 목록 대표이미지
	private String mainImageUrl; // 메인 대표이미지
	
	public Goods() {}
	
	public Goods(int goodsNo, long gid, String goodsNm, int goodsPrice, int cateCd, String goodsExplain, String cateNm, int listOrder, String regDt) {
		this.goodsNo = goodsNo;
		this.gid = gid;
		this.goodsNm = goodsNm;
		this.goodsPrice = goodsPrice;
		this.cateCd = cateCd;
		this.goodsExplain = goodsExplain;
		this.cateNm = cateNm;
		this.listOrder = listOrder;
		this.regDt = regDt;
		
		/** 상품 업로드 이미지 S */
		ArrayList<FileInfo> images = FileDao.getInstance().gets(gid);
		for (FileInfo image : images) {
			String fileCode = (image.getFileCode() == null)?"":image.getFileCode();
			switch (fileCode) {
				case "goods_main" : 
					if (mainImages == null) {
						mainImages = new ArrayList<FileInfo>();
					}
					mainImages.add(image);
					break;
				case "goods_list" : 
					if (listImages == null) {
						listImages = new ArrayList<FileInfo>();
					}
					
					listImages.add(image);
					break;
				case "goods_explain" : 
					if (explainImages == null) {
						explainImages = new ArrayList<FileInfo>();
					}
					explainImages.add(image);
					break;
			}
		}
		if (mainImages != null && mainImages.size() > 0) {
			mainImageUrl = mainImages.get(0).getUploadUrl();
		}
		
		if (listImages != null && listImages.size() > 0) {
			listImageUrl = listImages.get(0).getUploadUrl();
		}
		/** 상품 업로드 이미지 E */ 
		
	}

	public Goods(ResultSet rs) throws SQLException {
		this(
			rs.getInt("goodsNo"),
			rs.getLong("gid"),
			rs.getString("goodsNm"),
			rs.getInt("goodsPrice"),
			rs.getInt("cateCd"),
			rs.getString("goodsExplain"),
			rs.getString("cateNm"),
			rs.getInt("listOrder"),
			rs.getString("regDt")
		);
	}
	
	public int getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(int goodsNo) {
		this.goodsNo = goodsNo;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public String getGoodsNm() {
		return goodsNm;
	}

	public void setGoodsNm(String goodsNm) {
		this.goodsNm = goodsNm;
	}

	public int getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getCateCd() {
		return cateCd;
	}

	public void setCateCd(int cateCd) {
		this.cateCd = cateCd;
	}

	public String getGoodsExplain() {
		return goodsExplain;
	}

	public void setGoodsExplain(String goodsExplain) {
		this.goodsExplain = goodsExplain;
	}
	
	public String getCateNm() {
		return cateNm;
	}

	public void setCateNm(String cateNm) {
		this.cateNm = cateNm;
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

	public ArrayList<FileInfo> getMainImages() {
		return mainImages;
	}

	public void setMainImages(ArrayList<FileInfo> mainImages) {
		this.mainImages = mainImages;
	}

	public ArrayList<FileInfo> getListImages() {
		return listImages;
	}

	public void setListImages(ArrayList<FileInfo> listImages) {
		this.listImages = listImages;
	}

	public ArrayList<FileInfo> getExplainImages() {
		return explainImages;
	}

	public void setExplainImages(ArrayList<FileInfo> explainImages) {
		this.explainImages = explainImages;
	}

	public String getListImageUrl() {
		return listImageUrl;
	}

	public void setListImageUrl(String listImageUrl) {
		this.listImageUrl = listImageUrl;
	}

	public String getMainImageUrl() {
		return mainImageUrl;
	}

	public void setMainImageUrl(String mainImageUrl) {
		this.mainImageUrl = mainImageUrl;
	}

	@Override
	public Goods setResultSet(ResultSet rs) throws SQLException {
		return new Goods(rs);
	}
}
