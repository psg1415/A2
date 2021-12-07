package com.core;

import java.io.*;
import java.util.*;
import org.json.simple.*;

public class CommonLib {
	
	/**
	 * alert 메세지 출력
	 * 
	 * @param msg 알림메시지 
	 * @param url 알림 메세지 후 이동 URL
	 * @param target  - 기본값은 self, parent - 부모창 
	 */
	public static void alert(String msg, String url, String target) {
		try {
			PrintWriter out = Res.get().getWriter();
			out.printf("<script>alert('%s');</script>", msg);
			if (url != null) {
				go(url, target);
			}
		} catch (IOException e) {}
	}
	
	public static void alert(Throwable e, String url, String target) {
		alert(e.getMessage(), url, target);
		Logger.log(e);
	}
	
	public static void alert(String msg, String url) {
		alert(msg, url, "self");
	}
	
	public static void alert(Throwable e, String url) {
		alert(e, url, "self");
	}
	
	public static void alert(String msg) {
		alert(msg, null, "self");
	}
	
	public static void alert(Throwable e) {
		alert(e, null, "self");		
	}
	
	/**
	 * 메세지 출력 후 뒤로가기 또는 앞으로 가기 처리 
	 * 
	 * @param out
	 * @param msg
	 * @param step
	 * @param target
	 */
	public static void alert (String msg, int step, String target) {
		try {
			PrintWriter out = Res.get().getWriter();
			out.printf("<script>alert('%s');</script>", msg);
			if (step != 0) {
				back(step, target);
			}
		} catch (IOException e) {}
	}
	
	public static void alert(Throwable e, int step, String target) {
		alert(e.getMessage(), step, target);
		Logger.log(e);
	}
	
	public static void alert(String msg, int step) {
		alert(msg, step, "self");
	}
	
	public static void alert(Throwable e, int step) {
		alert(e, step, "self");
	}
	
	/**
	 * 링크 이동 (자바스크립트 location.replace ... )
	 * 
	 * @param out
	 * @param url
	 * @param target
	 */
	public static void go(String url, String target) {
		try {
			PrintWriter out = Res.get().getWriter();
			target = (target == null || target.trim().equals(""))?"self":target;
			String location = "<script>%s.location.replace('%s');</script>";
			out.printf(location, target, url);
		} catch (IOException e) {}
	}
	
	public static void go(String url) {
		go(url, "self");
	}
	
	/**
	 * 방문기록 이동(history.go...)
	 * 
	 * @param out
	 * @param step
	 * @param target
	 */
	public static void back(int step, String target) {
		try {
			PrintWriter out = Res.get().getWriter();
			step = (step == 0)?-1:step;
			target = (target == null || target.trim().equals(""))?"self":target;
			String script = "<script>%s.history.go(%d);</script>";
			out.printf(script, target, step);
		} catch (IOException e) {}
	}
	
	public static void back(PrintWriter out, int step) {
		back(step, "self");
	}
	
	public static void back(PrintWriter out) {
		back(-1, "self");
	}
	
	/**
	 * 페이지 새로고침 (location.reload()) 
	 * @param target
	 */
	public static void reload(String target) {
		try {
			PrintWriter out = Res.get().getWriter();
			out.printf("<script>%s.location.reload();</script>", target);
		} catch (Exception e) {}
	}
	
	public static void reload() {
		reload("self");
	}
	
	/**
	 * 예외를 JSON 형태로 출력 
	 * 
	 * @param e
	 */
	public static void printJson(Throwable e) {
		Logger.log(e);
		try {
			Res.get().setHeader("Content-Type", "application/json");
			PrintWriter out = Res.get().getWriter();
			HashMap<Object, Object> map = new HashMap<>();
			map.put("success", false);
			map.put("message", e.getMessage());
			JSONObject json = new JSONObject(map);
			out.print(json);
		} catch (IOException e2) {}
	}
}
