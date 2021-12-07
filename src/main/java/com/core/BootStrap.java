package com.core;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.models.member.*;

public class BootStrap {
    /**
	* 사이트 초기화 
	*
	*/
	public static void init(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		/** Req, Res 인스턴스 설정 */
		Req.set(request);
		Res.set(response);
		
		/** 사이트 설정 초기화 */
		Config.init();
		
		/** 로거 초기화 */
		Logger.init();
		
		/** 접속자 정보 로그 */
		Logger.log(request);
		
		/** 로그인 유지 */
		String URI = Req.get().getRequestURI();
		if (URI.indexOf("/resources") == -1) {
			MemberDao.init();
		}
		
		/** 공통 속성 설정 처리 */
		setAttributes();
	}
	
	/**
	 * 사이트 전역에서 사용되는 공통 속성 설정 
	 *  
	 */
	public static void setAttributes() throws IOException {
		Config config = Config.getInstance();
		HttpServletRequest request = Req.get();
		HttpServletResponse response = Res.get();
		
		/** rootURL */
		String rootURL = request.getServletContext().getContextPath();
		request.setAttribute("rootURL", rootURL);
		
		/** rootPath */
		String rootPath = request.getServletContext().getRealPath(".");
		request.setAttribute("rootPath", rootPath);
		
		/** Request URI */
		String requestURI = request.getRequestURI();
		request.setAttribute("requestURI", requestURI);
		
		/** Request URL */
		String requestURL = request.getRequestURL().toString();
		request.setAttribute("requestURL", requestURL);
		
		/** URI별 추가 CSS */
		request.setAttribute("addCss", config.getCss());
		
		/** URI별 추가 JS */
		request.setAttribute("addScripts", config.getScripts());
		
		/** 사이트 기본 제목 */
		request.setAttribute("pageTitle", config.get("PageTitle"));
		
		/** Environment - development(개발중), production(서비스 중) */
		String env = ((String)config.get("Environment")).equals("production")?"production":"development";
		request.setAttribute("environment", env);
		
		/** CSS, JS 버전 */
		String cssJsVersion = null;
		if (env.equals("development")) {
			cssJsVersion = "?v=" + String.valueOf(System.currentTimeMillis());
		}
		request.setAttribute("cssJsVersion", cssJsVersion);
		
		/** Body 태그 추가 클래스 */
		request.setAttribute("bodyClass",  config.getBodyClass());
		
		/** 요청 메서드 + requestURL, Request Encoding, Response Encoding 설정 */			
		request.setAttribute("httpMethod", request.getMethod().toUpperCase());
		request.setAttribute("requestURL", request.getRequestURL().toString());
			
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
	}
}
