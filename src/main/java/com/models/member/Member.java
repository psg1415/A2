package com.models.member;

import java.sql.*;
import com.models.*;

/**
 * Member bean 클래스 
 *
 */
public class Member extends Dto<Member> {
	private int memNo; // 회원번호
	private String memId; // 회원아이디
	private String memPw; // 비밀번호
	private String memPwHint; // 비밀번호 찾기 힌트
	private String memNm; // 회원명
	private String cellPhone; // 휴대전화번호
	private String address; // 주소
	private String socialType; // 소셜 로그인 가입 채널
	private String socialId; // 소셜 로그인 채널별 회원 구분 ID
	private String regDt; // 가입일시
	private boolean isAdmin; // 관리자여부
	
	public Member() {}
	
	public Member(int memNo, String memId, String memPw, String memPwHint, String memNm, String cellPhone, String address, String socialType, String socialId, String regDt, boolean isAdmin) {
		this.memNo = memNo;
		this.memId = memId;
		this.memPw = memPw;
		this.memPwHint = memPwHint;
		this.memNm = memNm;
		this.cellPhone = cellPhone;
		this.address = address;
		this.socialType = (socialType == null)?"none":socialType;
		this.socialId = socialId;
		this.regDt = regDt;
		this.isAdmin = isAdmin;
	}

	public Member(ResultSet rs) throws SQLException {
		this(
			rs.getInt("memNo"),
			rs.getString("memId"),
			rs.getString("memPw"),
			rs.getString("memPwHint"),
			rs.getString("memNm"),
			rs.getString("cellPhone"),
			rs.getString("address"),
			rs.getString("socialType"),
			rs.getString("socialId"),
			rs.getString("regDt"),
			(rs.getInt("isAdmin") == 1)?true:false
		);
	}
	
	public int getMemNo() {
		return memNo;
	}
	
	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}
	
	public String getMemId() {
		return memId;
	}
	
	public void setMemId(String memId) {
		this.memId = memId;
	}
	
	public String getMemPw() {
		return memPw;
	}
	
	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}
	
	public String getMemPwHint() {
		return memPwHint;
	}
	
	public void setMemPwHint(String memPwHint) {
		this.memPwHint = memPwHint;
	}
	
	public String getMemNm() {
		return memNm;
	}
	
	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}
	
	public String getCellPhone() {
		return cellPhone;
	}
	
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getRegDt() {
		return regDt;
	}
	
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public Member setResultSet(ResultSet rs) throws SQLException {
		return new Member(rs);
	}
}