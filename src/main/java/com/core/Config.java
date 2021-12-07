package com.core;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * 사이트 설정 관리 
 * 
 */
public class Config {
	
	private static HttpServletRequest request;
	private static String requestURI;
	
	private static Config instance = null;
	
	
	private HashMap<String, Object> conf = new HashMap<>(); // 설정 담기
	
	/** 
	* src/main/config/config.json을 읽어서 설정 HashMap
	*  
	*/
	private Config() {
		requestURI = request.getRequestURI();
		String configPath = request.getServletContext().getRealPath(".");
		configPath += File.separator + ".." + File.separator + "config" + File.separator + "config.json";
		
		File file = new File(configPath);
		if (!file.exists()) { // 설정 파일 존재 X 중지 
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		try (FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr)) {
			String line = null;
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			JSONObject  json = (JSONObject)new JSONParser().parse(sb.toString());
			Iterator<String> ir = json.keySet().iterator();
			while(ir.hasNext()) {
				String key = ir.next();
				conf.put(key, json.get(key));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 싱글톤 인스턴스 반환
	 * 
	 * @return
	 */

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		
		return instance;
	}
	
	public static Config getInstance(boolean isNew) {
		if (instance == null || isNew) {
			instance = new Config();
		}
		
		return instance;
	}
	
	public static void init() {
		request = Req.get();
		Config.requestURI = request.getRequestURI();	
	}
	
	/**
	 * 설정 조회 
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		if (!conf.containsKey(key)) {
			return null;
		}
		
		Object value = conf.get(key);
		// 설정 값이 문자열이면 그냥 반환 
		if (value instanceof String) {
			return value;
		}
		
		// 아니면 JSONObject -> HashMap -> Object
		HashMap<String, String> map = new HashMap<>();
		JSONObject json = (JSONObject)value;
		Iterator<String> ir = json.keySet().iterator();
		while(ir.hasNext()) {
			String k = ir.next();
			String v = (String)json.get(k);
			map.put(k, v);
		}
		
		return map;
	}
	
	/**
	 * URI 패턴에 따른 CSS 파일 목록 
	 * 
	 * @return
	 */
	public HashSet<String> getCss() {
		
		HashSet<String> list = new HashSet<>();
		HashMap<String, String> css = (HashMap<String, String>)get("css");
		Iterator<String> ir = css.keySet().iterator();
		while(ir.hasNext()) {
			String URI = ir.next();
			if (requestURI.indexOf(URI) != -1) {
				StringTokenizer st = new StringTokenizer(css.get(URI), "||");
				while(st.hasMoreTokens()) {
					list.add(st.nextToken());
				}
			}
		}
		
		return list;
	}
	
	/**
	 * URI 패턴에 따른 JS 파일 목록 
	 * @return
	 */
	public HashSet<String> getScripts() {
		HashSet<String> list = new HashSet<>();
		HashMap<String, String> js = (HashMap<String, String>)get("js");
		Iterator<String> ir = js.keySet().iterator();
		while(ir.hasNext()) {
			String URI = ir.next();
			if (requestURI.indexOf(URI) != -1) { // URI 포함되어 있다면 
				StringTokenizer st = new StringTokenizer(js.get(URI), "||");
				while(st.hasMoreTokens()) {
					list.add(st.nextToken());
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 헤더, 푸터 addon 경로 
	 * 
	 * @param addonType 
	 * 				 - AddonHeader - 헤더 추가 영역, AddonFooter - 푸터 추가영역 
	 * @return
	 */
	public String getAddon(String addonType) {
		String addon = null;
		String commonAddon = null;
		
		HashMap<String, String> addons = (HashMap<String, String>)get(addonType);
		Iterator<String> ir = addons.keySet().iterator();
		while(ir.hasNext()) {
			String URI = ir.next();
			if (URI.equals("*")) { // 공통
				commonAddon = addons.get(URI);
			} else { // 별도 패턴 -> 체크 
				if (requestURI.indexOf(URI) != -1) {
					addon = addons.get(URI);
				}
			}
		}
		
		// 헤더 추가 영역 없는 경우 
		if (addon != null && addon.equals("none")) {
			return null;
		}
		
		String type = addonType.equals("AddonHeader")?"header":"footer";
		
		StringBuilder sb = new StringBuilder();
		sb.append("/views/outline/");
		sb.append(type);
		sb.append("/inc/");
		
		if (addon == null) { // 공통 
			sb.append(commonAddon);
		} else { 
			sb.append(addon);
		}
		sb.append(".jsp");
		
		return sb.toString();
	}
	
	/**
	 * 사이트 헤더 영역에 추가될 jsp 경로
	 * 
	 * @return
	 */
	public String getHeaderAddon() {
		return getAddon("AddonHeader");
	}
	
	/**
	 * 사이트 푸터 영역에 추가될 jsp 경로
	 * 
	 * @return
	 */
	public String getFooterAddon() {
		return getAddon("AddonFooter");
	}
	
	/**
	 * 페이지 URI를 가지고 body에 추가할 클래스명
	 * 	
	 * @return
	 */
	public String getBodyClass() {
		String rootURL = request.getServletContext().getContextPath();
		String URI = requestURI.replace(rootURL, "").replace("index.jsp", "");
		if (URI.equals("/")) { // 메인페이지
			return "body-main body-index";
		}
		
		StringTokenizer st = new StringTokenizer(URI, "/");
		StringBuilder sb = new StringBuilder();
		String prevClassNm = null;
		
		while(st.hasMoreTokens()) {
			String classNm = st.nextToken();
			if (classNm != null && !classNm.equals("")) {
				sb.append("body-");
				if (prevClassNm != null) {
					sb.append(prevClassNm);
					sb.append("-");
				}
				
				sb.append(classNm);
				sb.append(" ");
				prevClassNm = classNm;
			}
		}
			
		return sb.toString();
	}
}
