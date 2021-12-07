package com.models.file;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import com.core.*;
import static com.core.DB.*;

import org.json.simple.*;

/**
 * 파일 관련 
 *	  1. 파일 업로드 
 *	  2. 파일 삭제 
 *    3. 파일 다운로드
 *    4. 파일 조회(목록, 개별) 
 */
public class FileDao {
	private static FileDao instance = new FileDao();
	
	private FileDao() {}; 
	
	public static FileDao getInstance() {
		if (instance == null) {
			instance = new FileDao();
		}
		
		return instance;
	}
	
	/**
	 * 파일 업로드 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public FileInfo upload(HttpServletRequest request) throws Exception {
		/**
		 * 0. 유효성 검사 - O 
		 * 1. 파일 정보 저장 
		 * 2. 파일 등록 번호 idx -> base64 데이터를 디코딩하여 실제 파일을 저장
		 */
		StringBuilder sb = new StringBuilder();
		String[] required = {"gid","fileName", "fileType", "data"};
		
		boolean isFirst = true;
		for(String s : required) {
			String value = request.getParameter(s);
			if (value == null || value.trim().equals("")) {
				if (!isFirst) sb.append(",");
				sb.append(s);
				isFirst = false;
			}
		}
		
		if (sb.length() > 0) { // 필수 항목 누락 
			throw new Exception("필수 항목 누락 - " + sb.toString());
		}
		
		String sql = "INSERT INTO fileinfo (gid, fileName, fileType, fileCode) VALUES (?,?,?,?)";
		ArrayList<DBField> bindings = new ArrayList<>();
		String fileCode = request.getParameter("fileCode");
		fileCode = (fileCode == null)?"":fileCode;
		bindings.add(setBinding("Long", request.getParameter("gid")));
		bindings.add(setBinding("String", request.getParameter("fileName")));
		bindings.add(setBinding("String", request.getParameter("fileType")));
		bindings.add(setBinding("String", fileCode));
		
		int idx = DB.executeUpdate(sql, bindings, true);
		if (idx == 0) {
			throw new Exception("파일 업로드 실패");
		}
		String uploadPath = getUploadPath(idx);
		byte[] bytes = Base64.getDecoder().decode(request.getParameter("data"));
		try (FileOutputStream fos = new FileOutputStream(uploadPath) ) {
			fos.write(bytes);
		} catch (IOException e) {
			throw new Exception("파일 업로드 실패");
		}
		
		FileInfo info = get(idx);
		
