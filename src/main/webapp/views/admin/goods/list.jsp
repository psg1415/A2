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

