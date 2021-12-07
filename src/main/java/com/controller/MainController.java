package com.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

import com.models.goods.*;
import com.models.snslogin.*;


/**
 * 메인 페이지 - index.jsp
 *
 */
public class MainController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		SocialLogin.clear();
		
		ArrayList<Goods> list = GoodsDao.getInstance().getList();
		request.setAttribute("list", list);
		RequestDispatcher rd = request.getRequestDispatcher("/views/main/index.jsp");
		rd.include(request, response);
	}
}