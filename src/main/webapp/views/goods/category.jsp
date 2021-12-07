<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${categories != null && categories.size() > 0}">
<ul>
<c:forEach var="item" items="${categories}">
	<li>
		<a href='../goods/list?cateCd=${item.cateCd}'>${item.cateNm}</a>
	</li>
</c:forEach>
</ul>
</c:if>