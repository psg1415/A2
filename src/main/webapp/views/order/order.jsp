<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<div class='stitle'>주문하기</div>
<div class
='main_content'>
<form name='frmCart' method="post" action="../order/form" target="ifrmHidden" autocomplete="off">
	<ul class='cart_list order_item_list'>
	<c:choose>
	<c:when test="${items == null || items.size() == 0}">
		<li class='no_list'>담겨진 상품이 없습니다.</li>
	</c:when>
	<c:otherwise>
		<c:forEach var="item" items="${items}">
		<li class='items'>
			<div class='left'>
				<input type="hidden" name="idx" value="${item.getIdx()}">
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
					<fmt:formatNumber value='${item.goodsCnt}'/>개
				</div>
				<div class='total'>합계 : <span class='no'><fmt:formatNumber value="${item.goodsInfo.goodsPrice * item.goodsCnt}" />원</span></div>
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
				<input type="text" name="orderName" value="${member.memNm}">
			</dd>
		</dl>
		<dl>
			<dt>휴대전화번호</dt>
			<dd>
				<input type="text" name="orderCellPhone" value="${member.cellPhone}">
			</dd>
		</dl>
		<dl>
			<dt>이메일주소</dt>
			<dd>
				<input type="email" name="orderEmail">
			</dd>
		</dl>
	</div>
	<!-- // order_box -->
	
	<div class='stitle'>배송지정보</div>
	<div class='order_box'>
		<dl>
			<dt>수령자명</dt>
			<dd>
				<input type="text" name="receiverName">
			</dd>
		</dl>
		<dl>
			<dt>휴대전화번호</dt>
			<dd>
				<input type="text" name="receiverCellPhone" placeholder="수령인 휴대전화번호">
			</dd>
		</dl>
		<dl>
			<dt>배송지주소</dt>
			<dd>
				<div class='addr addr1'>
					<input type="text" name="receiverZonecode" readonly>
					<button type="button" class='search_address'>주소찾기</button>
				</div>
				<div class='addr addr2'>
					<input type="text" name="receiverAddress" placeholder="주소" readonly>
				</div>
				<div class="addr addr3">
					<input type="text" name="receiverAddressSub" placeholder="나머지 주소">
				</div>
			</dd>
		</dl>
	</div>
	<!-- // order_box -->
	
	<div class='stitle'>결제정보</div>
	<div class='order_box'>
		<dl>
			<dt>결제금액</dt>
			<dd>
				<fmt:formatNumber value="${settlePrice}" />원
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
	<div class='control_box'>
		<div class='btns'>
			<button type="button" class='cancel'>취소하기</button>
			<button type="submit" class="order" onclick="return confirm('정말 주문하시겠습니까?');">주문하기</button>
		</div>
	</div>
	<!--// control_box -->
</form>
</div>
<!-- // main_content -->