		return info;
	}
	
	/**
	 * 파일 업로드 후 JSON 출력 
	 * 
	 * @param request
	 * @param isJson
	 * @return
	 * @throws Exception
	 */
	public String uploadJson(HttpServletRequest request) throws Exception {
		FileInfo info = upload(request);
		
		/** JSON 출력 옵션일 때 */
		Map<Object, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("idx", info.getIdx());
		map.put("gid", info.getGid());
		map.put("fileName", info.getFileName());
		map.put("fileType", info.getFileType());
		map.put("fileCode", info.getFileCode());
		map.put("isFinish", info.isFinish());
		map.put("regDt", info.getRegDt());
		map.put("uploadPath", info.getUploadPath());
		map.put("uploadUrl", info.getUploadUrl());
		
		return new JSONObject(map).toString();
		
	}
	
	/**
	 * 파일 다운로드 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean download(HttpServletRequest request) throws Exception {
		
		return false;
	}
	
	/**
	 * 파일 정보 조회 
	 * 
	 * @param idx
	 * @return
	 */
	public FileInfo get(int idx) {
		String sql = "SELECT * FROM fileinfo WHERE idx = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Integer", idx));
		FileInfo info = executeQueryOne(sql, bindings, new FileInfo());
	
		return info;
	}
	
	public FileInfo get(String idx) {
		return get(Integer.valueOf(idx));
	}
	
	/**
	 * 파일 목록 
	 * @param gid
	 * @param fileCode 
	 * 
	 * @return
	 */
	public ArrayList<FileInfo> gets(long gid, String fileCode) {
		StringBuilder sb = new StringBuilder();
		ArrayList<DBField> bindings = new ArrayList<>();
		sb.append("SELECT * FROM fileinfo WHERE gid = ? AND isFinish=1");
		bindings.add(setBinding("Long", gid));
		if (fileCode != null) {
			sb.append(" AND fileCode = ?");
			bindings.add(setBinding("String", fileCode));
		}
		
		String sql = sb.toString();
		
		
		ArrayList<FileInfo> list = DB.executeQuery(sql, bindings, new FileInfo());
		
		return list;
	}
	
	public ArrayList<FileInfo> gets(long gid) {
		return gets(gid, null);
	}
	
	public ArrayList<FileInfo> gets(String gid, String fileCode) {
		return gets(Long.valueOf(gid), fileCode);
	}
	
	public ArrayList<FileInfo> gets(String gid) {
		return gets(Long.valueOf(gid), null);
	}
	
	/**
	 * 파일 삭제 
	 * 
	 * @param idx
	 * @return
	 */
	public boolean delete(int idx) {
		
		/**
		 * 1. 파일 정보 삭제 
		 * 2. 파일 삭제 
		 * 
		 */
		FileInfo info = get(idx);
		if (info != null) {
			File file = new File(info.getUploadPath());
			if (file.exists()) {
				file.delete();
			}
			
			String sql = "DELETE FROM fileinfo WHERE idx = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", idx));
			int rs = executeUpdate(sql, bindings);
			if (rs > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean delete(String idx) {
		return delete(String.valueOf(idx));
	}
	
	/**
	 * 파일 삭제 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean delete(HttpServletRequest request) throws Exception {
		String idx = request.getParameter("idx");
		if (idx == null) {
			throw new Exception("필수항목 누락 - idx");
		}
		
		return delete(Integer.valueOf(idx));
	}
	
	/**
	 * 파일 삭제 후 JSON으로 결과 출력 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String deleteJson(HttpServletRequest request) throws Exception {
		boolean result = delete(request);
		
		Map<Object, Object> map = new HashMap<>();
		map.put("success", result);
	
		return new JSONObject(map).toString();
	}
	
	/**
	 * gid로 파일 삭제
	 * 
	 * @param gid
	 */
	public void deleteByGid(long gid) {
		ArrayList<FileInfo> list = gets(gid);
		if (list == null)
			return;
		
		for(FileInfo info : list) {
			File file = new File(info.getUploadPath());
			if (file.exists()) {
				file.delete();
			}
			
			String sql = "DELETE FROM fileinfo WHERE idx = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(setBinding("Integer", info.getIdx()));
			executeUpdate(sql, bindings);
		}
	}
	
	public void deleteByGid(String gid) {
		deleteByGid(Long.valueOf(gid));
	}
	
	/**
	 * 업로드 할 파일 경로
	 * 
	 * @param idx
	 * @return
	 */
	public String getUploadPath(int idx) {
		String rootPath = (String)Req.get().getAttribute("rootPath");
		StringBuilder sb = new StringBuilder(rootPath);
		sb.append(File.separator);
		sb.append("resources");
		sb.append(File.separator);
		sb.append("upload");
		sb.append(File.separator);
		sb.append(idx);
		
		return sb.toString();
	}
	
	/**
	 * 업로드된 파일 URL 
	 * 
	 * @param idx
	 * @return
	 */
	public String getUploadUrl(int idx) {
		String rootURL = (String)Req.get().getAttribute("rootURL");
		StringBuilder sb = new StringBuilder(rootURL);
		sb.append("/resources/upload/");
		sb.append(idx);
		
		return sb.toString();
	}
	
	/**
	 * 파일 업로드 완료 처리 
	 * 
	 * @param gid
	 */
	public void updateFinish(long gid) {
		String sql = "UPDATE fileinfo SET isFinish=1 WHERE gid = ?";
		ArrayList<DBField> binding = new ArrayList<>();
		binding.add(setBinding("Long", gid));
		DB.executeUpdate(sql, binding);
	}
	
	public void updateFinish(String gid) {
		updateFinish(Long.valueOf(gid));
	}
}
