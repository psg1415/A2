package com.core;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletResponse;

public class Res {
	private static HttpServletResponse response;
	
	public static void set(ServletResponse response) {
		if (response instanceof HttpServletResponse) {
			Res.response = (HttpServletResponse)response;
		}
	}
	
	public static HttpServletResponse get() {
		return response;
	}
}