CREATE TABLE `cart` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `memNo` int NOT NULL COMMENT '회원번호',
  `goodsNo` int NOT NULL COMMENT '상품번호',
  `goodsCnt` int NOT NULL COMMENT '구매수량',
  `isBuy` tinyint(1) DEFAULT '0' COMMENT '0 - 장바구니\n1 - 주문하기',
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`)
);