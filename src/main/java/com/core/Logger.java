package com.core;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * 로거 
 *
 */
public class Logger {
	
	/** 로그 레벨 상수 */
	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int NOTICE = 2;
	public static final int WARNING = 3;
	public static final int ERROR = 4;
	public static final int CRITICAL = 5;
	public static final int ALERT = 6;
	public static final int EMERGENCY = 7;
	
	private static final String[] errorLevels = {"debug", "info", "notice", "warning", "error", "critical", "alert", "emergency" };

	
	private static String status; // dev - 개발중(콘솔), service - 서비스 중(파일)
	private static Writer writer; 
	private static String logDir; // 로그 디렉토리
	private static String logPath;// 디렉토리포함 로그 파일명(날짜)
	
	private static boolean preventClosed = false;
	
	/**
	 * 설정 초기화 
	 * 
	 */
	public static void init(String status, String logDir) {
		Logger.status = status;
		Logger.logDir = logDir;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String logFileName = sdf.format(new Date()) + ".log";
		
		Logger.logPath = logDir + File.separator + logFileName;
	}
	
	public static void init(FilterConfig config) {
		init(
			config.getInitParameter("status"),
			config.getInitParameter("logDir")
		);
	}
	
	public static void init() {
		Config config = Config.getInstance();
		logDir = (String)config.get("LogDir");
		status = ((String)config.get("Environment")).equals("production")?"service":"dev";
		init(status, logDir);
	}
	
	
	/** 
	 * 리소스를 로그 기록 후에 닫기 여부 
	 * 
	 * @param preventClosed
	 */
	public static void setPreventClosed(boolean preventClosed) {
		Logger.preventClosed = preventClosed;
	}
	 
	/**
	 * Writer(출력 스트림) 설정 
	 * 
	 * @param writer
	 */
	public static void setWriter(Writer writer) {
		Logger.writer = writer;
	}
	
	/**
	 * 바이트 단위 스트림을 Writer(문자단위)으로 설정
	 * 
	 * @param stream
	 */
	public static void setStream(OutputStream stream) {
		Logger.writer = new OutputStreamWriter(stream);
	}
	
	/**
	 * 로그 기록 
	 * 
	 * @param message
	 * @param level 로그 레벨(0~7) - 1 - info
	 */
	public static void log(String message, int level) {
		
		if (level < 0 || level > 7) level = 1; // info가 기본값
		
		BufferedWriter bw = null;
		PrintWriter out  = null;
		try {
			// 별도 설정 Writer가 없는 경우 FileWriter 또는 System.out 설정 
			if (writer == null) {
				if (status.equals("dev")) { // 콘솔 출력 
					setStream(System.out);
				} else { // 파일 출력 
					setWriter(new FileWriter(logPath, true));
				}
			}
			
			bw = new BufferedWriter(writer);
			out = new PrintWriter(bw);
			
			StringBuilder sb = new StringBuilder();
			// 로그 레벨 표기 
			sb.append("[%s]");
			
			// 로그 작성 시간 표기 
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String time = sdf.format(new Date());
			sb.append("[%s]");
			
			// 메세지 표기 
			sb.append("%s%n");
			
			out.printf(sb.toString(), errorLevels[level], time, message);
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (!status.equals("dev") && !preventClosed) {
				try {
					if (out != null) out.close();
					if (bw != null) bw.close();
					if (writer != null) {
						writer.close();
						writer = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
 		}
	}
	
	/**
	 * 로그 기록, level - INFO로 고정 
	 * 
	 * @param message
	 */
	public static void log(String message) {
		log(message, Logger.INFO);
	}
	
	public static void log(StringBuilder sb, int level) {
		log(sb.toString(), level);
	}
	
	public static void log(StringBuffer sb, int level) {
		log(sb.toString(), level);
	}
	
	/**
	 * 로그기록 - 사용자 접속 정보
	 * 
	 * @param request
	 */
	public static void log(ServletRequest request) {
		if (request instanceof HttpServletRequest) {
		/**
		 * 1. 요청 메서드(O)
		 * 2. 요청 URL(O)
		 * 3. 접속 IP(O)
		 * 4. Referrer - 유입 경로 (O)
		 * 5. User Agent -> 요청 헤더 (O)
		 * 6. Accept-Language -> 사용 언어 (O) 
		 */
			HttpServletRequest req = (HttpServletRequest)request;
			StringBuilder sb = new StringBuilder();
			
			String method = req.getMethod();
			String referer = req.getHeader("referer"); // 유입경로
			
			sb.append(method);
			sb.append(" / URL : ");
			sb.append(req.getRequestURL());
			sb.append(" / IP : ");
			sb.append(req.getRemoteAddr());
			if (referer != null) {
				sb.append(" / REF : ");
				sb.append(referer);
			}
			// User Agent(브라우저 접속 정보)
			sb.append(" / UA : ");
			sb.append(req.getHeader("user-agent"));
			
			// 사용 언어 
			sb.append(" / LANG : ");
			sb.append(req.getHeader("accept-language"));
			
			log(sb, Logger.INFO);
		}
	}
	
	/**
	 * 예외, 에러에대한 로그 기록 
	 * 
	 * @param e   /  로그 레벨 ERROR로 고정 
	 */
	public static void log(Throwable e) {
		
		
		log("------------------------------------- Error Stack Start ----------------------------", ERROR);
		log("Message : " + e.getMessage(), ERROR);
		log("Exception : " + e.getClass().getName(), ERROR);
		StackTraceElement[] stacks = e.getStackTrace();
		for (StackTraceElement stack : stacks) {
			StringBuilder sb = new StringBuilder();
			sb.append("ClassName : ");
			sb.append(stack.getClassName());
			sb.append(" / File : ");
			sb.append(stack.getFileName());
			sb.append(" / LINE : ");
			sb.append(stack.getLineNumber());
			sb.append(" / Method : " );
			sb.append(stack.getMethodName());
			log(sb, ERROR);
		}
		
		
		log("------------------------------------- Error Stack END ----------------------------", ERROR);
		log("", ERROR);
	}
}