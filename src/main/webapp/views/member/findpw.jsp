<%@ page contentType="text/html; charset=utf-8" %>

<div class="find2">
	<h2 style="text-align: center;">비밀번호 찾기</h2>
	<form method="post" action="../member/findpw" autocomplete="off" target="ifrmHidden">
		<input type="text" name="memId" placeholder="아이디"><br><br>
		<input type="text" name="memNm" placeholder="회원명"><br><br>
		<input type="text" name="memPwHint" placeholder="비밀번호 찾기 힌트"><br><br>
		<input class="c" type="submit" value="비밀번호 찾기" style="padding: 0;"><br><br>
	</form>
	<div>
		<a href='./findid'>아이디 찾기</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
		<a href='./login'>로그인</a>
	</div>
</div>