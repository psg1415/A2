package com.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

import com.core.*;
/**
 * 공통 필터 - 사이트 전역 적용 
 *
 */
public class CommonFilter implements Filter {
	
	public void init(FilterConfig config) throws ServletException{
		
	}
	/** 
	 * 정적 디렉토리(헤더, 푸터가 적용되지 않는 경로)
	 *    - css, js, image ... 
	 */
	private String[] staticDirs = {"resources", "file"};
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		BootStrap.init(request, response);
		
		// 헤더 출력
		if (isPrintOk(request)) {
			printHeader(request, response);
		}
		
		chain.doFilter(request, response);
		
		// 푸터 출력
		if (isPrintOk(request)) {
			printFooter(request, response);
		}	
	}
	
	/** 
	 * 헤더 출력 
	 * 
	 */
	private void printHeader(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		RequestDispatcher rd = request.getRequestDispatcher("/views/outline/header/main.jsp");
		rd.include(request, response);
		
		/** 헤더 추가 영역 처리 */
		Config config = Config.getInstance();
		String addonURL = config.getHeaderAddon();
		if (addonURL != null) {
			RequestDispatcher inc = request.getRequestDispatcher(addonURL);
			inc.include(request, response);
		}
	}
	
	/**
	 * 푸터 출력 
	 */
	private void printFooter(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		
		/** 푸터 추가 영역 처리 */
		Config config = Config.getInstance();
		String addonURL = config.getFooterAddon();
		if (addonURL != null) {
			RequestDispatcher inc = request.getRequestDispatcher(addonURL);
			inc.include(request, response);
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/views/outline/footer/main.jsp");
		rd.include(request, response);
	}
	
	/**
	 * 헤더, 푸터를 출력 할지 결정 
	 * 
	 * @param request
	 * @return
	 */
  	private boolean isPrintOk(ServletRequest request) {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest)request;
			
			/** 정적 경로 제외 S */
			String URI = req.getRequestURI();
			for (String dir : staticDirs) {
				if (URI.indexOf("/" + dir) != -1) { // 정적 경로가 포함되어 있으면 false
					return false;
				}
			}
			/** 정적 경로 제외 E */
			
			String outline = request.getParameter("outline");
			/** 요청 메서드 GET 방식이 아닌 경우 제외 */
			String method = req.getMethod().toUpperCase();
			if ((!method.equals("GET") && outline == null) || (!method.equals("GET") && outline != null && !outline.equals("print"))) {
				return false;
			}
			
			/** 요청 파라미터 중에서 outline = none일때 제외 */
			if (outline != null && outline.equals("none")) {
				return false;
			}
		}
		
		return true;
	}
  	
  	public void destroy() {}
}
