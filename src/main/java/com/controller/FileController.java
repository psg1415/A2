package com.controller;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

import com.core.*;
import com.models.file.*;

/**
 * 파일 업로드, 다운로드, 삭제 
 *
 */
@WebServlet("/file/*")
public class FileController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String URI = request.getRequestURI();
		String mode = URI.substring(URI.lastIndexOf("/") + 1);
		
		try {
			FileDao dao = FileDao.getInstance();
			switch (mode) {
				/** 파일 업로드 */
				case "upload" : 
					String json = dao.uploadJson(request);
					response.setHeader("Content-Type", "application/json");
					response.getWriter().print(json);
					break;
				/** 파일 다운로드 */
				case "download" :
					dao.download(request);
					break;
				/** 파일 삭제 */
				case "delete" : 
					String djson = dao.deleteJson(request);
					response.setHeader("Content-Type", "application/json");
					response.getWriter().print(djson);
					break;				
			}
		} catch (Exception e) {
			CommonLib.printJson(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}




