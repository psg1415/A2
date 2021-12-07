<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class='mtitle'>${category.cateNm}</div>
<c:if test="${list != null && list.size() > 0}">
<ul class="goods_list">
<c:forEach var="item" items="${list}">
	<li>
		<a href='../goods/view?goodsNo=${item.goodsNo}'>
			<img src="${item.listImageUrl}" class='goodsImage'>						

	    	<div class='goodsNm'>${item.goodsNm}</div>
			<div class='price'>
	       		<span class='goodsPrice'>
	       			<fmt:formatNumber value="${item.goodsPrice}" />원
	       		</span>
       		</div>
		</a>
	</li>	
</c:forEach>
</ul>
</c:if>