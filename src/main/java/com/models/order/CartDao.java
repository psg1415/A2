package com.models.order;

import java.util.*;
import javax.servlet.http.*;

import com.core.*;
import com.models.member.*;
import com.models.goods.*;
import static com.core.DB.*;

/**
 * 장바구니 관련
 *
 */
public class CartDao {
	private static CartDao instance = new CartDao();
	
	private CartDao() {}
	
	public static CartDao getInstance() {
		return instance;
	}
	
	/**
	 * 장바구니 담기
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean add(HttpServletRequest request) throws Exception {
		Member member = (Member)request.getAttribute("member");
		String mode = request.getParameter("mode");
		int isBuy = (mode != null && mode.equals("order"))?1:0;
		String goodsNo = request.getParameter("goodsNo");
		int goodsCnt = 1;
		if (request.getParameter("goodsCnt") != null) {
			goodsCnt = Integer.valueOf(request.getParameter("goodsCnt"));
			if (goodsCnt <= 0) goodsCnt = 1;
		}
		
		if (!MemberDao.isLogin()) {
			throw new Exception("로그인이 필요합니다.");
		}
		
		if (goodsNo == null) {
			throw new Exception("잘못된 접근입니다.");
		}
		
		/** 이미 상품이 존재한다면 수량 증가, 없다면 추가 */
		String sql = "SELECT * FROM cart WHERE memNo = ? AND goodsNo = ? AND isBuy = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Integer", member.getMemNo()));
		bindings.add(setBinding("Integer", goodsNo));
		bindings.add(setBinding("Integer", isBuy));
		Cart cart = DB.executeQueryOne(sql, bindings, new Cart());
		if (cart == null) {
			sql = "INSERT INTO cart (memNo, goodsNo, goodsCnt, isBuy) VALUES (?,?,?,?)";
			bindings = new ArrayList<DBField>();
			bindings.add(setBinding("Integer", member.getMemNo()));
			bindings.add(setBinding("Integer", goodsNo));
			bindings.add(setBinding("Integer", goodsCnt));
			bindings.add(setBinding("Integer", isBuy));
		} else {
			goodsCnt += cart.getGoodsCnt();
			
			sql = "UPDATE cart SET goodsCnt = ? WHERE idx = ?";
			bindings = new ArrayList<DBField>();
			bindings.add(setBinding("Integer", goodsCnt));
			bindings.add(setBinding("Integer", cart.getIdx()));
		}
		
		int rs = executeUpdate(sql, bindings);
		
		return (rs > 0)?true:false;
	}
	
	/**
	 * 선택상품 삭제 
	 * 
	 * @param request
	 * @throws Exception
	 */
	public void delete(HttpServletRequest request) throws Exception {
		String[] idxes = request.getParameterValues("idx");
		if (idxes == null || idxes.length == 0) {
			throw new Exception("삭제할 상품을 선택하세요.");
		}
		
		for (String idx : idxes) {
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", idx));
			String sql = "DELETE FROM cart WHERE idx = ?";
			executeUpdate(sql, bindings);
		}
	}
	
	/**
	 * 장바구니 수량 변경
	 * 
	 * @param idx
	 * @param goodsCnt
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsCnt(int idx, int goodsCnt) throws Exception {
		
		if (idx == 0) {
			throw new Exception("잘못된 접근입니다.");
		}
		
		if (goodsCnt <= 0) {
			throw new Exception("수량은 1이상 입력하세요.");
		}
		
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Integer", goodsCnt));
		bindings.add(setBinding("Integer", idx));
		String sql = "UPDATE cart SET goodsCnt = ? WHERE idx = ?";
		int rs = DB.executeUpdate(sql, bindings);
		
		return (rs > 0)?true:false;
	}
	
	public boolean updateGoodsCnt(HttpServletRequest request) throws Exception {
		int idx = 0;
		int goodsCnt = 1;
		if (request.getParameter("idx") != null) {
			idx = Integer.valueOf(request.getParameter("idx"));
		}
		
		if (request.getParameter("goodsCnt") != null) {
			goodsCnt = Integer.valueOf(request.getParameter("goodsCnt"));
		}
		
		return updateGoodsCnt(idx, goodsCnt);
	}
	/**
	 * 장바구니 목록 
	 * 
	 * @param isBuy - false - 장바구니, true - 바로주문
	 * @return
	 */
	public ArrayList<Cart> gets(String[] idxes, boolean isBuy) {
		ArrayList<Cart> list = null;
		if (MemberDao.isLogin()) {
			ArrayList<DBField> bindings = new ArrayList<>();
			Member member = (Member)Req.get().getAttribute("member");
			int memNo = member.getMemNo();
			bindings.add(setBinding("Integer", memNo));
			StringBuilder sb = new StringBuilder("SELECT * FROM cart WHERE memNo = ?");
			
			if (idxes == null) {
				int _isBuy = isBuy?1:0;
				sb.append(" AND isBuy = ?");
				bindings.add(setBinding("Integer", _isBuy));
			} else {
				sb.append(" AND idx IN (");
				boolean isFirst = true;
				for(String idx : idxes) {
					if (!isFirst) sb.append(",");
					sb.append("?");
					bindings.add(setBinding("Integer", idx));
					isFirst = false;
				}
				sb.append(")");
			}
			sb.append(" ORDER BY idx");
			String sql = sb.toString();
			
			list = executeQuery(sql, bindings, new Cart());
		}
		
		return list;
	}
	
	public ArrayList<Cart> gets(HttpServletRequest request) {
		boolean isBuy = (request.getParameterValues("idx") == null)?true:false;
		return gets(request.getParameterValues("idx"), isBuy);
	}
	
	public ArrayList<Cart> gets() {
		return gets(null, false);
	}
	
	/**
	 * 장바구니에서 유입된 경우 바로 구매 상품 삭제 
	 * 	  장바구니에서 유입된 경우 URL에 장바구니 등록번호 idx가 있으며 그것으로 구분
	 * 
	 * @param request
	 */
	public void updateOrderCart(HttpServletRequest request) {
		if (MemberDao.isLogin() && request.getParameterValues("idx") != null) {
			Member member = (Member)request.getAttribute("member");
			int memNo = member.getMemNo();
			String sql = "DELETE FROM cart WHERE memNo = ? AND isBuy=1";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", memNo));
			executeUpdate(sql, bindings);
		}
	}
	
	/**
	 * 바로구매 상품 삭제
	 * 
	 */
	public void deleteBuyGoods() {
		if (MemberDao.isLogin()) {
			Member member = (Member)Req.get().getAttribute("member");
			int memNo = member.getMemNo();
			String sql = "DELETE FROM cart WHERE memNo = ? AND isBuy=1";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", memNo));
			executeUpdate(sql, bindings);
		}
	}
	
	/**
	 * 상품 결제 총 금액 
	 * 
	 * @param items
	 * @return
	 */
	public int getSettlePrice(ArrayList<Cart> items) {
		int total = 0;
		if (items != null && items.size() > 0) {
			for(Cart item : items) {
				Goods goods = item.getGoodsInfo();
				if (goods == null) continue;
				
				total += goods.getGoodsPrice() * item.getGoodsCnt();
			}
		}
		
		return total;
	}
}
