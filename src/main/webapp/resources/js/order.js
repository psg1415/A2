/**
* 주문서 관련 
*
*/
window.addEventListener("DOMContentLoaded", function() {
	/** 주소 찾기 S */
	const searchAddress = document.querySelector(".search_address");
	if (searchAddress) {
		searchAddress.addEventListener("click", function() {
			/** 다음 주소 검색 코드 S */
			 new daum.Postcode({
					oncomplete: function(data) {
						document.getElementsByName("receiverZonecode")[0].value = data.zonecode;
						document.getElementsByName("receiverAddress")[0].value = data.address;
						document.getElementsByName("receiverAddressSub")[0].focus();
					}
			}).open();
			/** 다음 주소 검색 코드 E */
		});
	}
	
	/** 주소 찾기 E */
	/** 취소하기 S */
	const cancel = document.querySelector(".control_box .cancel");
	if (cancel) {
		cancel.addEventListener("click", function() {
			if (confirm('정말 취소하시겠습니까?')) {
				location.href='../order/cart';
			}
		});
	}
	/** 취소하기 E */
});