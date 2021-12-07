package com.models.snslogin;

import java.util.*;

import javax.servlet.http.*;

import com.core.*;
import com.models.member.*;

import org.json.simple.*;

/**
 * 소셜 로그인 추상 클래스 
 *
 */
public abstract class SocialLogin {
	
	private static String[] socialTypes = {"naver", "kakao"};
	
	/** 
	 * Access Token을 발급 받기위한 인증 code 발급 URL 생성 
	 * @return
	 */
	public abstract String getCodeURL();
	
	/**
	 * Access Token 발급 
	 * 
	 * @param code
	 * @param state
	 * @return
	 */
	public abstract String getAccessToken(String code, String state) throws Exception;
	public abstract String getAccessToken() throws Exception;
	
	/**
	 * 회원 프로필 조회 API를 통해서 각 소셜 채널별 회원 정보 추출 
	 * 
	 * @param accessToken
	 * @return
	 */
	public abstract Member getProfile(String accessToken);
	
	/**
	 * 소셜 회원 가입이 되어 있는지 여부 체크 
	 * (가입 -> 로그인, 미가입 -> 회원가입) 
	 * @param request
	 * @return
	 */
	public abstract boolean isJoin();
	
	/**
	 * 소셜가입 회원 로그인 처리 
	 * 
	 * @param request
	 * @return
	 */
	public abstract boolean login();
	
	
	/**
	 * 현재 세션에 담겨 있는 소셜 프로필 정보 Member 인스턴스로 반환
	 *    socialType - naver - 네이버 프로필, kakao - 카카오 프로필
	 * @param request
	 * @return
	 */
	public static Member getSocialMember() {
		Member socialMember = null;
		HttpSession session = Req.get().getSession();
		for (String type : socialTypes) {
			if (session.getAttribute(type + "_member") != null) {
				socialMember = (Member)session.getAttribute(type + "_member");
				break;
			}
		}
		return socialMember;
	}
	
	/**
	 * 세션에 있는 프로필 정보로 각 소셜 채널에 맞는 인스턴스 반환 
	 * 
	 * @param request
	 * @return
	 */
	public static SocialLogin getSocialInstance() {
		Member member = getSocialMember();
		String type = "none";
		SocialLogin instance = null;
		if (member != null) {
			type = member.getSocialType();
			switch(type) {
				case "naver" :
					instance = NaverLogin.getInstance();
					break;
				case "kakao" :
					break;
			}
		}
		return instance;
	}
	
	/**
	 * 소셜 프로필 세션 정보 모두 비우기
	 * 
	 * @param request
	 */
	public static void clear() {
		HttpSession session = Req.get().getSession();
		for (String type : socialTypes) {
			session.removeAttribute(type + "_member");
		}
		
		MemberDao.setSocialMember(null);
	}
	
	/**
	 * 원격 HTTP 요청...
	 * 
	 * @param apiURL
	 */
	public JSONObject httpRequest(String apiURL, HashMap<String, String> headers) {
		return HttpRequest.request(apiURL, headers);
	}
	
	public JSONObject httpRequest(String apiURL) {
		return httpRequest(apiURL, null);
	}
}