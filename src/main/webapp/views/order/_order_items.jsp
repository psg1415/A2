<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ul class='cart_list order_item_list'>
	<c:choose>
	<c:when test="${items == null || items.size() == 0}">
		<li class='no_list'>담겨진 상품이 없습니다.</li>
	</c:when>
	<c:otherwise>
		<c:forEach var="item" items="${items}">
		<li class='items'>
			<div class='left'>
				<c:if test="${item.goods.listImageUrl != null}">
					<a class='goods_img' href='../goods/view?goodsNo=${item.goodsNo}' style="background:url('${item.goods.listImageUrl}') no-repeat center center; background-size: cover;">
					</a>
				</c:if>
			</div>
			<!-- // left -->
			<div class='right'>
				<div class='goods_nm'>${item.goodsNm}</div>
				<div class='price'>단가 : <fmt:formatNumber value="${item.goods.goodsPrice}" />원</div>
				<div class='goods_cnt'>수량 : 
					<fmt:formatNumber value='${item.goodsCnt}'/>개
				</div>
				<div class='total'>합계 : <span class='no'><fmt:formatNumber value="${item.goods.goodsPrice * item.goodsCnt}" />원</span></div>
			</div>
			<!-- // right -->				
		</li>
		</c:forEach>
	</c:otherwise>
	</c:choose>
</ul>

<div class='stitle'>주문자정보</div>
	<div class='order_box'>
		<dl>
			<dt>주문자명</dt>
			<dd>
				${order.orderName}
			</dd>
		</dl>
		<dl>
			<dt>휴대전화번호</dt>
			<dd>
				${order.orderCellPhone}
			</dd>
		</dl>
		<dl>
			<dt>이메일주소</dt>
			<dd>
				${order.orderEmail}
			</dd>
		</dl>
	</div>
	<!-- // order_box -->
	
	<div class='stitle'>배송지정보</div>
	<div class='order_box'>
		<dl>
			<dt>수령자명</dt>
			<dd>
				${order.receiverName}
			</dd>
		</dl>
		<dl>
			<dt>휴대전화번호</dt>
			<dd>
				${order.receiverCellPhone}
			</dd>
		</dl>
		<dl>
			<dt>배송지주소</dt>
			<dd>
				${order.receiverZonecode} ${order.receiverAddress} ${order.receiverAddressSub}
			</dd>
		</dl>
	</div>
	<!-- // order_box -->
	
	<div class='stitle'>결제정보</div>
	<div class='order_box'>
		<dl>
			<dt>결제금액</dt>
			<dd>
				<fmt:formatNumber value="${order.settlePrice}" />원
			</dd>
		</dl>
		<dl>
			<dt>결제수단</dt>
			<dd>무통장 입금</dd>
		</dl>
		<dl>
			<dt>입금계좌번호</dt>
			<dd>국민 00000-00-0000 예금주 OOOO</dd>
		</dl>
	</div>
	<!-- // order_box -->	