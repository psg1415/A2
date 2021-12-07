<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="main">
	<c:forEach var="item" items="${list}" varStatus="status">
	<div class="product product${status.count % 3}">	
		<a href='goods/view?goodsNo=${item.goodsNo}'>
			<img src="${item.listImageUrl}" class='goodsImage' >						
	
		    <div class='goodsNm'>${item.goodsNm}</div>
			<div class='price'>
		        <span class='goodsPrice'><fmt:formatNumber value="${item.goodsPrice}" />원</span>
	       </div>
		</a>
	</div>
	</c:forEach>
	
</div>