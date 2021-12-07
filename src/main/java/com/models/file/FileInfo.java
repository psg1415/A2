package com.models.file;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.models.*;

/**
 * 파일 업로드 정보 Bean
 *
 */
public class FileInfo extends Dto<FileInfo>{
	
	private int idx; // 파일 등록번호 
	private long gid; // 그룹 ID
	private String fileName; // 업로드 원본 파일명 
	private String fileType; // 파일 형식 
	private String fileCode; // 동일 gid 하위 code 
	private boolean isFinish; // 파일 정상 업로드 완료 여부
	private String regDt; // 파일 등록일시 
	private String uploadPath; // 업로드 경로
	private String uploadUrl; // 업로드 Url;
	
	public FileInfo() {}
	
	public FileInfo(int idx, long gid, String fileName, String fileType, String fileCode, boolean isFinish,
			String regDt) {
		this.idx = idx;
		this.gid = gid;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileCode = fileCode;
		this.isFinish = isFinish;
		this.regDt = regDt;
		FileDao dao = FileDao.getInstance();
		uploadPath = dao.getUploadPath(idx);
		uploadUrl = dao.getUploadUrl(idx);
	}
	
	public FileInfo(ResultSet rs) throws SQLException {
		this(
			rs.getInt("idx"),
			rs.getLong("gid"),
			rs.getString("fileName"),
			rs.getString("fileType"),
			rs.getString("fileCode"),
			(rs.getInt("isFinish") == 1)?true:false,
			rs.getString("regDt")
		);
	}
	
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	@Override
	public FileInfo setResultSet(ResultSet rs) throws SQLException {
		return new FileInfo(rs);
	}
}
