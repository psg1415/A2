# Diet Shopping Mall :fork_and_knife:
* JSP로 구현한 다이어트 음식 쇼핑몰 (<http://dietshop1.cafe24.com>)
## 개요
### 1. 서비스 내용 
1. 다이어트 음식 카테고리별 링크 구현
2. 장바구니에 원하는 상품을 담을수 있게 구현
3. 마이페이지에서 회원정보 변경

### 2. 적용기술
<img src="https://img.shields.io/badge/JSP-4FC08D?style=for-the-badge&logo=JSP&logoColor=white"> <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/html-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

### 3. 각 페이지별 소개
#### 사이트의 메인 페이지
* 검색
* 로그인 -> 로그인 한 경우, 로그아웃으로 변경
* 카테고리
* 마이페이지
* 장바구니

#### 카테고리 페이지

#### 마이페이지
* 주문내역 확인가능
#### 장바구니
* 상품을 선택해서 삭제, 주문이 가능
* 주문 버튼을 누르면 주문하기 페이지로 이동
#### 로그인
* 편의성을 위해 네이버로 로그인이 가능
* 로그인 후, 일정시간이 경과하면, 자동으로 로그아웃 됨

#### 회원가입
* 비밀번호는 영문자, 숫자, 특수문자를 사용하지 않으면 에러발생
* 핸드폰 번호의 형식과 맞지 않으면 에러발생

#### 주문하기
* 이메일의 형식에 맞지않으면 에러발생
* 주문하기 버튼을 누르면 주문 완료
* 주문하기 -> 쇼핑 계속하기로 변경
## 핵심기술

### 1. 서블릿
* web.xml -> 메인페이지 WebServlet 설정 
```
	<servlet>
		<servlet-name>main</servlet-name>
		<servlet-class>com.controller.MainController</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>main</servlet-name>
		<url-pattern>/index.jsp</url-pattern>
	</servlet-mapping>
```
* 메인페이지 제외 annotation으로 WebServlet 설정 -> main/java/com/controller
```
import javax.servlet.annotation.*;

@WebServlet("/file/*")
public class FileController extends HttpServlet {
....
```
* switch case를 사용 추가 URI WebServlet 설정 
```
// ex
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
...
```
### 2. 페이지 초기화
1. 필터 (main/java/com/filter/CommonFilter.java)
* doFilter() : 헤더와 푸터 설정
```
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		BootStrap.init(request, response); // 사이트 초기화!
		
		if (isPrintOk(request)) {
			printHeader(request, response);
		}
		
		chain.doFilter(request, response); // 헤더(위) 와 푸터(아래) 설정
		
		if (isPrintOk(request)) {
			printFooter(request, response);
		}	
	}
```
* printHeader() : 공통헤드 출력 과 헤더(시멘틱테그-inc폴더) 추가 
```
	private void printHeader(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		RequestDispatcher rd = request.getRequestDispatcher("/views/outline/header/main.jsp");
		rd.include(request, response);
		
		/** 헤더 추가 영역 처리 */
		Config config = Config.getInstance();
		String addonURL = config.getHeaderAddon();
		if (addonURL != null) {
			RequestDispatcher inc = request.getRequestDispatcher(addonURL);
			inc.include(request, response);
		}
	}
```
* printFooter() : 공통푸터 출력과 푸터(시멘틱테그-inc폴더) 추가
```
	private void printFooter(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		
		/** 푸터 추가 영역 처리 */
		Config config = Config.getInstance();
		String addonURL = config.getFooterAddon();
		if (addonURL != null) {
			RequestDispatcher inc = request.getRequestDispatcher(addonURL);
			inc.include(request, response);
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/views/outline/footer/main.jsp");
		rd.include(request, response);
	}
```
* isPrintOk() : 헤더와 푸터의 출력 여부를 결정한다.

2. 사이트 초기화 (main/java/com/core/BootStrap.java)
* init() : 사이트 초기화(doFilter에서 설정)
```
	public static void init(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		/** Req, Res 인스턴스 설정 */
		Req.set(request);
		Res.set(response);
		
		/** 사이트 설정 초기화 */
		Config.init();
		
		/** 로거 초기화 */
		Logger.init();
		
		/** 접속자 정보 로그 */
		Logger.log(request);
		
		/** 로그인 유지 */
		String URI = Req.get().getRequestURI();
		if (URI.indexOf("/resources") == -1) {
			MemberDao.init();
		}
		/** 공통 속성 설정 처리 */
		setAttributes();
	}
```
### 3. 회원가입
* join() : 회원 가입 유효성 검사 
```
public boolean join(HttpServletRequest request) throws Exception {
		
		/**
		 * 회원 가입데이트의 유효성 검사
		 */
		checkJoinData(request);
		
		ArrayList<DBField> bindings = new ArrayList<>();
		String sql = "INSERT INTO member (memId, memPw, memPwHint, memNm, cellPhone, address, socialType, socialId) VALUES (?,?,?,?,?,?,?,?)";
		String memPw = request.getParameter("memPw");
		String hash = "";
		String memPwHint = "";
		String socialType = "none";
		String socialId = "";
		if (socialMember == null) { // 일반회원 -> 비밀번호 해시
			hash = BCrypt.hashpw(memPw, BCrypt.gensalt(10));
			memPwHint = request.getParameter("memPwHint");
		} else { // 소셜 회원 - socialType, socialId
			socialType = socialMember.getSocialType();
			socialId = socialMember.getSocialId();
		}
		
		/** 휴대전화번호 형식 -> 숫자로만 구성 */
		String cellPhone = request.getParameter("cellPhone");
		cellPhone = cellPhone.replaceAll("[^\\d]", ""); // 숫자가 아닌 문자 제거 -> 숫자만 남는다
		
		bindings.add(setBinding("String", request.getParameter("memId")));
		bindings.add(setBinding("String", hash));
		bindings.add(setBinding("String", memPwHint));
		bindings.add(setBinding("String", request.getParameter("memNm")));
		bindings.add(setBinding("String", cellPhone));
		bindings.add(setBinding("String", request.getParameter("address")));
		bindings.add(setBinding("String", socialType));
		bindings.add(setBinding("String", socialId));
		
		int rs  = DB.executeUpdate(sql, bindings);
		if (rs > 0 && socialMember != null) { // 소셜 로그인 성공 -> 로그인 처리 
			SocialLogin sociallogin = SocialLogin.getSocialInstance();
			sociallogin.login();
		}
		System.out.println(rs);
		return (rs > 0)?true:false;
	}
	
	
``` 
* checkJoinData() : 회원 가입 조건에 충족하는지 확인한다. 
```
public void checkJoinData(HttpServletRequest request) throws Exception {
		
		/** 필수 항목 체크 S */
		
		
		String[] required = null;
		if (socialMember == null) {// 일반회원
			required = new String[] {
				"memId//아이디를 입력해 주세요.",
				"memPw//비밀번호를 입력해 주세요.",
				"memPwRe//비밀번호를 확인해 주세요.",
				"memPwHint//비밀번호 힌트를 입력해 주세요.",
				"memNm//회원명을 입력해 주세요.",
				"address//주소를 입력해주세요"
			};
		} else { // 소셜 회원 
			required = new String[] {
				"memId//아이디를 입력해 주세요.",
				"memNm//회원명을 입력해 주세요."
			};
		}
		
		
		for(String re : required) {
			String[] params = re.split("//");
			String value = request.getParameter(params[0]);
			if (value == null || value.trim().equals("")) { // 필수 값이 없는 경우 
				throw new Exception(params[1]);
			}
		}
		
		/** 필수 항목 체크 E */
		/** 아이디 체크 S */
		// 아이디 자리수 체크(8~30)
		// String - length()
		String memId = request.getParameter("memId");
		if (memId.length() < 8 || memId.length() > 30) {
			throw new Exception("아이디는 8~30개 이하로 입력해 주세요.");
		}
		
		// 알파벳 + 숫자로만 구성 
		if (!memId.matches("[a-zA-Z0-9]+")) {
			throw new Exception("아이디는 알파벳과 숫자로만 구성해 주세요.");
		}
		
		// 아이디 중복 체크 
		String[] fields = { "memId" };
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("String", memId));
		int count = DB.getCount("member", fields, bindings);
		if (count > 0) { // 아이디 중복
			throw new Exception("이미 가입된 아이디 입니다.");
		}
		/** 아이디 체크 E */
		
		/** 비밀번호 체크 S */
		if (socialMember == null) { // 소셜 회원가입은 비밀번호 불필요 
			String memPw = request.getParameter("memPw");
			String memPwRe = request.getParameter("memPwRe");
			checkPassword(memPw, memPwRe);
		}
		/** 비밀번호 체크 E */
		
		/** 휴대전화 번호 체크 S */
		String cellPhone = request.getParameter("cellPhone");
		if (cellPhone != null && !cellPhone.trim().equals("")) {
			checkCellPhone(cellPhone);
		}
		/** 휴대전화 번호 체크 E */
	}	
```
* joinController () : 회원가입 조건과 불일치시 회원가입 실패 , 성공시 로그인 페이지로 이동한다
 
``` 
private void joinController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 양식 출력 			
			request.setAttribute("action", "../member/join"); // 양식 처리 경로
			String socialType = "none";
			Member socialMember = SocialLogin.getSocialMember();
			if (socialMember != null) {
				socialType = socialMember.getSocialType();
			}
			
			request.setAttribute("socialType", socialType);
			request.setAttribute("socialMember", socialMember);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/member/form.jsp");
			rd.include(request, response);
		} else { // 양식 처리 
			MemberDao dao = MemberDao.getInstance();
			Member socialMember = SocialLogin.getSocialMember();
			try {
				boolean result = dao.join(request);
				if (!result) { // 가입 실패
					throw new Exception("가입에 실패하였습니다.");
				}
				
				// 가입 성공 -> 로그인페이지
				String redirectUrl = "../member/login";
				if (socialMember != null) { // 소셜 회원 가입은 로그인 처리하므로 메인
					redirectUrl = "../index.jsp";
				}

				go(redirectUrl, "parent");
				
			} catch (Exception e) {
				alert(e);
			}
		}
	}

``` 


### 4. 로그인 
* login() : 아이디와 비밀번호 조회 후 세션처리 
```
public boolean login(String memId, String memPw) throws Exception {
		/**
		 * 1. memId를 통해 회원 정보를 조회
		 * 2. 회원 정보가 조회가 되면(실제 회원 있으면) - 비밀번호를 체크
		 * 3. 비밀번호도 일치? -> 세션 처리(회원 번호 - memNo 세션에 저장)
		 */
		
		Member member = getMember(memId);
		if (member == null) { // memId에 일치하는 회원이 X 
			throw new Exception("가입하지 않은 아이디 입니다.");
		}
		
		// 비밀번호 체크 
		boolean match = BCrypt.checkpw(memPw, member.getMemPw());
		if (!match) { // 비밀번호 불일치
			throw new Exception("비밀번호가 일치하지 않습니다.");
		}
		
		// 세션 처리
		HttpSession session = Req.get().getSession();
		session.setAttribute("memNo", member.getMemNo());
		
		return true;
	}
	
``` 
* loginController () : 로그인 처리 
```

private void loginController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 로그인 양식 
			SocialLogin.clear();
			
			String naverCodeURL = NaverLogin.getInstance().getCodeURL();
			request.setAttribute("naverCodeURL", naverCodeURL);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/member/login.jsp");
			rd.include(request, response);
		} else { // 로그인 처리  
			try {
				MemberDao.getInstance().login(request);
				go("../index.jsp", "parent");
			} catch(Exception e) {
				alert(e);
			}
		}
	}
```
* naverLoginController() : 네이버 소셜 로그인 
```
	private void naverLoginController(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NaverLogin naver = NaverLogin.getInstance();
		try {
			String accessToken = naver.getAccessToken();
			naver.getProfile(accessToken);
			
			/**
			 *  네이버 소셜 채널로 이미 가입이 완료된 경우 -> 로그인 처리 
			 *  가입이 안되어 있는 경우 -> 회원 가입 처리 
			 */
			if (naver.isJoin()) { // 가입되어 있는 경우 
				boolean result = naver.login(); // 로그인
				if (!result) { // 로그인 실패 
					throw new Exception("네이버 아이디 로그인 실패!");
				}
				// 로그인 성공시 메인페이지 이동 
				go("../index.jsp");
			} else { // 미가입
				// 회원 가입 페이지 이동
				go("../member/join");
			}
		} catch (Exception e) {
			alert(e, "../member/login");
		}
	}
```

### 5. 상품관리 
* 관리자 페이지로 상품관리
* DB 변수 isAdmin이 1일때 관리자 권한 부여
1. 서블릿 (main/java/com/controller/AdminController.java)
```
@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
....
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 관리자 접근 권한 체크 */
		if (!MemberDao.isAdmin()) {
			alert("접근권한이 없습니다.", -1);
			return;
		}
		
		this.request = request;
		this.response = response;
		
		httpMethod = (String)request.getAttribute("httpMethod"); // 요청 메서드 
		
		dao = GoodsAdminDao.getInstance();
		
		String rootURL = (String)request.getAttribute("rootURL");
		String URI = request.getRequestURI().replace(rootURL, "");
		String[] URIs = URI.split("/");
		
		boolean isPageExists = false; // 페이지가 있는지 여부 
		if (URIs.length >= 3) {
			String type = URIs[URIs.length - 2];
			String mode = URIs[URIs.length - 1];
			
			isPageExists = checkPageExists(type, mode); // 페이지 존재 여부 체크 
			if (isPageExists) {
				if (type.equals("goods")) { // 상품관리
					/** 상품관리 서브 메뉴 */
					RequestDispatcher sub = request.getRequestDispatcher("/views/admin/goods/_sub.jsp");
					sub.include(request, response);
					
					switch(mode) {
						case "list" :  // 상품 목록 
							goodsListController();
							break;
						case "add" : // 상품 추가 
							goodsAddController();
							break;
						case "edit" : // 상품 수정 
							goodsEditController();
							break;
						case "delete" : // 상품 삭제
							goodsDeleteController();
							break;
						case "cate" : // 상품 분류관리
							goodsCateController();
							break;
					}
					
				} else if (type.equals("order")) { // 주문관리 
					switch (mode) {
						case "list":
							orderListController();
							break;
						case "view":
							orderViewController();
							break;
					}
				}
				
			} // endif 
			
			
		}
		
		if (!isPageExists) { // 페이지가 없는 경우 상품관리로 이동 
			String url = request.getServletContext().getContextPath() + "/admin/goods/list";
			go(url);
			
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
```
2. 상품 목록 
ㄱ. Model(GoodsDao.java) 
* DB goods 테이블 goodscate 레프트조인, 진열순서(listOrder)기준 내림차순으로 리스트 출력
```
	public ArrayList<Goods> getList(int cateCd) {
		return dao.getList(cateCd);
	}
	
	public ArrayList<Goods> getList(String cateCd) {
		int ccd = (cateCd == null)?0:Integer.valueOf(cateCd);
		return getList(ccd);
	}
	
	public ArrayList<Goods> getList() {
		return getList(0);
	}
```
ㄴ. View(views/admin/goods/list.jsp)
* taglib을 사용 리스트 출력
```
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='mtitle'>상품목록</div>

<c:if test="${categories != null}">
<div class='category_tab'>
	<a href='../goods/list' class='tab${(cateCd == null)?" on":""}'>전체목록</a>
	<c:forEach var="item" items="${categories}">
		<a href='?cateCd=${item.cateCd}' class='tab${(cateCd != null && cateCd == item.cateCd)?" on":""}'>${item.cateNm}</a>
	</c:forEach>
</div>
</c:if>

<form method="post" action="../goods/list" target="ifrmHidden" autocomplete="off">
	<table class="table-rows">
		<thead>
			<tr>
				<th width='20'><input type="checkbox" class="js_checkall" data-target-name="goodsNo"></th>
				<th width='80'>이미지</th>
				<th width='100'>상품번호</th>
				<th width='150'>분류명</th>
				<th>상품명</th>
				<th width='120'>판매가</th>
				<th width='100''>진열가중치</th>
				<th width='130'>등록일시</th>
				<th width='200'></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${list}">
			<tr>
				<td align='center'>
					<input type="checkbox" name="goodsNo" value="${item.goodsNo}">
				</td>
				<td align='center'>
					<c:if test="${item.listImageUrl != null}">
						<a style="width: 70px; height: 70px; background:url('${item.listImageUrl}') no-repeat center center; background-size: cover; display: block" href='${item.listImageUrl}' target='_blank'></a>
					</c:if>
				</td>
				<td align='center'>${item.goodsNo}</td>
				<td align='center'>${item.cateNm}</td>
				<td>
					<a href='../../goods/view?goodsNo=${item.goodsNo}' target='_blank'>${item.goodsNm}</a>
				</td>
				<td align='center'><fmt:formatNumber value="${item.goodsPrice}" />원</td>
				<td>
					<input type="number" name="listOrder_${item.goodsNo}" value="${item.listOrder}">
				</td>
				<td align='center'>
					<fmt:parseDate var="date" value="${item.regDt}" pattern="yyyy-MM-dd HH:mm:ss" />
					<fmt:formatDate value="${date}" pattern="yy.MM.dd HH:mm" />
				</td>
				<td>
					<a class='sbtn1' href='../goods/edit?goodsNo=${item.goodsNo}'>상품수정</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<c:if test="${list != null && list.size() > 0}">
	<div class='table-action'>
		<select name='mode'>
			<option value='update'>수정</option>
			<option value='delete'>삭제</option>
		</select>
		<input type="submit" value="처리하기" onclick="return confirm('정말 처리하시겠습니까?');" class='sbtn2'>
	</div>
	</c:if>
</form>
```
ㄷ. Controller(java/com/controller/AdminController.java) 
* 서블릿 admin/goods/list 설정, setAttribute설정
```
	/** 
	 * 상품 목록 
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void goodsListController() throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 상품 목록 양식 
			String cateCd = request.getParameter("cateCd");
			ArrayList<Goods> list = dao.getList(cateCd); // 상품목록 
			int total = DB.getCount("goods"); // 등록 상품 개수 
			ArrayList<Category> categories = dao.getCategories(false);
			
			request.setAttribute("list", list);
			request.setAttribute("total", total);
			request.setAttribute("categories", categories);
			request.setAttribute("cateCd", cateCd);
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/list.jsp");
			rd.include(request, response);
		} else { // 상품 목록 수정, 삭제 처리 
			try {
				String mode = request.getParameter("mode");
				if (mode != null && mode.equals("delete")) { // 상품삭제 
					dao.deleteList(request);
				} else { // 상품 수정 
					dao.editList(request);
				}
			} catch (Exception e) {
				alert(e);
			}
			
		}
	}
```
3. 상품등록
ㄱ.Model(java/com/models/goods/GoodsAdminDao.java)
*checkFormData로 유효성검사
```
	private void checkFormData(HttpServletRequest request) throws Exception {
		String[] required = {
			"gid/잘못된 접근입니다.",
			"goodsNm/상품명을 입력하세요.",
			"goodsPrice/판매가를 입력하세요"
		};
		for(String s : required) {
			String[] param = s.split("/");
			String value = request.getParameter(param[0]);
			if (value == null || value.trim().equals("")) {
				throw new Exception(param[1]);
			}
		}
	}
```
* 데이터 바인딩(상품 등록처리)
```
	public boolean add(HttpServletRequest request) throws Exception {
		/** 유효성 검사 */
		checkFormData(request);
		
		String sql = "INSERT INTO goods (gid, goodsNm, goodsPrice, cateCd, goodsExplain) VALUES (?,?,?,?,?)";
		ArrayList<DBField> bindings = new ArrayList<>();
		String cateCd = request.getParameter("cateCd");
		cateCd = (cateCd == null || cateCd.trim().equals(""))?"0":cateCd;
		
		String gid = request.getParameter("gid");
		bindings.add(setBinding("Long", gid));
		bindings.add(setBinding("String", request.getParameter("goodsNm")));
		bindings.add(setBinding("Integer", request.getParameter("goodsPrice")));
		bindings.add(setBinding("Integer", cateCd));
		bindings.add(setBinding("String", request.getParameter("goodsExplain")));
		
		int rs = DB.executeUpdate(sql, bindings);
		if (rs > 0) {
			FileDao.getInstance().updateFinish(gid);
		}
		
		return (rs > 0)?true:false;
	}
```
ㄴ. View (views/admin/goods/form.jsp)
*태그립, ckeditor 사용하여 상품 추가 -> 추가 완료되면 goods/list로 이동
```
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="${rootURL}/resources/js/ckeditor/ckeditor.js"></script>
<div class='mtitle'>
	<c:choose>
		<c:when test="${goods == null}">상품등록</c:when>
		<c:otherwise>상품수정</c:otherwise>
	</c:choose>
</div>
<form method="post" action="../goods/${mode}" target="ifrmHidden" autocomplete="off">
	<c:if test="${goods != null}">
		<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
	</c:if>
	<input type="hidden" name="gid" value="${(goods == null)?gid:goods.gid}">
	<table class="table-cols">
		<tr>
			<th>상품분류</th>
			<td>
				<select name="cateCd">
					<option value="">- 선택하세요 -</option>
					<c:forEach var="item" items="${categories}">
						<option value="${item.cateCd}"${(goods.cateCd == item.cateCd)?" selected":""}>${item.cateNm}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>상품명</th>
			<td>
				<input type="text" name="goodsNm" value="${goods.goodsNm}">
			</td>
		</tr>
		<tr>
			<th>판매가</th>
			<td>
				<input type="number" name="goodsPrice" value="${goods.goodsPrice}">
			</td>
		</tr>
		<tr>
			<th>상품이미지(메인)</th>
			<td>
				<input type="file" class="file_upload" data-file-code="goods_main" data-is-image-only="1">
				<div class="uploaded_images">
				<c:if test="${goods.mainImages != null}">
					<c:forEach var="item" items="${goods.mainImages}">
						<span class="file_item" data-idx='${item.idx}'>	
							<i class="xi-close-min remove"></i>
							<img src='${item.uploadUrl}' width='50'>
						</span>
					</c:forEach>
				</c:if>
				</div>
			</td>
		</tr>
		<tr>
			<th>상품이미지(목록)</th>
			<td>
				<input type="file" class="file_upload" data-file-code="goods_list" data-is-image-only="1">
				<div class="uploaded_images">
				<c:if test="${goods.listImages != null}">
					<c:forEach var="item" items="${goods.listImages}">
						<span class="file_item" data-idx='${item.idx}'>	
							<i class="xi-close-min remove"></i>
							<img src='${item.uploadUrl}' width='50'>
						</span>
					</c:forEach>
				</c:if>
				</div>
			</td>
		</tr>
		<tr>
			<th>상세설명</th>
			<td>
				<textarea name="goodsExplain" id="goodsExplain">${goods.goodsExplain}</textarea>
				이미지 첨부
				<input type="file" class="file_upload"	data-file-code="goods_explain" data-is-image-only="1">		
				<div class="uploaded_images">
				<c:if test="${goods.explainImages != null}">
					<c:forEach var="item" items="${goods.explainImages}">
						<span class="file_item" data-idx='${item.idx}'>	
							<i class="xi-close-min remove"></i>
							<img src='${item.uploadUrl}' width='50'>
						</span>
					</c:forEach>
				</c:if>
				</div>
			</td>
		</tr>
	</table>
	<div class='table-btn'>
		<input type="submit" value="${(goods == null)?'등록하기':'수정하기'}" class='btn1'>
	</div>
</form>

<script>
function uploadFileCallback(fileCode, data) {
	if (fileCode == 'goods_explain') { // 상세설명 에디터 첨부 처리
		const html = "<img src='" + data.uploadUrl +"'>";
		CKEDITOR.instances.goodsExplain.insertHtml(html);
	}
}
</script>
```
ㄷ. Controller(java/com/controller/AdminController.java) 
* 서블릿 admin/goods/add 설정, setAttribute설정
```
	private void goodsAddController() throws ServletException, IOException {
		if (httpMethod.equals("GET")) { // 상품 등록 양식
			/** action 모드 */
			request.setAttribute("mode", "add");
			
			/** 그룹 ID 생성 */
			long gid = System.currentTimeMillis();
			request.setAttribute("gid", gid);
			
			/** 분류 목록 */
			ArrayList<Category> categories = dao.getCategories(false);
			request.setAttribute("categories", categories);
			
			
			RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/form.jsp");
			rd.include(request, response);
		} else { // 상품 등록 처리 
			try {
				dao.add(request);
				go("../goods/list", "parent");
			} catch (Exception e) {
				alert(e);
			}
		}
	}
```
3. 상품수정
ㄱ.Model(java/com/models/goods/GoodsAdminDao.java)
* 상품유무 유효성 검사 (상품넘버가 없으면 throw)
```
	String goodsNo = request.getParameter("goodsNo");
	if (goodsNo == null || goodsNo.trim().equals("")) {
		throw new Exception("잘못된 접근입니다.");
	}
```
*checkFormData로 유효성검사
```
	private void checkFormData(HttpServletRequest request) throws Exception {
		String[] required = {
			"gid/잘못된 접근입니다.",
			"goodsNm/상품명을 입력하세요.",
			"goodsPrice/판매가를 입력하세요"
		};
		for(String s : required) {
			String[] param = s.split("/");
			String value = request.getParameter(param[0]);
			if (value == null || value.trim().equals("")) {
				throw new Exception(param[1]);
			}
		}
	}
```
* 수정할 상품 유무 유효성 검사
```
	Goods goods = get(goodsNo);
	if (goods == null) {
		throw new Exception("수정할 상품이 존재하지 않습니다.");
	}
```
* 데이터 바인딩(상품 수정처리)
```
	String sql = "UPDATE goods SET goodsNm = ?, goodsPrice = ?, cateCd = ?, goodsExplain = ? WHERE goodsNo = ?";
	ArrayList<DBField> bindings = new ArrayList<>();
	String cateCd = request.getParameter("cateCd");
	cateCd = (cateCd == null || cateCd.trim().equals(""))?"0":cateCd;
	
	bindings.add(setBinding("String", request.getParameter("goodsNm")));
	bindings.add(setBinding("Integer", request.getParameter("goodsPrice")));
	bindings.add(setBinding("Integer", cateCd));
	bindings.add(setBinding("String", request.getParameter("goodsExplain")));
	bindings.add(setBinding("Integer", goodsNo));
	int rs = DB.executeUpdate(sql, bindings);
	if (rs > 0) {
		FileDao.getInstance().updateFinish(goods.getGid());
	}
	
	return (rs > 0)?true:false;
```
ㄴ. View (views/admin/goods/form.jsp)
*태그립, ckeditor 사용하여 상품 추가 -> 수정 완료되면 goods/list로 이동

ㄷ. Controller(java/com/controller/AdminController.java) 
* 서블릿 admin/goods/edit 설정, setAttribute설정, 수정처리후 부모창으로 새로고침
```
	private void goodsEditController() throws ServletException, IOException {
		try {
			if (httpMethod.equals("GET")) { // 상품 수정 양식
				String goodsNo = request.getParameter("goodsNo");
				if (goodsNo == null || goodsNo.trim().equals("")) {
					throw new Exception("잘못된 접근입니다.");
				}
				
				/** 상품 등록 정보 */
				Goods goods = dao.get(goodsNo);
				if (goods == null) {
					throw new Exception("등록되지 않은 상품입니다.");
				}
				request.setAttribute("goods", goods);
				
				/** action 모드 */
				request.setAttribute("mode", "edit");
				
				/** 분류 목록 */
				ArrayList<Category> categories = dao.getCategories(false);
				request.setAttribute("categories", categories);
				
				
				RequestDispatcher rd = request.getRequestDispatcher("/views/admin/goods/form.jsp");
				rd.include(request, response);
			} else { // 상품 수정 처리 
				dao.edit(request);
				alert("수정되었습니다.");
				reload("parent");
			}
		} catch (Exception e) {
			if (httpMethod.equals("GET")) {
				alert(e, -1);
			} else {
				alert(e);
			}
		}
	}
```
4. 상품 삭제 -> Model(java/com/models/goods/GoodsAdminDao.java)
* 상품번호로 삭제할 상품 선택 삭제
```
	public void deleteList(HttpServletRequest request) throws Exception {
		String[] goodsNos = request.getParameterValues("goodsNo");
		if (goodsNos == null) {
			throw new Exception("삭제할 상품을 선택하세요.");
		}
		
		for(String goodsNo : goodsNos) {
			delete(goodsNo);
		}
	}
```
### 6. 주문관리
* 관리자 페이지로 상품관리
* DB 변수 isAdmin이 1일때 관리자 권한 부여
1. 서블릿 (main/java/com/controller/AdminController.java)
```
@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
....
				} else if (type.equals("order")) { // 주문관리 
					switch (mode) {
						case "list":
							orderListController();
							break;
						case "view":
							orderViewController();
							break;
					}
				}
			} // endif 		
		}
		
		if (!isPageExists) { // 페이지가 없는 경우 상품관리로 이동 
			String url = request.getServletContext().getContextPath() + "/admin/goods/list";
			go(url);
			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
```
2. Model (java/com/models/order/OrderDao.java)
* 주문 유효성 검사
```
private void checkApplyData(HttpServletRequest request) throws Exception {
	
	String[] idxes = request.getParameterValues("idx");
	if (idxes == null || idxes.length == 0) {
		throw new Exception("잘못된 접근입니다.");
	}
	
	String[] required = {
			"orderName/주문자명을 입력하세요.",
			"orderCellPhone/주문자 휴대전화번호를 입력하세요.",
			"orderEmail/주문자 이메일주소를 입력하세요.",
			"receiverName/수령자명을 입력하세요.",
			"receiverCellPhone/수령자 휴대전화번호를 입력하세요.",
			"receiverZonecode/주소를 입력하세요.",
			"receiverAddress/주소를 입력하세요.",
			"receiverAddressSub/주소를 입력하세요."
	};
	for(String s : required) {
		String[] param = s.split("/");
		String value = request.getParameter(param[0]);
		if (value == null || value.trim().equals("")) {
			throw new Exception(param[1]);
		}
	}
			/** 휴대전화번호 형식 체크 */
		String pattern = "01[016789][0-9]{3,4}[0-9]{4}";
		String orderCellPhone = request.getParameter("orderCellPhone").replaceAll("[^0-9]", "");
		if (!orderCellPhone.matches(pattern)) {
			throw new Exception("휴대전화번호 양식이 아닙니다.");		
		}
		
		String receiverCellPhone = request.getParameter("receiverCellPhone").replaceAll("[^0-9]", "");
		if (!receiverCellPhone.matches(pattern)) {
			throw new Exception("휴대전화번호 양식이 아닙니다.");
		}
	}
```

* 주문 추가
ㄱ. 주문상품 추가 쿼리문
```
	String sql = "INSERT INTO ordergoods (orderNo, goodsNo, goodsNm, goodsCnt) VALUES (?,?,?,?)";
	for(Cart item : items) {
		ArrayList<DBField> bindings = new ArrayList<>();
		bindings.add(setBinding("Long", orderNo));
		bindings.add(setBinding("Integer", item.getGoodsNo()));
		bindings.add(setBinding("String", item.getGoodsInfo().getGoodsNm()));
		bindings.add(setBinding("Integer", item.getGoodsCnt()));
		executeUpdate(sql, bindings);
	}
```
ㄴ. 주문서 추가 쿼리문
```
	Member member = (Member)request.getAttribute("member");
	String orderCellPhone = request.getParameter("orderCellPhone").replaceAll("[^0-9]", "");
	String receiverCellPhone = request.getParameter("receiverCellPhone").replaceAll("[^0-9]", "");
	String sql2 = "INSERT INTO `order` (orderNo,memNo,orderName,orderCellPhone,orderEmail, receiverName,`receiverCellPhone`,receiverZonecode,receiverAddress,receiverAddressSub,settlePrice) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	ArrayList<DBField> bindings = new ArrayList<>();
	bindings.add(setBinding("Long", orderNo));
	bindings.add(setBinding("Integer", member.getMemNo()));
	bindings.add(setBinding("String", request.getParameter("orderName")));
	bindings.add(setBinding("String", orderCellPhone));
	bindings.add(setBinding("String", request.getParameter("orderEmail")));
	bindings.add(setBinding("String", request.getParameter("receiverName")));
	bindings.add(setBinding("String", receiverCellPhone));
	bindings.add(setBinding("String", request.getParameter("receiverZonecode")));
	bindings.add(setBinding("String", request.getParameter("receiverAddress")));
	bindings.add(setBinding("String", request.getParameter("receiverAddressSub")));
	bindings.add(setBinding("Integer", settlePrice));
	int rs = DB.executeUpdate(sql2, bindings);
```
ㄷ. 주문 목록
```
	public ArrayList<Order> getOrders(int memNo) {
		ArrayList<DBField> bindings = new ArrayList<>();
		StringBuilder sb = new StringBuilder("SELECT * FROM `order`");
		if (memNo > 0) {
			sb.append(" WHERE memNo = ?");
			bindings.add(setBinding("Integer", memNo));
		}
		sb.append(" ORDER BY regDt DESC");
		String sql = sb.toString();
		
		ArrayList<Order> list = DB.executeQuery(sql, bindings, new Order());
		
		return list;
	}
```

### 7. 장바구니 
## 7. 장바구니
### add() 장바구니에 담아주는(추가) 기능의  메서드

*  ##### 회원일 경우에만 사용가능한 메서드이며, 회원이 아닐경우 에러를 발생시킴
```
 public boolean add(HttpServletRequest request) throws Exception {
      Member member = (Member)request.getAttribute("member");
      String mode = request.getParameter("mode");
      int isBuy = (mode != null && mode.equals("order"))?1:0;
      String goodsNo = request.getParameter("goodsNo");
      int goodsCnt = 1;
      if (request.getParameter("goodsCnt") != null) {
         goodsCnt = Integer.valueOf(request.getParameter("goodsCnt"));
         if (goodsCnt <= 0) goodsCnt = 1;
      }
      
      if (!MemberDao.isLogin()) {
         throw new Exception("로그인이 필요합니다.");
      }
      
      if (goodsNo == null) {
         throw new Exception("잘못된 접근입니다.");
      }
```
 * #### DB에 데이터를 바인딩해주는 기능을 담당하는 소스로, 이미 상품이 존재한다면 수량 증가, 없다면 추가
```    
      String sql = "SELECT * FROM cart WHERE memNo = ? AND goodsNo = ? AND isBuy = ?";
      ArrayList<DBField> bindings = new ArrayList<>();
      bindings.add(setBinding("Integer", member.getMemNo()));
      bindings.add(setBinding("Integer", goodsNo));
      bindings.add(setBinding("Integer", isBuy));
      Cart cart = DB.executeQueryOne(sql, bindings, new Cart());
      if (cart == null) {
         sql = "INSERT INTO cart (memNo, goodsNo, goodsCnt, isBuy) VALUES (?,?,?,?)";
         bindings = new ArrayList<DBField>();
         bindings.add(setBinding("Integer", member.getMemNo()));
         bindings.add(setBinding("Integer", goodsNo));
         bindings.add(setBinding("Integer", goodsCnt));
         bindings.add(setBinding("Integer", isBuy));
      } else {
         goodsCnt += cart.getGoodsCnt();
         
         sql = "UPDATE cart SET goodsCnt = ? WHERE idx = ?";
         bindings = new ArrayList<DBField>();
         bindings.add(setBinding("Integer", goodsCnt));
         bindings.add(setBinding("Integer", cart.getIdx()));
      }
      
      int rs = executeUpdate(sql, bindings);
      
      return (rs > 0)?true:false;
   }
```  

### delete() 장바구니에 담았던 상품을 선택해서 삭제하는 기능의 메서드
*  #### 장바구니에 추가될 때 바인딩된 idx 번호를 사용해서 구분 

```
   public void delete(HttpServletRequest request) throws Exception {
      String[] idxes = request.getParameterValues("idx");
      if (idxes == null || idxes.length == 0) {
         throw new Exception("삭제할 상품을 선택하세요.");
      }
      
      for (String idx : idxes) {
         ArrayList<DBField> bindings = new ArrayList<>();
         bindings.add(setBinding("Integer", idx));
         String sql = "DELETE FROM cart WHERE idx = ?";
         executeUpdate(sql, bindings);
      }
   }
```

### 장바구니에 담은 제품의 수량 변경 기능의 메서드
* #### 장바구니에 담지 않았던 상품이거나, 수량이 1이상이 아닐경우 에러발생
```
   public boolean updateGoodsCnt(int idx, int goodsCnt) throws Exception {
      
      if (idx == 0) {
         throw new Exception("잘못된 접근입니다.");
      }
      
      if (goodsCnt <= 0) {
         throw new Exception("수량은 1이상 입력하세요.");
      }
      
      ArrayList<DBField> bindings = new ArrayList<>();
      bindings.add(setBinding("Integer", goodsCnt));
      bindings.add(setBinding("Integer", idx));
      String sql = "UPDATE cart SET goodsCnt = ? WHERE idx = ?";
      int rs = DB.executeUpdate(sql, bindings);
      
      return (rs > 0)?true:false;
   }
```
* #### ???
 ```
     public boolean updateGoodsCnt(HttpServletRequest request) throws Exception {
      int idx = 0;
      int goodsCnt = 1;
      if (request.getParameter("idx") != null) {
         idx = Integer.valueOf(request.getParameter("idx"));
      }
      
      if (request.getParameter("goodsCnt") != null) {
         goodsCnt = Integer.valueOf(request.getParameter("goodsCnt"));
      }
      
      return updateGoodsCnt(idx, goodsCnt);
   }
```

### gets() 장바구니 목록을 보여주는 기능의 메서드
* #### 장바구니 DB에 바인딩 되어있던 데이터들을 가져와서 보여줌
```
public ArrayList<Cart> gets(String[] idxes, boolean isBuy) {
      ArrayList<Cart> list = null;
      if (MemberDao.isLogin()) {
         ArrayList<DBField> bindings = new ArrayList<>();
         Member member = (Member)Req.get().getAttribute("member");
         int memNo = member.getMemNo();
         bindings.add(setBinding("Integer", memNo));
         StringBuilder sb = new StringBuilder("SELECT * FROM cart WHERE memNo = ?");
         
         if (idxes == null) {
            int _isBuy = isBuy?1:0;
            sb.append(" AND isBuy = ?");
            bindings.add(setBinding("Integer", _isBuy));
         } else {
            sb.append(" AND idx IN (");
            boolean isFirst = true;
            for(String idx : idxes) {
               if (!isFirst) sb.append(",");
               sb.append("?");
               bindings.add(setBinding("Integer", idx));
               isFirst = false;
            }
            sb.append(")");
         }
         sb.append(" ORDER BY idx");
         String sql = sb.toString();
         
         list = executeQuery(sql, bindings, new Cart());
      }
      
      return list;
   }
   
   public ArrayList<Cart> gets(HttpServletRequest request) {
      boolean isBuy = (request.getParameterValues("idx") == null)?true:false;
      return gets(request.getParameterValues("idx"), isBuy);
   }
   
   public ArrayList<Cart> gets() {
      return gets(null, false);
   }
   
```
### getSettlePrice() 장바구니에 담긴 상품 총 결제 금액을 보여주는 기능의 메서드
* #### 상품들의 price(가격을) 모두 더해서 보여줌

```
   public int getSettlePrice(ArrayList<Cart> items) {
      int total = 0;
      if (items != null && items.size() > 0) {
         for(Cart item : items) {
            Goods goods = item.getGoodsInfo();
            if (goods == null) continue;
            
            total += goods.getGoodsPrice() * item.getGoodsCnt();
         }
      }
      
      return total;
   }
```
