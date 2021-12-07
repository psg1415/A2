/**
* 상품 상세
*
*/
const goodsView = {
	/**
	* 클릭 버튼 처리
	* 	cart 클래스 - 장바구니 
	* 	order 클래스 - 바로구매
	* 		
	*/
	process(e) {
		const inner = document.querySelector("#frmGoods .inner");
		if (inner && inner.classList.contains("dn")) {
	
			inner.classList.remove("dn");
			return;
		}

		if (e.currentTarget.classList.contains("cart")) { // 장바구니 
			frmGoods.mode.value = "cart";
		} else { // 바로구매 
			frmGoods.mode.value = "order";
		}
		
		frmGoods.submit();
	}
};

window.addEventListener("DOMContentLoaded", function() {
	/** 장바구니, 바로구매 버튼 클릭 처리  */
	const buttons = document.querySelectorAll(".control_box button");
	buttons.forEach(el => {
		el.addEventListener("click", goodsView.process);
	});
});

