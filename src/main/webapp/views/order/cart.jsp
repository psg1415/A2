<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='stitle'>장바구니</div>
<div class='main_content'>
<form name='frmCart' method="post" action="../order/cart_ps" target="ifrmHidden" autocomplete="off">
	<input type="hidden" name="mode" value="order">
	<ul class='cart_list order_item_list'>
	<c:choose>
	<c:when test="${list == null || list.size() == 0}">
		<li class='no_list'>담겨진 상품이 없습니다.</li>
	</c:when>
	<c:otherwise>
		<c:forEach var="item" items="${list}">
		<li class='items items_${item.idx}'>
			<div class='left'>
				<input type="checkbox" name="idx" value="${item.idx}" checked>
				<c:if test="${item.goodsInfo.listImageUrl != null}">
					<a class='goods_img' href='../goods/view?goodsNo=${item.goodsNo}' style="background:url('${item.goodsInfo.listImageUrl}') no-repeat center center; background-size: cover;">
					</a>
				</c:if>
			</div>
			<!-- // left -->
			<div class='right'>
				<div class='goods_nm'>${item.goodsInfo.goodsNm}</div>
				<div class='price'>단가 : <fmt:formatNumber value="${item.goodsInfo.goodsPrice}" />원</div>
				<div class='goods_cnt'>수량 : 
					<input type="number" class="goodsCnt" data-idx='${item.idx}' data-price='${item.goodsInfo.goodsPrice}' value="${item.goodsCnt}"/>
				</div>
				<div class='total'>합계 : <span class='no'><fmt:formatNumber value="${item.goodsInfo.goodsPrice * item.goodsCnt}" />원</span></div>
			</div>
			<!-- // right -->				
		</li>
		</c:forEach>
	</c:otherwise>
	</c:choose>
	</ul>
	<div class='control_box'>
		<div class='btns'>
			<button type="button" class='delete'>선택상품 삭제</button>
			<button type="button" class="order">선택상품 주문</button>
		</div>
	</div>
	<!--// control_box -->
</form>
</div>
<!-- // main_content -->
