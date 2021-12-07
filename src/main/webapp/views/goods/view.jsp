<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='goods_nm'>${goods.goodsNm}</div>
<div class='main_content'>
<!-- 메인배너 S -->
<jsp:include page="_goods_main_banner.jsp" />
<!-- 메인배너 E -->

<div class='goods_info'>
	<div class='price'>판매가: <fmt:formatNumber value="${goods.goodsPrice}" />원</div>
</div>

<div class='stitle'>상품설명</div>
<div class='explain'>${goods.goodsExplain}</div>
</div>
<!-- // main_content  -->
<div class='control_box'>
<form id='frmGoods' name='frmGoods' method="post" action="../order/cart" target='ifrmHidden'>
	<input type="hidden" name="mode" value="cart">
	<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
	
	<div class='inner dn'>
		구매수량 : <input type="number" name="goodsCnt" value="1" id='goodsCnt'>
	</div>
	<div class='btns'>
		<button type="button" class='cart'>장바구니</button>
		<button type="button" class='order'>바로구매</button>
	</div>
</form>
</div>
<!-- // control_box -->
