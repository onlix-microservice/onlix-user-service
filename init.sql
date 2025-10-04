-- DB 생성
CREATE DATABASE `onlix_user`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

-- DB 선택
USE `onlix_user`;

-- account 테이블 생성
CREATE TABLE `account` (
  `account_id` BIGINT NOT NULL AUTO_INCREMENT,
  `login_id` VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` VARCHAR(10) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB
  AUTO_INCREMENT=1
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 초기 계정 데이터 삽입
INSERT INTO account (account_id, login_id, password, `role`, status)
VALUES (
  1,
  'test@naver.com',
  '$2a$10$sOD2lTQaXSzbVtCVJBnlAuVoW7ZcbAx2r1ilpuGIx1e8BFIRgAUTe', 
  'USER',
  'ACTIVE'
);