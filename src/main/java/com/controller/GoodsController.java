package com.controller;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import com.models.goods.*;
import com.models.order.*;
import static com.core.CommonLib.*;


/**
 * 상품관련 Controller 
 *		- /goods/list - 상품목록 
 *      - /goods/view - 상품 상세
 */
@WebServlet("/goods/*")
public class GoodsController extends HttpServlet {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private GoodsDao dao;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		dao = GoodsDao.getInstance();
		
		String URI = request.getRequestURI();
		String mode = URI.substring(URI.lastIndexOf("/") + 1);

		switch(mode) {
			case "list" : // 상품목록
				listController();
				break;
			case "view": // 상품상세
				viewController();
				break;
			case "category" : // 상품분류
				categoryController();
				break;
			default : // 없는 페이지 
				RequestDispatcher rd = request.getRequestDispatcher("/views/error/404.jsp");
				rd.forward(request, response);
		}
	}
	
	/**
	 * 상품목록 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listController() throws ServletException, IOException {
		try {
			String cateCd = request.getParameter("cateCd");
			if (cateCd == null || cateCd.trim().equals("")) {
				throw new Exception("잘못된 접근입니다.");
			}
			
			ArrayList<Goods> list = dao.getList(cateCd);
			Category category = dao.getCategory(cateCd);
			
			request.setAttribute("list", list);
			request.setAttribute("category", category);
			RequestDispatcher rd = request.getRequestDispatcher("/views/goods/list.jsp");
			rd.include(request, response);
		} catch (Exception e) {
			alert(e, -1);
		}
	}
	
	/**
	 * 상품상세
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewController() throws ServletException, IOException {
		try {
			String goodsNo = request.getParameter("goodsNo");
			if (goodsNo == null || goodsNo.trim().equals("")) {
				throw new Exception("잘못된 접근입니다.");
			}
			
			Goods goods = dao.get(goodsNo);
			
			if (goods == null) {
				throw new Exception("상품이 존재하지 않습니다.");
			}
			
			/** 바로구매 장바구니 상품 삭제 */
			CartDao.getInstance().deleteBuyGoods();
			
			request.setAttribute("goods", goods);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/goods/view.jsp");
			rd.include(request, response);
		} catch (Exception e) {
			alert(e, -1);
		}
	}
	
	/**
	 * 카테고리 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void categoryController() throws ServletException, IOException {
		ArrayList<Category> categories = dao.getCategories();
		request.setAttribute("categories", categories);
		
		RequestDispatcher rd = request.getRequestDispatcher("/views/goods/category.jsp");
		rd.include(request, response);
	}
}
