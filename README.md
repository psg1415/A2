# Diet Shop - 팀프로젝트
* JSP로 구현한 반응형 웹 다이어트 음식 쇼핑몰 [사이트링크](http://dietshop1.cafe24.com)
* 개발인원: 5명
* css및 views파일담당
# 개요
## 1. 서비스 내용 
* 다이어트 음식 쇼핑몰 구현
* 회원가입한 회원들이 원하는 음식을 장바구니에 추가 및 주문이 가능
* 관리자가 원하는 카테고리 생성 상품등록 가능

## 2. 적용기술
<img src="https://img.shields.io/badge/JSP-4FC08D?style=for-the-badge&logo=JSP&logoColor=white"> <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/html-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

## 3. 각 페이지별 소개
### 사이트의 메인 페이지
* 로그인 -> 로그인 한 경우, 로그아웃으로 변경
* 카테고리
* 마이페이지 - 주문내역 확인가능
* 장바구니
<details>
	<summary>메인페이지-모바일</summary>
	
![메인](https://user-images.githubusercontent.com/84897600/145925386-748161ce-3263-4a9e-8a19-854d45c89938.png)
	
</details>

### 카테고리 페이지
* 카테고리 분류별로 이동시 각 분류에 맞는 상품노출
<details>
	<summary>카테고리-모바일</summary>
	
![카테고리](https://user-images.githubusercontent.com/84897600/145925544-082d39c4-2759-401e-b5dc-a7e74daf2d56.png)
	
</details>


### 마이페이지
* 주문내역 확인가능
* 각 주문내역 이동시 상세내역 확인가능
<details>
	<summary>마이페이지-모바일</summary>
	
![주문목록](https://user-images.githubusercontent.com/84897600/145928052-60988dda-6074-459c-86eb-4bd650000286.png)
![마이페이지1](https://user-images.githubusercontent.com/84897600/145940756-be1ba0e7-e648-4f0c-ad60-8efae7d2f59c.png)
	
</details>


### 장바구니
* 상품을 선택해서 삭제, 주문이 가능
* 주문 버튼을 누르면 주문하기 페이지로 이동
<details>
	<summary>장바구니-모바일</summary>
	
![장바구니](https://user-images.githubusercontent.com/84897600/145927317-5c47db1d-76d7-4d16-928e-97af81cbb97d.png)
	
</details>


### 로그인
* 편의성을 위해 네이버로 로그인이 가능
* 로그인 후, 일정시간이 경과하면, 자동으로 로그아웃 됨
<details>
	<summary>로그인-모바일</summary>
	
![로그인](https://user-images.githubusercontent.com/84897600/145928271-924fadc2-c5e9-4617-9c49-bbdb36474e4b.png)
	
</details>


### 회원가입
* 비밀번호는 영문자, 숫자, 특수문자를 사용하지 않으면 에러발생
* 핸드폰 번호의 형식과 맞지 않으면 에러발생
<details>
	<summary>회원가입-모바일</summary>
	
![회원가입](https://user-images.githubusercontent.com/84897600/145928452-1eeb552a-ead2-4bfc-a8e2-89839544f9cc.png)
	
</details>


### 주문하기
* 이메일의 형식에 맞지않으면 에러발생
* 주문하기 버튼을 누르면 주문 완료
* 주문하기 -> 쇼핑 계속하기로 변경


### 관리자페이지
* 관리자권한이 있는 아이디만 접속가능
* 카테고리 등록/수정/삭제
* 상품 등록/수정/삭제
<details>
	<summary>관리자페이지</summary>
	
![상품분류](https://user-images.githubusercontent.com/84897600/146128648-97ef4618-50d3-4aab-a9c8-4959a36529ff.png)
![상품목록](https://user-images.githubusercontent.com/84897600/146128676-2e26d169-db1f-4939-83e8-b7bafe30726c.png)
	
</details>
