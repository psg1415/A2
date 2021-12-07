package com.models.order;

import java.util.*;
import javax.servlet.http.*;

import com.core.*;
import com.models.member.*;
import static com.core.DB.*;

/**
 * 주문관련
 *
 */
public class OrderDao {
	private static OrderDao instance = new OrderDao();
	private long orderNo;
	
	private OrderDao() {}
	
	public static OrderDao getInstance() {
		return instance;
	}
	
	/**
	 * 주문접수
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean apply(HttpServletRequest request) throws Exception {
		
		/** 유효성 검사 */
		checkApplyData(request);
		
		CartDao dao = CartDao.getInstance();
		ArrayList<Cart> items = dao.gets(request);
		if (items == null || items.size() == 0) {
			throw new Exception("주문할 상품이 존재하지 않습니다.");
		}
		
		long orderNo = generateOrderNo(); // 주문번호
		int settlePrice = dao.getSettlePrice(items); // 결제금액
		/**
		 * 연속 SQL 실행이라 트랜잭션이 필요하나
		 * DB 클래스의 변경 사항이 정상적으로 반영되지 않음..
		 *  DB 클래스에 트랜잭션은 따로 하나 만들어야 함.. 
		 */
		//DB.setAutoCommit(true);
		/** 주문상품 추가 */
		String sql = "INSERT INTO ordergoods (orderNo, goodsNo, goodsNm, goodsCnt) VALUES (?,?,?,?)";
		for(Cart item : items) {
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Long", orderNo));
			bindings.add(setBinding("Integer", item.getGoodsNo()));
			bindings.add(setBinding("String", item.getGoodsInfo().getGoodsNm()));
			bindings.add(setBinding("Integer", item.getGoodsCnt()));
			executeUpdate(sql, bindings);
		}
		
		/** 주문서 추가 */
		Member member = (Member)request.getAttribute("member");
		String orderCellPhone = request.getParameter("orderCellPhone").replaceAll("[^0-9]", "");
		String receiverCellPhone = request.getParameter("receiverCellPhone").replaceAll("[^0-9]", "");
		String sql2 = "INSERT INTO `order` (orderNo,memNo,orderName,orderCellPhone,orderEmail, receiverName,`receiverCellPhone`,receiverZonecode,receiverAddress,receiverAddressSub,settlePrice) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Long", orderNo));
		bindings.add(setBinding("Integer", member.getMemNo()));
		bindings.add(setBinding("String", request.getParameter("orderName")));
		bindings.add(setBinding("String", orderCellPhone));
		bindings.add(setBinding("String", request.getParameter("orderEmail")));
		bindings.add(setBinding("String", request.getParameter("receiverName")));
		bindings.add(setBinding("String", receiverCellPhone));
		bindings.add(setBinding("String", request.getParameter("receiverZonecode")));
		bindings.add(setBinding("String", request.getParameter("receiverAddress")));
		bindings.add(setBinding("String", request.getParameter("receiverAddressSub")));
		bindings.add(setBinding("Integer", settlePrice));
		int rs = DB.executeUpdate(sql2, bindings);

		//DB.commit();
		if (rs > 0) { // 주문한 장바구니 내역 비우기 
			CartDao.getInstance().delete(request);
		}
		
		
		return (rs > 0)?true:false;
	}
	
	/**
	 * 주문접수 데이터 유효성 검사 
	 * 
	 * @param request
	 * @throws Exception
	 */
	private void checkApplyData(HttpServletRequest request) throws Exception {
		
		String[] idxes = request.getParameterValues("idx");
		if (idxes == null || idxes.length == 0) {
			throw new Exception("잘못된 접근입니다.");
		}
		
		String[] required = {
				"orderName/주문자명을 입력하세요.",
				"orderCellPhone/주문자 휴대전화번호를 입력하세요.",
				"orderEmail/주문자 이메일주소를 입력하세요.",
				"receiverName/수령자명을 입력하세요.",
				"receiverCellPhone/수령자 휴대전화번호를 입력하세요.",
				"receiverZonecode/주소를 입력하세요.",
				"receiverAddress/주소를 입력하세요.",
				"receiverAddressSub/주소를 입력하세요."
		};
		
		for(String s : required) {
			String[] param = s.split("/");
			String value = request.getParameter(param[0]);
			if (value == null || value.trim().equals("")) {
				throw new Exception(param[1]);
			}
		}
		
		/** 휴대전화번호 형식 체크 */
		String pattern = "01[016789][0-9]{3,4}[0-9]{4}";
		String orderCellPhone = request.getParameter("orderCellPhone").replaceAll("[^0-9]", "");
		if (!orderCellPhone.matches(pattern)) {
			throw new Exception("휴대전화번호 양식이 아닙니다.");		
		}
		
		String receiverCellPhone = request.getParameter("receiverCellPhone").replaceAll("[^0-9]", "");
		if (!receiverCellPhone.matches(pattern)) {
			throw new Exception("휴대전화번호 양식이 아닙니다.");
		}
	}
	
	/**
	 * 주문번호 생성 
	 * 
	 * @return
	 */
	public long generateOrderNo() {
		long orderNo =  System.currentTimeMillis();
		this.orderNo = orderNo;
		
		return orderNo;
	}
	
	/**
	 * 주문번호 
	 * 
	 * @return
	 */
	public long getOrderNo() {
		return orderNo;
	}
	
	/**
	 * 주문 정보
	 * 
	 * @param orderNo
	 * @return
	 */
	public Order getInfo(long orderNo) {
		String sql = "SELECT * FROM `order` WHERE orderNo = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Long", orderNo));
		Order order = DB.executeQueryOne(sql, bindings, new Order());
				
		return order;
	}
	
	public Order getInfo(String orderNo) {
		long ordNo = (orderNo == null)?0L:Long.valueOf(orderNo);
		
		return getInfo(ordNo);
	}
	
	/**
	 * 주문 상품 정보
	 * 
	 */
	public ArrayList<OrderGoods> getGoodsInfo(long orderNo) {
		String sql = "SELECT * FROM ordergoods WHERE orderNo = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Long", orderNo));
		ArrayList<OrderGoods> list = DB.executeQuery(sql, bindings, new OrderGoods());
		
		return list;
	}
	
	public ArrayList<OrderGoods> getGoodsInfo(String orderNo) {
		long ordNo = (orderNo == null)?0L:Long.valueOf(orderNo);
		
		return getGoodsInfo(ordNo);
	}
	
	/**
	 * 주문목록 
	 * 
	 */
	public ArrayList<Order> getOrders(int memNo) {
		ArrayList<DBField> bindings = new ArrayList<>();
		StringBuilder sb = new StringBuilder("SELECT * FROM `order`");
		if (memNo > 0) {
			sb.append(" WHERE memNo = ?");
			bindings.add(setBinding("Integer", memNo));
		}
		sb.append(" ORDER BY regDt DESC");
		String sql = sb.toString();
		
		ArrayList<Order> list = DB.executeQuery(sql, bindings, new Order());
		
		return list;
	}
	
	public ArrayList<Order> getOrders(String memNo) {
		return getOrders(memNo);
	}
	
	public ArrayList<Order> getOrders() {
		return getOrders(0);
	}
} 
