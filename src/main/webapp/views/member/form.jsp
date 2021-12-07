<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>
	<c:if test="${socialType != 'none'}">
		<c:choose>
			<c:when test="${socialType == 'naver'}">
				네이버
			</c:when>
			<c:when test="${socialType == 'kakao'}">
				카카오
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${mode == 'address'}">
				배송지주소
			</c:when>
			<c:otherwise>
				아이디로
			</c:otherwise>
		</c:choose>
	</c:if>
	
	<form class="joinFrm" name='frmJoin' id='frmJoin' method="post" action="${action}" target="ifrmHidden" autocomplete="off">
		<div class="join">
			<h2 class="hjoin" style="text-align: center;">
				<c:choose>
					<c:when test="${member == null}">
						회원가입
					</c:when>
					<c:otherwise>
						정보수정
					</c:otherwise>
				</c:choose>
			</h2>
			<c:choose>
				<c:when test="${mode == 'address'}">
					<input type="text" name="address" value="${member.address}">
				</c:when>
			<c:otherwise>
			<c:choose>
				<c:when test="${member == null}">
					<input type="text" name="memId" value="${socialMember.memId}" placeholder="아이디"><br><br>
				</c:when>
				<c:otherwise>
					<c:out value="아이디 : ${member.memId}"/><br><br>
				</c:otherwise>
			</c:choose>
			<c:if test="${socialType == 'none'}">
				<input type="password" name="memPw" placeholder="비밀번호"><br><br>
				<input type="password" name="memPwRe" placeholder="비밀번호 확인"> <br><br>
				<input type="text" name="memPwHint" value="${member.memPwHint}" placeholder="비밀번호 힌트"> <br><br>
			</c:if>
			<c:choose>
				<c:when test="${member == null }">
					<input type="text" name="memNm" maxlength="20" value="${socialMember.memNm}" placeholder="이름"> <br><br>
				</c:when>
				<c:otherwise>
					<input type="text" name="memNm" value="${member.memNm}"><br><br>
				</c:otherwise>
			</c:choose>
			<input type="text" name="cellPhone" value="${member.cellPhone}" placeholder="핸드폰번호"><br><br>
			<input type="text" name="address" value="${member.address}" placeholder="주소"><br><br>
				
				
			
			
			
			<c:choose>
				<c:when test="${member == null}">
					<input class="c" type="submit" value="회원가입" style="padding: 0;"><br><br>
				</c:when>
				<c:otherwise>
					<input type="submit" value="정보수정" style="padding: 0;"><br><br>
				</c:otherwise>
			</c:choose>
			
				<div class="cancle">
					<input class="cancle1" type="reset" value="다시입력" style="padding: 0;">
					<input class="cancle1" class="c" type="button" value="취소" style="padding: 0;" onClick="location.href='${rootURL}/member/login'">
				</div>
			</c:otherwise>
			</c:choose>
		</div>
	</form>
</div>