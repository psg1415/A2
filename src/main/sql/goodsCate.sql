CREATE TABLE `goodscate` (
  `cateCd` int NOT NULL AUTO_INCREMENT COMMENT '분류 코드',
  `cateNm` varchar(60) DEFAULT NULL COMMENT '상품분류 명',
  `isShow` tinyint(1) DEFAULT '0' COMMENT '노출 여부 - 0(미노출), 1(노출)',
  `listOrder` int DEFAULT '0' COMMENT '진열 순서 - 진열 가중치 -> 숫자가 입력되면 큰 숫자먼저 노출',
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '날짜 오름차순 기본 정렬',
  PRIMARY KEY (`cateCd`),
  KEY `ix_listOrder_regDt` (`listOrder` DESC,`regDt`)
);