<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${memId == null}">
		<div class="find1">
			<h2 style="text-align: center;">아이디찾기</h2>
			<form method="post" action="../member/findid" autocomplete="off" >
				<input type="hidden" name="outline" value="print">
				<input type="text" name="memNm" placeholder="회원명"><br><br>
				<input type="text" name="cellPhone" placeholder="핸드폰번호"><br><br>
				<input class="c" type="submit" value="아이디 찾기" style="padding: 0;"><br><br>
			</form>
			<div>
				<a href='./findpw'>비밀번호 찾기</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
				<a href='./login'>로그인</a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<h3>아이디 : <c:out value="${memId}" /></h3>
		<div>
			<div>
				<a href='../member/findid'>다시 찾기</a> 
				<a href='../member/findpw'>비밀번호 찾기</a>
			</div>
			<div class='right'>
				<a href='../index.jsp'>로그인 하기</a>
			</div>
		</div>
	</c:otherwise>
</c:choose>