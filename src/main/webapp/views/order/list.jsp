<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='stitle'>주문목록</div>
<div class='main_content'>
<ul class='order_list'>
<c:choose>
<c:when test='${list == null || list.size() == 0 }'>
	<li class='no_list'>주문내역이 없습니다.</li>
</c:when>
<c:otherwise>
<c:forEach var="item" items="${list}">
	<li>
		<a href='../order/view?orderNo=${item.orderNo}'>
			주문번호 : ${item.orderNo} / 주문상태 : ${item.statusStr}
		</a>
	</li>
</c:forEach>
</c:otherwise>
</c:choose>
</ul>
</div>
<!-- // main_content -->
