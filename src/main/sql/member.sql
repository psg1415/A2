CREATE TABLE `member` (
  `memNo` int NOT NULL AUTO_INCREMENT,
  `memId` varchar(30) NOT NULL,
  `memPw` varchar(65) NOT NULL,
  `memPwHint` varchar(50) NOT NULL,
  `memNm` varchar(30) NOT NULL,
  `cellPhone` varchar(11) DEFAULT NULL,
  `socialType` enum('none','kakao','naver') DEFAULT 'none',
  `socialId` varchar(65) DEFAULT NULL,
  `address` varchar(40) DEFAULT NULL,
  `isAdmin` tinyint(1) DEFAULT '0' COMMENT '1 - 관리자\n0 - 일반회원',
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP,
  `modDt` datetime DEFAULT NULL,
  PRIMARY KEY (`memNo`),
  UNIQUE KEY `memId` (`memId`)
);