package com.core;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class HttpRequest {
	/**
	 * 원격 HTTP 요청 
	 *  
	 * @param apiURL - 요청 URL
	 * @param headers - 요청 헤더
	 * @param method - 요청 방식, GET, POST, ....
	 * @param postData - POST(DELETE, PATCH, PUT ...) 전송 데이터 
	 * @return JSONObject
	 */
	public static JSONObject request(String apiURL, HashMap<String, String> headers, String method, HashMap<String, String> postData) {
		JSONObject json = null;
		try {
			URL url = new URL(apiURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			method = (method == null)?"GET":method.toUpperCase(); 
			conn.setRequestMethod(method);
			
			/** method가 POST(DELETE, PUT, PATCH .... )일때 추가 헤더 처리 */
			byte[] sendParams = null;
			if (!method.equals("GET")) {
				if (headers == null) {
					headers = new HashMap<String, String>();
				}
				headers.put("Content-Type", "application/x-www-form-urlencoded");
			
			
				StringBuilder params = new StringBuilder();
				boolean isFirst = true;
				for (Map.Entry<String, String> map : postData.entrySet()) {
					String key = URLEncoder.encode(map.getKey(), "UTF-8");
					String value = URLEncoder.encode(map.getValue(), "UTF-8");
					if (!isFirst) {
						params.append("&");
					}
					params.append(key);
					params.append("=");
					params.append(value);
					isFirst = false;
				}
				
				sendParams = params.toString().getBytes("UTF-8");
				headers.put("Content-Length", String.valueOf(sendParams.length));
			}
			
			/** 요청 헤더 처리 */
			if (headers != null) {
				Iterator<String> ir = headers.keySet().iterator();
				while(ir.hasNext()) {
					String key = ir.next();
					String value = headers.get(key);
					conn.setRequestProperty(key, value);
				}
			}
			
			/** POST 데이터 전송 처리 */
			if (!method.equals("GET") && sendParams.length > 0) {
				conn.setDoOutput(true);
				try(OutputStream out = conn.getOutputStream()) {
					out.write(sendParams);
				} catch (IOException e) {
					Logger.log(e);
				}
			}
			
			InputStream in;
			int code = conn.getResponseCode(); // 200, HttpURLConnection.HTTP_OK
			if (code == HttpURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
			
			StringBuilder sb = new StringBuilder();
			try (
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr)) {
				String line = null;
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				Logger.log(e);
			}finally {
				in.close();
			}
			
			String data = sb.toString();
			if (data != null && !data.trim().equals("")) {
				JSONParser parser = new JSONParser();
				json = (JSONObject)parser.parse(data);
			}
		} catch (Exception e) {
			Logger.log(e);
		}
		
		return json;
	}
	
	public static JSONObject request(String apiURL) {
		return request(apiURL, null, "GET", null);
	}
	
	public static JSONObject request(String apiURL, HashMap<String, String> headers) {
		return request(apiURL, headers, "GET", null);
	}
	
	public static JSONObject request(String apiURL, String method) {
		return request(apiURL, null, method, null);
	}
	
	public static JSONObject request(String apiURL, String method, HashMap<String, String> postData) {
		return request(apiURL, null, method, postData);
	}
}