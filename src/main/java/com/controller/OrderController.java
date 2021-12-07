package com.controller;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import com.models.member.*;
import com.models.order.*;
import static com.core.CommonLib.*;

import org.json.simple.*;

/**
 * 주문관련 Controller 
 *		- /order/cart -> 장바구니 
 *      - /order/form -> 주문하기 
 *      - /order/confirmation -> 주문완료 및 확인
 */
@WebServlet("/order/*")
public class OrderController extends HttpServlet {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String httpMethod;
	private CartDao cartDao;
	private OrderDao orderDao;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		httpMethod = request.getMethod().toUpperCase();
		cartDao = CartDao.getInstance();
		orderDao = OrderDao.getInstance();
		
		String URI = request.getRequestURI();
		String mode = URI.substring(URI.lastIndexOf("/") + 1);

		switch(mode) {
			case "cart" : // 장바구니  
				cartController();
				break;
			case "cart_ps" : // 장바구니 선택상품 삭제, 주문 처리  
				cartPsController();
				break;
			case "form": // 주문하기
				formController();
				break;
			case "confirmation" : // 주문완료 및 확인 
				confirmationController();
				break;
			case "list" :
				listController(); // 주문내역 확인
				break;
			case "view" : 
				viewController(); // 주문상세 보기
				break;
			default : // 없는 페이지 
				RequestDispatcher rd = request.getRequestDispatcher("/views/error/404.jsp");
				rd.forward(request, response);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * 장바구니 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void cartController() throws ServletException, IOException {
		try {
			if (!MemberDao.isLogin()) {
				throw new Exception("로그인이 필요합니다.");
			}
			
			if (httpMethod.equals("GET")) { // 장바구니 페이지
				
				/** 바로구매 장바구니 상품 삭제 */
				CartDao.getInstance().deleteBuyGoods();
				
				ArrayList<Cart> list = cartDao.gets();
				
				request.setAttribute("list", list);
				RequestDispatcher rd = request.getRequestDispatcher("/views/order/cart.jsp");
				rd.include(request, response);
			} else { // 장바구니추가, 바로구매시 DB 처리 
				String mode = request.getParameter("mode");
				mode = (mode == null)?"order":mode;
				boolean result = cartDao.add(request);
				if (!result) {
					throw new Exception("장바구니 담기에 실패하였습니다.");
				}
				
				if (mode.equals("order")) { // 바로구매
					go("../order/form", "parent");
				} else { // 장바구니 
					go("../order/cart", "parent");
				}
			}
		} catch (Exception e) {
			alert(e);
			
			if (!MemberDao.isLogin()) {
				go("../member/login", "parent");
			}
		}
	}
	
	/**
	 * 장바구니 선택삭제, 선택 주문 처리 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void cartPsController() throws ServletException, IOException {
		String mode = request.getParameter("mode");
		mode = (mode == null)?"":mode;
		try {
			if (!MemberDao.isLogin()) {
				throw new Exception("로그인이 필요합니다.");
			}
			
			switch (mode) {
				// 장바구니 선택 상품 삭제 
				case "delete" :
					cartDao.delete(request);
					reload("parent");
					break;
				// 장바구니 상품 수량 변경 
				case "change" : 
					boolean result = cartDao.updateGoodsCnt(request);
					if (!result) {
						throw new Exception("수량변경 실패하였습니다.");
					}
					
					HashMap<Object, Object> map = new HashMap<>();
					map.put("success", true);
					response.setHeader("Content-Type", "application/json");
					response.getWriter().print(new JSONObject(map));
					break;
				default : // 선택상품 주문
					String[] idxes = request.getParameterValues("idx");
					if (idxes != null && idxes.length == 0) {
						throw new Exception("주문할 상품을 선택하세요");
					}
					
					boolean isFirst = true;
					StringBuilder sb = new StringBuilder();
					sb.append("../order/form?");
					for(String idx : idxes) {
						if (!isFirst) sb.append("&");
						sb.append("idx=");
						sb.append(idx);
						isFirst = false;
					}
					
					String url = sb.toString();
					go(url, "parent");
					
			}
			
			
		} catch (Exception e) {
			if (mode.equals("change")) {
				printJson(e);
			} else {
				alert(e);
				
				if (!MemberDao.isLogin()) {
					go("../member/login", "parent");
				}
			} // endif 
		}
	}
	
	/**
	 * 주문하기
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void formController() throws ServletException, IOException {
		try {
			if (!MemberDao.isLogin()) {
				throw new Exception("로그인이 필요합니다.");
			}
			
			if (httpMethod.equals("GET")) { // 주문 양식
				/** 장바구니 유입 상품의 경우 바로 구매 상품 삭제 */
				cartDao.updateOrderCart(request);
				
				/** 주문 상품 */
				ArrayList<Cart> items = cartDao.gets(request);
				request.setAttribute("items", items);
				
				/** 결제 금액 */
				int settlePrice = cartDao.getSettlePrice(items);
				
				request.setAttribute("settlePrice", settlePrice);
				
				RequestDispatcher rd = request.getRequestDispatcher("/views/order/order.jsp");
				rd.include(request, response);
			} else { // 주문 접수 처리 
				boolean result = orderDao.apply(request);
				if (!result) {
					throw new Exception("주문접수 실패하였습니다.");
				}
				
				go("../order/confirmation?orderNo=" + orderDao.getOrderNo(), "parent");
			}
			
		} catch (Exception e) {
			alert(e);
			if (!MemberDao.isLogin()) {
				go("../member/login", "parent");
			}
		}
	}
	
	/**
	 * 주문완료 및 확인
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void confirmationController() throws ServletException, IOException {
		try {
			String orderNo = request.getParameter("orderNo");
			if (orderNo == null) {
				throw new Exception("잘못된 접근입니다.");
			}
			
			if (!MemberDao.isLogin()) {
				throw new Exception("로그인이 필요한 서비스입니다.");
			}
			
			Order order = orderDao.getInfo(orderNo);
			ArrayList<OrderGoods> orderGoods = orderDao.getGoodsInfo(orderNo);
			if (order == null) {
				throw new Exception("주문 내역이 존재하지 않습니다.");
			}
			
			Member member = (Member)request.getAttribute("member");
			if (order.getMemNo() != member.getMemNo()) {
				throw new Exception("본인의 주문만 확인할 수 있습니다.");
			}
			
			request.setAttribute("order", order);
			request.setAttribute("items", orderGoods);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/order/confirmation.jsp");
			rd.include(request, response);
		} catch (Exception e) {
			alert(e, -1);
		}
	}
	
	/**
	 * 주문 목록 확인 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listController() throws ServletException, IOException {
		try {
			if (!MemberDao.isLogin()) {
				throw new Exception("로그인이 필요한 서비스 입니다.");
			}
			
			Member member = (Member)request.getAttribute("member");
			ArrayList<Order> list = orderDao.getOrders(member.getMemNo());
			request.setAttribute("list", list);
			RequestDispatcher rd = request.getRequestDispatcher("/views/order/list.jsp");
			rd.include(request, response);
		} catch (Exception e) {
			alert(e, -1);
		}
	}
	
	/**
	 * 주문상세 보기 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewController() throws ServletException, IOException {
		try {
			String orderNo = request.getParameter("orderNo");
			if (orderNo == null) {
				throw new Exception("잘못된 접근입니다.");
			}
			
			if (!MemberDao.isLogin()) {
				throw new Exception("로그인이 필요한 서비스입니다.");
			}
			
			Order order = orderDao.getInfo(orderNo);
			ArrayList<OrderGoods> orderGoods = orderDao.getGoodsInfo(orderNo);
			if (order == null) {
				throw new Exception("주문 내역이 존재하지 않습니다.");
			}
			
			Member member = (Member)request.getAttribute("member");
			if (order.getMemNo() != member.getMemNo()) {
				throw new Exception("본인의 주문만 확인할 수 있습니다.");
			}
			
			request.setAttribute("order", order);
			request.setAttribute("items", orderGoods);
			
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/order/view.jsp");
			rd.include(request, response);
		} catch (Exception e) {
			alert(e, -1);
		}
	}
}



