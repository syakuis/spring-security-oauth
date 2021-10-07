# [Legacy] Spring Security oauth server

## 용어정리

- authorization server - 인증, 인가를 처리하는 서버 프로그램이며 인증 서버라고 부른다.
- resource server - 클라언트에서 자원을 요청하는 서버 프로그램이며, authorization server를 통해 인가 후 자원을 응답해준다. 자원 서버라고 부른다.
- Stateless - 무상태란? 서버에 상태 값을 저장하지 않고 요청하는 곳에서 상태 값을 관리하는 것이다. 클라이언트에서 인증 토큰을 가지고 있고 자원을 요청할때 인증 토큰을 헤더 값에 설정하여 요청한다. 자원 서버에서 인증 서버로 인증 토큰을 검증 후 자원 서버에서 자원을 응답한다. 자원 서버는 인증과 관련되 정보를 가지고 있지 않기 때문에 확장성에 이점을 가진다.

## Workflow

## 인증 서버 구현

## 자원 서버 구현