# 게시판 만들기

## 사용된 기술

- Spring Boot
- Spring MVC
- Spring JDBC
- MYSQL - SQL
- thymeleaf 탬플릿 엔진

### MySql 계정
- username : root
- userpassword : root1234

## 아키텍처


```
Spring Boot : 전체 프레임워크
Spring core : 전체 Bean 관리.
Spring MVC : 컨트롤러부터 서비스까지 관리.
Spring JDBC : 다오와 DB사이를 관리
MySQL : 사용하는 DB

브라우저 -> 요청 -> Controller -> Service -> DAO -> DB
DB -> DAO -> Service -> Controller -> 템플릿 -> 응답 -> 브라우저 

layer간에 데이터 전송은 DTO를 사용.
```

## 게시판 만드는 순서

1. Controller와 템플릿
2. Service - 비지니스 로직을 처리 (하나의 트랜잭션 단위)
3. Service는 비지니스 로직을 처리하기 위해 데이터를 CRUD 하기위해 DAO를 사용






