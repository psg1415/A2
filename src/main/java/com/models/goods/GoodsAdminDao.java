package com.models.goods;

import java.util.*;
import javax.servlet.http.*;

import com.core.*;
import com.models.file.*;
import static com.core.DB.*;


/**
 * 상품관리
 * 
 */
public class GoodsAdminDao {
	private static GoodsAdminDao instance = new GoodsAdminDao();
	
	private GoodsAdminDao() {}
	
	public static GoodsAdminDao getInstance() {
		if (instance == null) {
			instance = new GoodsAdminDao();
		}
		
		return instance;
	}
	
	/**
	 * 상품 분류 생성 
	 * 
	 * @param cateNm
	 * @return
	 * @throws Exception
	 */
	public boolean createCategory(String cateNm) throws Exception {
		if (cateNm == null || cateNm.trim().equals("")) {
			throw new Exception("상품분류명을 입력하세요.");
		}
		
		String sql = "INSERT INTO goodscate (cateNm) VALUES (?)";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(DB.setBinding("String", cateNm));
		int rs = DB.executeUpdate(sql, bindings);
		
		return (rs > 0)?true:false;
	}
	
	public boolean createCategory(HttpServletRequest request) throws Exception {
		return createCategory(request.getParameter("cateNm"));
	}
	
	/**
	 * 상품 분류 수정 
	 *  
	 * @param request
	 * @throws Exception
	 */
	public void editCategory(HttpServletRequest request) throws Exception {
		String[] cateCds = request.getParameterValues("cateCd");
		if (cateCds == null) {
			throw new Exception("수정할 분류를 선택하세요.");
		}
	
		for(String cateCd : cateCds) {
			String cateNm = request.getParameter("cateNm_" + cateCd);
			String isShow = request.getParameter("isShow_" + cateCd);
			String listOrder = request.getParameter("listOrder_" + cateCd);
			
			String sql = "UPDATE goodscate SET cateNm = ?, isShow = ?, listOrder = ? WHERE cateCd = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("String", cateNm));
			bindings.add(setBinding("Integer", isShow));
			bindings.add(setBinding("Integer", listOrder));
			bindings.add(setBinding("Integer", cateCd));
			
			DB.executeUpdate(sql, bindings);
		}
	}
	
