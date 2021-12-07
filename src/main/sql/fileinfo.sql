CREATE TABLE `fileinfo` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `gid` bigint DEFAULT NULL,
  `fileName` varchar(100) DEFAULT NULL,
  `fileType` varchar(65) DEFAULT NULL,
  `fileCode` varchar(45) DEFAULT NULL COMMENT '같은 gid 하위의 있는 코드별 구분',
  `isFinish` tinyint(1) DEFAULT '0' COMMENT '파일 업로드 후 상품 등록, 또는 게시글 등록이 완료되면 유효한 파일 업로드라는 표기\n0 - 유효하지 않으므로 일괄적으로 파일 정리 \n1- 정상 업로드 - 유지 ',
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  KEY `ix_gid_regDt` (`gid`,`regDt`),
  KEY `idx_gid_fileCode` (`gid`,`fileCode`)
);