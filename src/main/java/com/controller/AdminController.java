package com.controller;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

import static com.core.CommonLib.*;

import com.core.DB;
import com.models.goods.*; 
import com.models.member.*;

/**
 * 관리자 페이지 Controller 
 *    상품관리
 *    		- /admin/goods/list - 등록 상품 목록
 *    		- /admin/goods/add - 상품등록 
 *    		- /admin/goods/edit - 상품수정
 *    		- /admin/goods/delete - 상품 삭제 
 *    주문관리
 *    		- /admin/order/list - 주문 목록 (GET - 목록, POST - 수정, 삭제) 
 *    		- /admin/order/view - 주문 상세(GET - 상세, POST - 수정, 삭제)
 *    		
 */
@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String httpMethod;
	private GoodsAdminDao dao;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 관리자 접근 권한 체크 */
		if (!MemberDao.isAdmin()) {
			alert("접근권한이 없습니다.", -1);
			return;
		}
		
		this.request = request;
		this.response = response;
		
		httpMethod = (String)request.getAttribute("httpMethod"); // 요청 메서드 
		
		dao = GoodsAdminDao.getInstance();
		
		String rootURL = (String)request.getAttribute("rootURL");
		String URI = request.getRequestURI().replace(rootURL, "");
		String[] URIs = URI.split("/");
		
		boolean isPageExists = false; // 페이지가 있는지 여부 
		if (URIs.length >= 3) {
			String type = URIs[URIs.length - 2];
			String mode = URIs[URIs.length - 1];
			
			isPageExists = checkPageExists(type, mode); // 페이지 존재 여부 체크 
			if (isPageExists) {
				if (type.equals("goods")) { // 상품관리
					/** 상품관리 서브 메뉴 */
					RequestDispatcher sub = request.getRequestDispatcher("/views/admin/goods/_sub.jsp");
					sub.include(request, response);
					
					switch(mode) {
						case "list" :  // 상품 목록 
							goodsListController();
							break;
						case "add" : // 상품 추가 
							goodsAddController();
							break;
						case "edit" : // 상품 수정 
							goodsEditController();
							break;
						case "delete" : // 상품 삭제
							goodsDeleteController();
							break;
						case "cate" : // 상품 분류관리
							goodsCateController();
							break;
					}
					
				} else if (type.equals("order")) { // 주문관리 
					switch (mode) {
						case "list":
							orderListController();
							break;
						case "view":
							orderViewController();
							break;
					}
				}
				
			} // endif 
			
			
		}
		
		if (!isPageExists) { // 페이지가 없는 경우 상품관리로 이동 
			String url = request.getServletContext().getContextPath() + "/admin/goods/list";
			go(url);
			
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/** 
	 * 상품 목록 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goodsListController() throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 상품 목록 양식 
			String cateCd = request.getParameter("cateCd");
			ArrayList<Goods> list = dao.getList(cateCd); // 상품목록 
			int total = DB.getCount("goods"); // 등록 상품 개수 
			ArrayList<Category> categories = dao.getCategories(false);
			
			request.setAttribute("list", list);
			request.setAttribute("total", total);
			request.setAttribute("categories", categories);
			request.setAttribute("cateCd", cateCd);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/list.jsp");
			rd.include(request, response);
		} else { // 상품 목록 수정, 삭제 처리 
			try {
				String mode = request.getParameter("mode");
				if (mode != null && mode.equals("delete")) { // 상품삭제 
					dao.deleteList(request);
				} else { // 상품 수정 
					dao.editList(request);
				}
			} catch (Exception e) {
				alert(e);
			}
			
		}
	}
	
	/**
	 * 상품 추가 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goodsAddController() throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 상품 등록 양식
			/** action 모드 */
			request.setAttribute("mode", "add");
			
			/** 그룹 ID 생성 */
			long gid = System.currentTimeMillis();
			request.setAttribute("gid", gid);
			
			/** 분류 목록 */
			ArrayList<Category> categories = dao.getCategories(false);
			request.setAttribute("categories", categories);
			
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/form.jsp");
			rd.include(request, response);
		} else { // 상품 등록 처리 
			try {
				dao.add(request);
				go("../goods/list", "parent");
			} catch (Exception e) {
				alert(e);
			}
		}
	}
	
	/**
	 * 상품 수정 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goodsEditController() throws ServletException, IOException {
		try {
			if (httpMethod.equals("GET")) { // 상품 수정 양식
				String goodsNo = request.getParameter("goodsNo");
				if (goodsNo == null || goodsNo.trim().equals("")) {
					throw new Exception("잘못된 접근입니다.");
				}
				
				/** 상품 등록 정보 */
				Goods goods = dao.get(goodsNo);
				if (goods == null) {
					throw new Exception("등록되지 않은 상품입니다.");
				}
				request.setAttribute("goods", goods);
				
				/** action 모드 */
				request.setAttribute("mode", "edit");
				
				/** 분류 목록 */
				ArrayList<Category> categories = dao.getCategories(false);
				request.setAttribute("categories", categories);
				
				
				RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/form.jsp");
				rd.include(request, response);
			} else { // 상품 수정 처리 
				dao.edit(request);
				alert("수정되었습니다.");
				reload("parent");
			}
		} catch (Exception e) {
			if (httpMethod.equals("GET")) {
				alert(e, -1);
			} else {
				alert(e);
			}
		}
	}
	
	/**
	 * 상품 삭제 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goodsDeleteController() throws ServletException, IOException {
		
	}
	
	/**
	 * 분류 관리 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goodsCateController() throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 분류 등록 양식 
			ArrayList<Category> list = dao.getCategories(false);
			request.setAttribute("list", list);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/cate.jsp");
			rd.include(request, response);

		} else { // 분류 등록 
			try {
				String mode = request.getParameter("mode");
				mode = (mode == null)?"":mode;
				System.out.println("mode : " + mode);
				switch (mode) {
					case "edit" : // 분류 수정
						dao.editCategory(request);
						reload("parent");
						break;
					case "delete" : // 분류 삭제
						dao.deleteCategory(request);
						reload("parent");
						break;
					default : // 분류 등록 
						boolean result = dao.createCategory(request);
						if (!result) {
							throw new Exception("상품분류 등록 실패!");
						}
					
						reload("parent"); // 분류 등록 성공 -> 분류 목록 새로고침
				}
			} catch (Exception e) {
				alert(e);
			}
		}
	}
	
	/**
	 * 주문 목록 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void orderListController() throws ServletException, IOException {
		
	}
	
	/**
	 * 주문 상세 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void orderViewController() throws ServletException, IOException {
		
	}
	
	/**
	 * 페이지 존재 여부 체크 
	 * 
	 * @param type
	 * @param mode
	 */
	private boolean checkPageExists(String type, String mode) {
		String[] URLs = getAdminURLs();
		StringBuilder sb = new StringBuilder("/");
		sb.append(type);
		sb.append("/");
		sb.append(mode);
		String URL = sb.toString();
		for (String s : URLs) {
			if (s.indexOf(URL) != -1) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 관리자 메뉴 URL
	 * 
	 * @return
	 */
	private String[] getAdminURLs() {
		String[] URLs = {
				"/goods/list",
				"/goods/add",
				"/goods/edit",
				"/goods/delete",
				"/goods/cate",
				"/order/list",
				"/order/view",
		};
		return URLs; 
	}
}
