<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="${rootURL}/resources/js/ckeditor/ckeditor.js"></script>
<div class='mtitle'>
	<c:choose>
		<c:when test="${goods == null}">상품등록</c:when>
		<c:otherwise>상품수정</c:otherwise>
	</c:choose>
</div>
<form method="post" action="../goods/${mode}" target="ifrmHidden" autocomplete="off">
	<c:if test="${goods != null}">
		<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
	</c:if>
	<input type="hidden" name="gid" value="${(goods == null)?gid:goods.gid}">
	<table class="table-cols">
		<tr>
			<th>상품분류</th>
			<td>
				<select name="cateCd">
					<option value="">- 선택하세요 -</option>
					<c:forEach var="item" items="${categories}">
						<option value="${item.cateCd}"${(goods.cateCd == item.cateCd)?" selected":""}>${item.cateNm}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>상품명</th>
			<td>
				<input type="text" name="goodsNm" value="${goods.goodsNm}">
			</td>
		</tr>
		<tr>
			<th>판매가</th>
			<td>
				<input type="number" name="goodsPrice" value="${goods.goodsPrice}">
			</td>
		</tr>
		<tr>
			<th>상품이미지(메인)</th>
			<td>
				<input type="file" class="file_upload" data-file-code="goods_main" data-is-image-only="1">
				<div class="uploaded_images">
				<c:if test="${goods.mainImages != null}">
					<c:forEach var="item" items="${goods.mainImages}">
						<span class="file_item" data-idx='${item.idx}'>	
							<i class="xi-close-min remove"></i>
							<img src='${item.uploadUrl}' width='50'>
						</span>
					</c:forEach>
				</c:if>
				</div>
			</td>
		</tr>
		<tr>
			<th>상품이미지(목록)</th>
			<td>
				<input type="file" class="file_upload" data-file-code="goods_list" data-is-image-only="1">
				<div class="uploaded_images">
				<c:if test="${goods.listImages != null}">
					<c:forEach var="item" items="${goods.listImages}">
						<span class="file_item" data-idx='${item.idx}'>	
							<i class="xi-close-min remove"></i>
							<img src='${item.uploadUrl}' width='50'>
						</span>
					</c:forEach>
				</c:if>
				</div>
			</td>
		</tr>
		<tr>
			<th>상세설명</th>
			<td>
				<textarea name="goodsExplain" id="goodsExplain">${goods.goodsExplain}</textarea>
				이미지 첨부
				<input type="file" class="file_upload"	data-file-code="goods_explain" data-is-image-only="1">		
				<div class="uploaded_images">
				<c:if test="${goods.explainImages != null}">
					<c:forEach var="item" items="${goods.explainImages}">
						<span class="file_item" data-idx='${item.idx}'>	
							<i class="xi-close-min remove"></i>
							<img src='${item.uploadUrl}' width='50'>
						</span>
					</c:forEach>
				</c:if>
				</div>
			</td>
		</tr>
	</table>
	<div class='table-btn'>
		<input type="submit" value="${(goods == null)?'등록하기':'수정하기'}" class='btn1'>
	</div>
</form>

<script>
/**
 * 파일 업로드 콜백 
 *
 * @param String fileCode 파일 코드 
 * @param JSON data 업로드된 파일 정보 
 */
function uploadFileCallback(fileCode, data) {
	if (fileCode == 'goods_explain') { // 상세설명 에디터 첨부 처리
		const html = "<img src='" + data.uploadUrl +"'>";
		CKEDITOR.instances.goodsExplain.insertHtml(html);
	}
}
</script>