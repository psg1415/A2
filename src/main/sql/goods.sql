CREATE TABLE `goods` (
  `goodsNo` int NOT NULL AUTO_INCREMENT,
  `gid` bigint DEFAULT NULL,
  `goodsNm` varchar(30) NOT NULL,
  `goodsPrice` int NOT NULL,
  `cateCd` int DEFAULT NULL,
  `goodsExplain` text NOT NULL,
  `listOrder` int DEFAULT '0',
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`goodsNo`),
  KEY `ix_listOrder_regDt` (`listOrder` DESC,`regDt` DESC)
);