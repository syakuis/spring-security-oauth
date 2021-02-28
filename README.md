# Authorization Server

## Objective

JWT 인증 서버 개발.

## TODO

- 기본 인증 구현
- OAuth2 Token 인증 방식 적용
- JWT 인증 방식 적용
- 인증 저장을 내장 저장소를 DB 사용
- Spring Security OAuth 내장 쿼리사용하지 않고 직접 JPA 구현
- Redis 사용 인증 정보 캐싱 처리
- Resource Server 테스트 확인 (서버 분리 필수)

### Addition

- Docker 서비스 구동
- Github Actions 적용

## Done

- 기본 인증 구현
    - form 인증, Http Basic 인증
- OAuth2 Token 인증 방식 적용