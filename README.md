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
    - [x] authorization code : 인증 코드
    - [x] implicit : 암시적 인증
    - [x] password : 암호 인증
    - [x] client credentials : 클라언트 크리덴셜 인증
  - [x] 액세스 토큰 파괴 개발
  - 액세스 토큰에 JWT가 아닌 token_id 를 발급하고 token_id로 JWT로 교환할 수 있도록 한다.
    - [x] 인증 후 response body 수정하기
    - [ ] token_id 로 JWT 교환 구현
    - [ ] 테스트 작성
- [ ] 리소스 서버 설정 구성
  - [x] 액세스 토큰 원격 Opaque Token 검증 구현
  - [x] 액세스 토큰 로컬 JwkSet 검증 구현
  - [x] 액세스 토큰 로컬 public key 검증 구현
  - [x] 액세스 토큰 - JWT Payload 변경 개발 (AuthoritiesOpaqueTokenIntrospector)
- [x] 계정 서비스 개발
- [x] oauth client id 개발
  - [ ] redis 활용 - 데이터 캐시 관리
- [ ] 소캣 통신 - 인증 예제 작성
- [ ] 개인적으로 사용하기 위한 액세스 토큰 생성 구현
- [ ] 보안 ... csrf
---
# 이슈들

## ClientDetailsService 구현체를 @Transactional 설정하면 업데이트가 발생한다.

readOnly 속성으로 해결하긴 했지만, 어떻게 업데이트가 발생하는 것일까? 내부적인 코드를 확인해야 알 수 있을 것 같다. 