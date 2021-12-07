<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='mtitle'>분류 등록</div>
<form method="post" action="../goods/cate" target="ifrmHidden" autocomplete="off">
	<input type="hidden" name="mode" value="add">
	<table class="table-cols">
		<tr>
			<th>분류명</th>
			<td>
				<input type="text" name="cateNm">
			</td>
		</tr>
	</table>
	<div class='table-btn'>
		<input type="submit" value="등록하기" class='btn1'>
	</div>
</form>

<div class='mtitle'>분류 목록</div>
<form method="post" action="../goods/cate" target="ifrmHidden" autocomplete="off">
	<table class="table-rows">
		<thead>
			<tr>
				<th width="20">
					<input type="checkbox" class="js_checkall" data-target-name="cateCd">
				</th>
				<th width="100">분류번호</th>
				<th>분류명</th>
				<th width="100">노출여부</th>
				<th width="100">진열가중치</th>
				<th width="130">등록일시</th>
				<th width="100"></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${list}">
			<tr>
				<td align='center'>
					<input type="checkbox" name="cateCd" value="${item.cateCd}">
				</td>
				<td>${item.cateCd}</td>
				<td>
					<input type="text" name="cateNm_${item.cateCd}" value="${item.cateNm}">
				</td>
				<td align='center'>
					<select name="isShow_${item.cateCd}">
						<option value="0"${item.isShow?"":" selected"}>미노출</option>
						<option value="1"${item.isShow?" selected":""}>노출</option>
					</select>
				</td>
				<td>
					<input type="number" name="listOrder_${item.cateCd}" value="${item.listOrder}">
				</td>
				<td>
					<fmt:parseDate var="date" value="${item.regDt}" pattern="yyyy-MM-dd HH:mm:ss" />
					<fmt:formatDate value="${date}" pattern="yy.MM.dd HH:mm" /> 
				</td>
				<td>
					<a href='../../goods/list?cateCd=${item.cateCd}' target='_blank' class='sbtn1'>미리보기</a>
				</td>
			</tr>
		</c:forEach>			
		</tbody>
	</table>
	<div class='table-action'>
		<select name="mode">
			<option value="edit">수정</option>
			<option value="delete">삭제</option>
		</select>
		<input type="submit" value="처리하기" class="sbtn2" onclick="return confirm('정말 처리하시겠습니까?');">
	</div>
</form>
