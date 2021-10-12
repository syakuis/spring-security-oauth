# [Legacy] Spring Security oauth server

## 할일

- [ ] 단위 테스트는 Mock 으로 작성할 것.
- [ ] 각 서비스 개별 프로젝트로 분리할 것.
- [ ] client-id 서비스 추가

---

## 의존성 버전

- spring boot - 2.4.5
- spring security oauth server - 2.3.8.RELEASE

## 서비스
- authorization-service
- account-service
- clientid-service
- todo-service

## 용어정리

- authorization server - 인증, 인가를 처리하는 서버 프로그램이며 인증 서버라고 부른다.
- resource server - 클라언트에서 자원을 요청하는 서버 프로그램이며, authorization server를 통해 인가 후 자원을 응답해준다. 자원 서버라고 부른다.
- Stateless - 무상태란? 서버에 상태 값을 저장하지 않고 요청하는 곳에서 상태 값을 관리하는 것이다. 클라이언트에서 인증 토큰을 가지고 있고 자원을 요청할때 인증 토큰을 헤더 값에 설정하여 요청한다. 자원 서버에서 인증 서버로 인증 토큰을 검증 후 자원 서버에서 자원을 응답한다. 자원 서버는 인증과 관련되 정보를 가지고 있지 않기 때문에 확장성에 이점을 가진다.

## Workflow

## Gradle 구성

- *-server 접미사는 독립적으로 운영되는 서비스 패키지입니다.
- *-module 접미사는 반복적으로 사용되는 기능을 모듈화하여 재사용할 수 있는 라이브러리 패키이입니다.


## 인증 서버 구현
- 계정 서비스
- Client-id 서비스
  - 애플리케이션을 등록하고 관리한다.
  - 등록된 애플리케이션에 대한 인증 서비스 제공하고 관리하기 위함이다.
- 인증 서비스
  - 사용자 계정에 대한 인증, 인가를 처리한다.

## 자원 서버 구현

- 인증 서비에 인증, 인가를 요청하고 처리한다.
- 클라이언트에서 요청한 자원을 응답한다.

## API Gateway 구현

## BFF 구현

## Frontend 구현