/** 이벤트 처리  */
window.addEventListener("DOMContentLoaded", function() {
	/** 체크박스 전체 선택 S  */
	const checkalls = document.querySelectorAll(".js_checkall");
	checkalls.forEach(el => {
		el.addEventListener("click", e => {
			const target = e.target.dataset.targetName;
			const checks = document.getElementsByName(target);
			checks.forEach(check => {
				check.checked = e.target.checked;	
			});
		});
	});
	/** 체크박스 전체 선택 E */
});