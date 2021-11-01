# Spring Security oauth server

spring security oauth server를 사용하여 인증 서버를 구축한 프로젝트입니다.

## 의존성 버전

- spring boot - 2.4.5
- spring security oauth server - 2.3.8.RELEASE

## 할일

- [x] 인프라 환경 도커로 설정 구성
- [x] 인증 서버 시큐리티 설정 구성
  - [x] 액세스 토큰 - JWT Payload 변경 구현
  - 액세스 토큰 저장소 설정 구현
    - [x] jdbc
    - [x] redis
  - 인증 방식 구현
    - [x] authorization code : 인증 코드
    - [x] implicit : 암시적 인증
    - [x] password : 암호 인증
    - [x] client credentials : 클라언트 크리덴셜 인증
  - [x] 액세스 토큰 파괴 개발
  - 액세스 토큰을 jwt 값이 아닌 연결되는 id 값으로 대처 - 브래인스토밍 결과에 의해 구현하지 않음.
- 리소스 서버 설정 구성
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

# 브레인 스토밍

## ClientDetailsService 구현체를 @Transactional 설정하면 업데이트가 발생한다.

readOnly 속성으로 해결하긴 했지만, 내부적으로 어떠한 변경이 발생하여 업데이트가 되는 지 확인할 것. reflection 에 의해 발생하는 것이 아닌가?
나머지 find 서비스도 read only transaction 으로 설정할 것

## 액세스 토큰을 jwt 값이 아닌 연결되는 id 값으로 대처

### 2차 수정안

인증 검증 CheckTokenEndpoint 구현체를  그대로 새로 만들어서 해결 함.
refresh_token 을 얻기 위해 TokenEndpoint 변경이 필요함.
너무 많은 부분을 수정해야 하는 작업으로 의도한 것과 다르게 가는 것 같아 중단한다.

oauth server 를 위한 spring gateway 에서 구현하는 것이 더 효율적일 것 같다. 스프링 시큐리티 본연의 기능을 유지하고 외부에서 처리하는 방향으로 한다. 

### 1차 수정안

인증 발급 기능에서 응답시 jwt 값이 아닌 연결될 수 있는 id 값으로 대처해야한다. 비지니스 로직을 건들지 않고 응답 body의 access_token 값을 치환하는 것으로 해결 한다.

응답을 제어하기 위해 filter, interceptor, aop 를 고려해보았지만 authorization code는 여러 번의 클라이언트와 응답하기 때문에 그 과정에서 잘못된 캐치로 인해 오류가 발생하였다.
분명한 원인은 확인되지 않았지만 authorization code 인증 방식에서 code 를 redirect 받고 다시 액세스 토큰을 교환하기 위해 요청시 오류가 발생된다.
하여 최종적인 응답을 제어할 수 있는 RestControllerAdvice 를 사용하여 처리했다.

implicit 는 최종 응답 처리하는 클래스가 달라서 따로 구현해야한다. 현재 해당 인증 방식은 사용하지 않으므로 구현하지 않는 걸로한다.

인증 검증 기능에서 accessToken 을 구하기 위해 authorization 정보가 필요하다. 인증 발급 기능에서 받은 access_token 을 이용하여 조회가 가능하다.
하지만 InMemory 방식은 private 자료를 사용하므로 조회할 수 없다. 하여 인증 발급 기능과 인증 검증은 Redis 만 구현하는 것으로 방향을 변경한다.

jdbc
authentication_id = authenticationKeyGenerator.extractKey(OAuth2Authorization)
token_id = extractTokenKey(access_token)

redis
AUTH_TO_ACCESS = authenticationKeyGenerator.extractKey(OAuth2Authorization)
ACCESS = access_token
AUTH = access_token
