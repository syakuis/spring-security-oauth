# Spring Security oauth server

spring security oauth server를 사용하여 인증 서버를 구축한 프로젝트입니다.

## 의존성 버전

- spring boot - 2.4.5
- spring security oauth server - 2.3.8.RELEASE

## 할일

- [x] 인프라 환경 도커로 설정 구성
- [x] 인증 서버 시큐리티 설정 구성
  - [ ] 액세스 토큰 - JWT Payload 변경 구현
  - 액세스 토큰 저장소 설정 구현
    - [x] jdbc
    - [x] redis
  - 인증 방식 구현
    - [x] authorization code
    - [x] implicit
    - [x] password
    - [ ] client credentials
  - [ ] 액세스 토큰 파괴 개발
- [ ] 리소스 서버 설정 구성
  - [ ] 액세스 토큰 원격 Opaque Token 검증 구현
  - [ ] 액세스 토큰 로컬 JWTs 검증 구현
  - [ ] 액세스 토큰 - JWT Payload 변경 개발
- [x] 계정 서비스 개발
- [ ] oauth client id 개발
  - [ ] redis 활용 - 데이터 캐시 관리
- [ ] 소캣 통신 - 인증 예제 작성
- [ ] Spring Gateway에서 액세스 토큰으로 JWT 얻기 구현
- [ ] 개인적으로 사용하기 위한 액세스 토큰 생성 구현