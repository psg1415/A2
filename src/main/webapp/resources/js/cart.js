/**
* 장바구니 처리 관련
*
*/
const cart = {
	/**
	* 장바구니 버튼 처리 
	*
	* delete 클래스 - 선택상품 삭제
	* order 클래스 - 선택상품 주문 
	*/
	process(e) {
		const target = e.target;
		let mode = "order";
		if (target.classList.contains("delete")) {
			mode = "delete";
		}
		
		let message = "";
		/** 선택한 상품이 있는지 체크 */
		const checks = document.querySelectorAll(".body-order-cart input[name='idx']:checked");
		if (checks.length == 0) {
			message = (mode == 'delete')?"삭제":"주문";
			message += "할 상품을 선택하세요.";
			alert(message);
			return;
		}
		
		message = "정말 ";
		message += (mode == 'delete')?"삭제":"주문";
		message += "하시겠습니까?"
		if (confirm(message)) {
			frmCart.mode.value = mode;
			frmCart.submit();
		} // endif 
		
	},
	/**
	* 장바구니 수량 변경 
	*
	*/
	updateGoodsCnt(e) {
		const target = e.target;
		const idx = target.dataset.idx;
		const price = Number(target.dataset.price);
		const goodsCnt = Number(target.value);
		
		const param = `mode=change&idx=${idx}&price=${price}&goodsCnt=${goodsCnt}`;
		
		const xhr = new XMLHttpRequest();
		xhr.open("POST", "../order/cart_ps");
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				const json = JSON.parse(xhr.responseText);
				if (json.success) {
					const el = document.querySelector(".cart_list .items_" + idx + " .total .no");
					const total = price * goodsCnt;
					el.innerText = total.toLocaleString() + "원";
				
					
				} else {
					alert(json.message);
				}
			}
		};
		
		xhr.send(param);
	}
};

window.addEventListener("DOMContentLoaded", function() {
	/** 장바구니 버튼 이벤트 등록 S */
	const buttons = document.querySelectorAll(".body-order-cart .control_box button");
	buttons.forEach(el => {
		el.addEventListener("click", cart.process);
	});
	/** 장바구니 버튼 이벤트 등록 E */
	
	/** 장바구니 수량 변경 S */
	const goodsCnts = document.querySelectorAll(".body-order-cart .goodsCnt");
	goodsCnts.forEach(el => {
		el.addEventListener("keyup", cart.updateGoodsCnt);
		el.addEventListener("change", cart.updateGoodsCnt);
		el.addEventListener("blur", cart.updateGoodsCnt);
	});
	/** 장바구니 수량 변경 E */
});