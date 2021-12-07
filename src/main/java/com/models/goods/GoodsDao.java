package com.models.goods;

import java.util.*;

/**
 * 프론트 상품관련
 *
 */
public class GoodsDao {
	private static GoodsDao instance = new GoodsDao();
	private GoodsAdminDao dao = GoodsAdminDao.getInstance();
	
	private GoodsDao() {}
	
	public static GoodsDao getInstance() {
		if (instance == null) {
			instance = new GoodsDao();
		}
		
		return instance;
	}
	
	/**
	 * 상품 목록 
	 * 
	 * @param cateCd
	 * @return
	 */
	public ArrayList<Goods> getList(int cateCd) {
		return dao.getList(cateCd);
	}
	
	public ArrayList<Goods> getList(String cateCd) {
		int ccd = (cateCd == null)?0:Integer.valueOf(cateCd);
		return getList(ccd);
	}
	
	public ArrayList<Goods> getList() {
		return getList(0);
	}
	
	/**
	 * 상품 정보 조회 
	 * 
	 * @param goodsNo
	 * @return
	 */
	public Goods get(int goodsNo) {
		return dao.get(goodsNo);
	}
	
	public Goods get(String goodsNo) {
		return get(Integer.valueOf(goodsNo));
	}
	
	/**
	 * 상품 분류 정보 
	 * 
	 * @param cateCd
	 * @return
	 */
	public Category getCategory(int cateCd) {
		return dao.getCategory(cateCd);
	}
	
	public Category getCategory(String cateCd) {
		int ccd = (cateCd == null)?0:Integer.valueOf(cateCd);
		return getCategory(ccd);
	}
	
	/**
	 * 상품분류
	 * 
	 * @return
	 */
	public ArrayList<Category> getCategories() {
		return dao.getCategories();
	}
}
