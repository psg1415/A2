package com.models.snslogin;

import java.util.*;
import java.net.URLEncoder;
import javax.servlet.http.*;

import com.core.*;
import com.models.member.Member;

import org.json.simple.*;

/**
 * 네이버 아이디 로그인 
 *
 */
public class NaverLogin extends SocialLogin {
	
	private String clientId; 
	private String clientSecret;
	private String callbackURL;
	
	private static NaverLogin instance = new NaverLogin();
	private NaverLogin() {}
	
	public static NaverLogin getInstance() {
		if (instance == null) {
			instance = new NaverLogin();
		}
		
		/** 네이버 로그인 설정 처리 -> config.json */
		if (instance.clientId == null || instance.clientSecret == null || instance.callbackURL == null) {
			try {
				HashMap<String, String> conf = (HashMap<String, String>)Config.getInstance().get("NaverLogin");
				instance.clientId = conf.get("clientId");
				instance.clientSecret = conf.get("secret");
				instance.callbackURL = URLEncoder.encode(conf.get("callbackURL"), "UTF-8");
			} catch (Exception e) {
				Logger.log(e);
			}
		}
		
		return instance;
	}
	
	@Override
	public String getCodeURL() {
		HttpServletRequest request = Req.get();
		HttpSession session = request.getSession();
		String state = String.valueOf(System.currentTimeMillis());
		session.setAttribute("state", state);
				
		StringBuilder sb = new StringBuilder();
		sb.append("https://nid.naver.com/oauth2.0/authorize?");
		sb.append("response_type=code&client_id=");
		sb.append(clientId);
		sb.append("&redirect_uri=");
		sb.append(callbackURL);
		sb.append("&state=");
		sb.append(state);
		
		return sb.toString();
	}

	@Override
	public String getAccessToken(String code, String state) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("https://nid.naver.com/oauth2.0/token?");
		sb.append("grant_type=authorization_code&client_id=");
		sb.append(clientId);
		sb.append("&client_secret=");
		sb.append(clientSecret);
		sb.append("&code=");
		sb.append(code);
		sb.append("&state=");
		sb.append(state);
		
		String apiURL = sb.toString();
		JSONObject json = httpRequest(apiURL);
		String accessToken = null;
		if (json != null) {
			if (json.containsKey("access_token")) { // 액세스 토큰 정상 발급 
				accessToken = (String)json.get("access_token");
			} else { // 오류 발생시
				throw new Exception((String)json.get("error_description"));
			}
		}
		
		return accessToken;
	}

	@Override
	public String getAccessToken() throws Exception {
		HttpServletRequest request = Req.get();
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		if (code == null || code.trim().equals("")) {
			throw new Exception("잘못된 요청입니다.");
		}
		
		HttpSession session = request.getSession();
		String _state = null;
		if (session.getAttribute("state") != null) {
			_state = (String)session.getAttribute("state");
		}
		
		if (_state == null || state == null || !_state.equals(state)) {
			throw new Exception("데이터가 변조되었습니다.");
		}
		
		return getAccessToken(code, state);
	}
	
	@Override
	public Member getProfile(String accessToken) {
		
		/**
		 * 접근 토큰(access token)을 전달하는 헤더
		* 다음과 같은 형식으로 헤더 값에 접근 토큰(access token)을 포함합니다. 
		* 토큰 타입은 "Bearer"로 값이 고정돼 있습니다. 
		* Authorization: {토큰 타입] {접근 토큰]
		*/
		Member member = null;
		
		String apiURL = "https://openapi.naver.com/v1/nid/me";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + accessToken);
		JSONObject json = httpRequest(apiURL, headers);
		String resultcode = (String)json.get("resultcode");
		if (resultcode.equals("00")) { // 요청 성공 
			JSONObject res = (JSONObject)json.get("response");
			String memId = null;
			String email = (String)res.get("email");
			if (email != null) {
				memId = email.substring(0, email.lastIndexOf("@"));
			}
			
			member = new Member(
					0, 
					memId,
					null,
					null,
					(String)res.get("name"),
					null,
					null,
					"naver",
					(String)res.get("id"),
					null,
					false
			);	
			
			/**
			 * 네이버 회원프로필 조회 API로 얻어온 회원 정보는 
			 * 페이지가 이동하더라도 데이터 유지 할 필요
			 * (회원가입, 회원 가입처리 ....)
			 * 세션을 통해서 데이터 유지
			 */
			HttpSession session = Req.get().getSession();
			session.setAttribute("naver_member", member);
		}
		
		return member;
	}

	@Override
	public boolean isJoin() {
		HttpServletRequest request = Req.get();
		HttpSession session = request.getSession();
		if (session.getAttribute("naver_member") == null) {
			return false;
		}
		
		Member naverMember = (Member)session.getAttribute("naver_member");
		if (naverMember == null)
			return false;
		
		
		String sql = "SELECT * FROM member WHERE socialType='naver' AND socialId = ?";
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(DB.setBinding("String", naverMember.getSocialId()));
		
		Member member = DB.executeQueryOne(sql, bindings, new Member());
		if (member != null) 
			return true;
		
		return false;
	}

	@Override
	public boolean login() {
		Member member = getMember();
		if (member != null) {
			Req.get().getSession().setAttribute("memNo", member.getMemNo());
			
			return true;
		}
		
		// 프로필 정보 세션 비우기
		SocialLogin.clear();
		
		return false;
	}
	
	/**
	 * 네이버 로그인 회원정보 DB
	 *  
	 * @param request
	 * @return
	 */
	public Member getMember() {
		HttpServletRequest request = Req.get();
		Member member = null;
		HttpSession session = request.getSession();
		if (session.getAttribute("naver_member") != null) {
			Member naverMember = (Member)session.getAttribute("naver_member");
			String socialId = naverMember.getSocialId();
			
			String sql = "SELECT * FROM member WHERE socialType='naver' AND socialId = ?";
			ArrayList<DBField> bindings = new ArrayList<>();
			bindings.add(DB.setBinding("String", socialId));
			member = DB.executeQueryOne(sql, bindings, new Member());
		}
		 
		return member;
	}
}