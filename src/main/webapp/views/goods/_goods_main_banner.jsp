<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${goods.mainImages != null && goods.mainImages.size() > 0}">
<link rel="stylesheet" href="https://unpkg.com/swiper@7/swiper-bundle.min.css" />
<div class='main_rolling'>
	<div class="swiper mySwiper">
		<div class="swiper-wrapper">
		<c:forEach var="item" items="${goods.mainImages}">
			<div class="swiper-slide">
			<img src='${item.uploadUrl}'>
			</div>
		</c:forEach>
		</div>
		<div class="swiper-button-next"></div>
	    <div class="swiper-button-prev"></div>
	</div>
</div>
<!-- // main_rolling -->
<script src="https://unpkg.com/swiper@7/swiper-bundle.min.js"></script>
<script>
 var swiper = new Swiper(".mySwiper", {
	 navigation: {
  		nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
     },
  });
</script>
</c:if>