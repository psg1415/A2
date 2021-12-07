CREATE TABLE `ordergoods` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `orderNo` bigint NOT NULL COMMENT '주문번호',
  `goodsNo` int unsigned NOT NULL COMMENT '주문상품번호',
  `goodsNm` varchar(100) NOT NULL COMMENT '주문상품명',
  `goodsCnt` int DEFAULT '1' COMMENT '주문상품갯수',
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '주문일시',
  PRIMARY KEY (`idx`)
);