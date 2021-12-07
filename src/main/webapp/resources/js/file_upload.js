/**
* ajax 파일 업로드 
*
*/
const fileUpload = {
	/**
	* 파일 업로드 처리 
	*
	*/
	upload(e) {
		try {
			const target = e.target;
			const fileCode = target.dataset.fileCode;
			const isImageOnly = target.dataset.isImageOnly;
			let gid = 0;
			const gidEl = document.getElementsByName("gid")[0];
			if (gidEl) gid = gidEl.value;
			
			const file = e.target.files[0];
			file.gid = gid;
			file.fileCode = fileCode;
			
			/** 파일형식이 이미지만 업로드 하는 경우 체크 */
			if (isImageOnly == "1" && file.type.indexOf("image") == -1) {
				throw new Error("이미지 형식의 파일만 업로드 하세요.");
			}
			
			
			const reader = new FileReader();
			
			// 파일 정상 업로드 완료시 발생하는 이벤트  
			reader.onload = async function() {
				try {
					const base64 = reader.result;
					const data = await fileUpload.sendFile(file, base64);
					if (!data.success) {
						throw new Error(data.message);
					}
					
					/** 이미지형식의 파일인 경우는 uploaded_images에 이미지 표기 */
					const el = target.nextElementSibling;
					if (el && el.classList.contains("uploaded_images")) {
						const span = document.createElement("span");
						span.className = "file_item";
						span.dataset.idx=data.idx;
						span.dataset.url=data.uploadUrl;
						const remove = document.createElement("i");
						remove.className = "xi-close-min remove";
						const img = document.createElement("img");
						img.src = data.uploadUrl;
						img.width = 50;	
						
						span.appendChild(remove);
						span.appendChild(img);
						el.appendChild(span);
						
						remove.addEventListener("click", fileUpload.delete);
					}
					
					/** 파일 업로드 콜백 함수가 정의되어 있으면 호출  */
					if (typeof uploadFileCallback == 'function') {
						uploadFileCallback(fileCode, data);
					}
					
					target.value = "";
				} catch (err) {
					console.error(err);
				}
				
			};
			
			// 파일 업로드시에 오류 발생시  
			reader.onerror = function(e) {
				console.error(e);
			};
			
			reader.readAsDataURL(file);
		} catch(err) {
			alert(err.message);
		}
	},
	/**
	* 서버에 파일 전송 
	*
	 */
	sendFile(file, base64) {
		return new Promise((resolve, reject) => {
			const xhr = new XMLHttpRequest();
			let url = "../file/upload";
			if (location.pathname.indexOf("/admin") != -1) {
				url = "../" + url;
			}
			const data = base64.split("base64,")[1].trim();
			const param = `gid=${encodeURIComponent(file.gid)}&fileCode=${encodeURIComponent(file.fileCode)}&fileName=${encodeURIComponent(file.name)}&fileType=${encodeURIComponent(file.type)}&data=${encodeURIComponent(data)}`;
			xhr.open("POST", url);
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			
			/** 전송 후 응답 성공 처리  */
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4 && xhr.status == 200) {
					resolve(JSON.parse(xhr.responseText));
				}
			};
			
			/** 전송 후 에러 처리  */
			xhr.onerror = function(e) {
				reject(e);	
			};	
			
			xhr.onabort = function(e) {
				reject(e);
			}
			
			xhr.send(param);
		});
	},
	/**
	* 파일삭제 처리 
	*
	 */
	delete(e) {
		if (!confirm('정말 삭제하시겠습니까?')) {
			return;
		}
		
		const target = e.currentTarget;
		const parentEl = target.parentElement;
		const idx = parentEl.dataset.idx;
		if (!idx) {
			return;
		}
		
		let url = "../file/delete?idx=" + idx;
		if (location.pathname.indexOf("/admin") != -1) {
			url = "../" + url;
		}
		
		const xhr = new XMLHttpRequest();
		xhr.open("GET", url);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				const json = JSON.parse(xhr.responseText);
				if (json.success) {
					parentEl.parentElement.removeChild(parentEl);
				} else {
					alert("삭제 실패하였습니다.");
				}
			}
		};
		
		xhr.send(null);
		
	}
};

window.addEventListener("DOMContentLoaded", () => {
	/** 파일 업로드  */
	const fileUploads = document.querySelectorAll(".file_upload");
	fileUploads.forEach(el => {
		el.addEventListener("change", fileUpload.upload);
	});
	
	/** 파일 삭제 */
	const fileRemoves = document.querySelectorAll(".file_item .remove");
	fileRemoves.forEach(el => {
		el.addEventListener("click", fileUpload.delete);
	});
});