	/**
	 * 상품 분류 삭제 
	 * 
	 * @param request
	 * @throws Exception
	 */
	public void deleteCategory(HttpServletRequest request) throws Exception {
		String[] cateCds = request.getParameterValues("cateCd");
		if (cateCds == null) {
			throw new Exception("삭제할 분류를 선택하세요.");
		}
		
		for(String cateCd : cateCds) {
			String sql = "DELETE FROM goodscate WHERE cateCd = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", cateCd));
			DB.executeUpdate(sql, bindings);
		}
	}
	
	/**
	 * 상품분류 목록 
	 * 
	 * @param isShow
	 * @return
	 */
	public ArrayList<Category> getCategories(boolean isShow) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM goodscate");
		if (isShow) {
			sb.append(" WHERE isShow=1");
		}
		sb.append(" ORDER BY listOrder DESC, regDt");
		
		ArrayList<DBField> bindings = new ArrayList<>();
		String sql = sb.toString();
		ArrayList<Category> list = DB.executeQuery(sql, bindings, new Category());
		
		return list;
	}
	
	public ArrayList<Category> getCategories() {
		return getCategories(true);
	}
	
	/**
	 * 상품분류 정보
	 * 
	 * @param cateCd
	 * @return
	 */
	public Category getCategory(int cateCd) {
		String sql = "SELECT * FROM goodscate WHERE cateCd = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Integer", cateCd));
		Category category = DB.executeQueryOne(sql, bindings, new Category());
		
		return category;
	}
	
	public Category getCategory(String cateCd) {
		int ccd = (cateCd == null)?0:Integer.valueOf(cateCd);
		return getCategory(ccd);
	}
	
	/**
	 * 상품 등록 처리 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean add(HttpServletRequest request) throws Exception {
		/** 유효성 검사 */
		checkFormData(request);
		
		String sql = "INSERT INTO goods (gid, goodsNm, goodsPrice, cateCd, goodsExplain) VALUES (?,?,?,?,?)";
		ArrayList<DBField> bindings = new ArrayList<>();
		String cateCd = request.getParameter("cateCd");
		cateCd = (cateCd == null || cateCd.trim().equals(""))?"0":cateCd;
		
		String gid = request.getParameter("gid");
		bindings.add(setBinding("Long", gid));
		bindings.add(setBinding("String", request.getParameter("goodsNm")));
		bindings.add(setBinding("Integer", request.getParameter("goodsPrice")));
		bindings.add(setBinding("Integer", cateCd));
		bindings.add(setBinding("String", request.getParameter("goodsExplain")));
		
		int rs = DB.executeUpdate(sql, bindings);
		if (rs > 0) {
			FileDao.getInstance().updateFinish(gid);
		}
		
		return (rs > 0)?true:false;
	}
	
	/**
	 * 상품정보 수정 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean edit(HttpServletRequest request) throws Exception {
		
		/** 상품 등록 유효성 검사 */
		String goodsNo = request.getParameter("goodsNo");
		if (goodsNo == null || goodsNo.trim().equals("")) {
			throw new Exception("잘못된 접근입니다.");
		}
		
		checkFormData(request);
		
		Goods goods = get(goodsNo);
		if (goods == null) {
			throw new Exception("수정할 상품이 존재하지 않습니다.");
		}
		
		String sql = "UPDATE goods SET goodsNm = ?, goodsPrice = ?, cateCd = ?, goodsExplain = ? WHERE goodsNo = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		String cateCd = request.getParameter("cateCd");
		cateCd = (cateCd == null || cateCd.trim().equals(""))?"0":cateCd;
		
		bindings.add(setBinding("String", request.getParameter("goodsNm")));
		bindings.add(setBinding("Integer", request.getParameter("goodsPrice")));
		bindings.add(setBinding("Integer", cateCd));
		bindings.add(setBinding("String", request.getParameter("goodsExplain")));
		bindings.add(setBinding("Integer", goodsNo));
		int rs = DB.executeUpdate(sql, bindings);
		if (rs > 0) {
			FileDao.getInstance().updateFinish(goods.getGid());
		}
		
		return (rs > 0)?true:false;
	}
	
	public boolean delete(String goodsNo) throws Exception {
		
		return delete(Integer.valueOf(goodsNo));
	}
	
	/**
	 * 상품 삭제 
	 * 
	 * @param goodsNo
	 * @return
	 * @throws Exception
	 */
	public boolean delete(int goodsNo) throws Exception {
		
		Goods goods = get(goodsNo);
		if (goods != null) {
			String sql = "DELETE FROM goods WHERE goodsNo = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", goodsNo));
			int rs = DB.executeUpdate(sql, bindings);
			if (rs > 0) {
				FileDao.getInstance().deleteByGid(goods.getGid());
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 목록 상품 수정 
	 * 
	 * @param request
	 * @throws Exception
	 */
	public void editList(HttpServletRequest request) throws Exception {
		String[] goodsNos = request.getParameterValues("goodsNo");
		if (goodsNos == null) {
			throw new Exception("수정할 상품을 선택하세요.");
		}
		
		for(String goodsNo : goodsNos) {
			String listOrder = request.getParameter("listOrder_" + goodsNo);
			String sql = "UPDATE goods SET listOrder = ? WHERE goodsNo = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", listOrder));
			bindings.add(setBinding("Integer", goodsNo));
			DB.executeUpdate(sql, bindings);
		}
	} 
	
	/**
	 * 목록 상품 삭제 
	 * 
	 * @param request
	 * @throws Exception
	 */
	public void deleteList(HttpServletRequest request) throws Exception {
		String[] goodsNos = request.getParameterValues("goodsNo");
		if (goodsNos == null) {
			throw new Exception("삭제할 상품을 선택하세요.");
		}
		
		for(String goodsNo : goodsNos) {
			delete(goodsNo);
		}
	}
	 
	/**
	 * 상품 등록/수정 유효성 검사 
	 * 
	 * @param request
	 * @throws Exception
	 */
	private void checkFormData(HttpServletRequest request) throws Exception {
		String[] required = {
			"gid/잘못된 접근입니다.",
			"goodsNm/상품명을 입력하세요.",
			"goodsPrice/판매가를 입력하세요"
		};
		
		for(String s : required) {
			String[] param = s.split("/");
			String value = request.getParameter(param[0]);
			if (value == null || value.trim().equals("")) {
				throw new Exception(param[1]);
			}
		}
	}
	
	/**
	 * 상품목록 
	 * 
	 * @param cateCd 카테고리 코드 
	 * 
	 * @return
	 */
	public ArrayList<Goods> getList(int cateCd) {
		ArrayList<DBField> bindings = new ArrayList<>();
		StringBuilder sb = new StringBuilder("SELECT g.*, c.cateNm FROM goods g LEFT JOIN goodscate c ON g.cateCd = c.cateCd");
		if (cateCd > 0) {
			sb.append(" WHERE g.cateCd = ?");
			bindings.add(setBinding("Integer", cateCd));
		}
		sb.append(" ORDER BY g.listOrder DESC, g.regDt DESC");
		
		String sql = sb.toString();
		
		ArrayList<Goods> list = DB.executeQuery(sql, bindings, new Goods());
		
		return list;
	}
	
	public ArrayList<Goods> getList(String cateCd) {
		int ccd = (cateCd == null)?0:Integer.valueOf(cateCd);
		return getList(ccd);
	}
	
	public ArrayList<Goods> getList() {
		return getList(0);
	}
	
	/**
	 * 상품정보 조회
	 * 
	 * @param goodsNo
	 * @return
	 */
	public Goods get(int goodsNo) {
		String sql = "SELECT g.*, c.cateNm FROM goods g LEFT JOIN goodscate c ON g.cateCd = c.cateCd WHERE g.goodsNo = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Integer", goodsNo));
		Goods goods = executeQueryOne(sql, bindings, new Goods());
		
		return goods;
	}
	
	public Goods get(String goodsNo) {
		return get(Integer.valueOf(goodsNo));
	}
}
