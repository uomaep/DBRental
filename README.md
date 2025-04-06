# DB 대여 서비스

### 개요
DB 서버가 없으신 분들에게 실습 및 프로젝트를 위한 데이터베이스 스키마를 제공해주는 서비스입니다. 간단하게 회원가입하여 여러 데이터베이스를 생성할 수 있습니다.

### 아키텍처
![Frame 3](https://github.com/user-attachments/assets/31f2fc14-6411-461c-80e8-557a6b42160d)

### 기능
1. 로그인
2. 회원가입
3. 데이터베이스 생성 및 삭제

### 관리자 기능
1. 회원가입, 로그인, 데이터베이스 차단/허용 기능

### 설명

#### 회원가입
<img width="486" alt="스크린샷 2025-04-06 오후 11 42 58" src="https://github.com/user-attachments/assets/f7a92cbf-7bc0-45f2-bdc2-4d901c2227e4" />

DB 접속할 때 사용하게 될 계정과 비밀번호입니다.

#### 로그인
<img width="486" alt="스크린샷 2025-04-06 오후 11 40 36" src="https://github.com/user-attachments/assets/152d4e24-a8b0-47a5-97b6-b0ee176c3daa" />  

가입한 계정과 비밀번호로 로그인할 수 있습니다.

#### 홈

![Frame 2](https://github.com/user-attachments/assets/0a98c6d0-fe62-4e29-8589-13403afb13a0)

- 로그인 시 접속할 수 있는 홈 화면입니다. 입력창에 사용할 데이터베이스명을 입력한 뒤 생성할 수 있습니다.
- 대여한 데이터베이스 리스트를 보여줍니다. 또한 삭제 버튼을 클릭하여 데이터베이스를 삭제할 수 있습니다.

#### 데이터베이스 접속
- host: 실제 DB(서비스 주인이 오픈한 DB 서버)의 호스트 주소
- port: 실제 DB(서비스 주인이 오픈한 DB 서버의 포트)의 포트번호
- database: 이용자가 생성한 데이터베이스 이름
- username, password: 이용자가 로그인한 계정 및 비밀번호

### 직접 서비스 시 작성해야 할 파일(보안이 중요한 파일, .gitignore 적용됨)
/src/main/resources 폴더에 하위 파일 작성 필요
- application-database.properties
- application-secure.properties

### 직접 서비스 시 만들어야 할 DB(db-rental) 테이블
```sql
CREATE DATABASE `db-rental`;
```
```sql
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(100) NOT NULL,
  `password` char(128) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_unique` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
```sql
CREATE TABLE `user_database` (
  `id` int NOT NULL AUTO_INCREMENT,
  `database_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_schema_unique` (`database_name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

#### application-database.properties
```application-database.properties
spring.datasource.url=jdbc:mysql://host:port/db-rental?allowMultiQueries=true
spring.datasource.username=WAS용_DB_USER
spring.datasource.password=WAS용_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### application-secure.properties
```application-secure.properties
# 비밀번호 해시에 사용될 솔트값
security.password.salt=

# 관리자용 account, 이 값에 해당하는 유저가 로그인 시, 홈 화면에서 로그인, 회원가입, 데이터베이스 생성 차단/허용 버튼 활성화 함
admin.account=
```

<img width="467" alt="스크린샷 2025-04-07 오전 12 16 37" src="https://github.com/user-attachments/assets/0502ff70-1897-4864-83de-f00843547efb" />
