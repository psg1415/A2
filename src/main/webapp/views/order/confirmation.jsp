<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='stitle'>주문완료</div>
<div class='message'>
	주문이 완료 되었습니다.<br>
	주문번호 : ${order.orderNo}<br>
	주문상태 : ${order.statusStr}
</div>
<div class='main_content'>
<jsp:include page="_order_items.jsp" />
</div>
<!-- // main_content -->
<div class='control_box'>
	<div class='btns'>
		<a href='../order/list'>마이페이지</a>
		<a href='../goods/category'>쇼핑계속하기</a>
	</div>
</div>
<!--// control_box -->