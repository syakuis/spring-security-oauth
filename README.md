# JWT Authorization Server

## Objective

JWT 인증 서버 개발

## Lifecycle

```plantuml
actor user
user -> login
login -> accessToken
login <-- accessToken
note right : POST http://localhost:8080/oauth/token
user -> tokenCheck
note right : POST http://localhost:8080/oauth/check_token
tokenCheck -> page
```

## HOW

- 인증 서버 역할
    - 회원 관리 REST APIs
    - 클라이언트 관리 REST APIs
        - 클라언트 인가 설정
            - 회원 정보 조회
            - 토큰 검증
            - 토큰 삭제
            - 토큰 조회
    - 인증 토큰 관리 REST APIs
        - DB 저장소 사용
        - 토큰 생성 수 제한 (기기 토큰 발행 제어)
        - 모든 요청은 인증 서버에 확인. (캐시 필요)
            - 즉시 토큰 파괴 기능 제공
        - ~~로컬 토큰 사용 (성능 효율적)~~
            - ~~즉시 토큰 파괴 기능 제공하지 못함 (만료시 파괴 됨)~~
- 자원 서버 역할
    - 인가 직접 관리
    - 토큰 검증 요청
- JPA 사용
- Redis cache 사용

## TODO

- [x] 기본 인증 구현
- [x] OAuth2 Token 인증 방식 적용
- [x] JWT 인증 방식 적용
    - [x] 디비 이용
    - [x] 비대칭키 암호 적용
- [x] 인증 서버와 자원 서버 분리하기 (멀티 프로젝트)
- [x] 자원 서버 인증 테스트 확인
- [x] 자원 서버 : 토큰 검증 HTTP 요청으로 변경
- [ ] 인증 서버 : profile REST API 개발
    - 사용자 정보
    - 사용자 액세스 토큰 관리
- [ ] 인증 서버 토큰 관리 디비로 변경
- [ ] 인증 서버 토큰 관리 REST APIs 개발
    - [ ] Redis cache 사용
- [ ] 인증 서버 클라언드 관리 REST APIs 개발
    - [ ] Redis cache 사용

### Addition

- [x] Docker 서비스 구동
- [ ] Github Actions 적용
- [ ] Spring Security OAuth2 라이브러리 최신화 변경 (자원 서버)


## 참고

### 비대칭

```
keytool -genkeypair -alias syaku -keyalg RSA -keypass syaku@1234 -keystore authorization.jks -storepass syaku@pass1234
```

인증서와 공개키는 외부 폴더에서 관리되어야 합니다.

- storepass 는 키 저장소에 액세스하는 데 사용됩니다.
- keypass 는 특정 키 쌍의 개인 키에 액세스하는 데 사용됩니다.

### 공개키

```
keytool -list -rfc --keystore authorization.jks | openssl x509 -inform pem -pubkey > publicKey.txt
```

### docker compose test

gradle build ...

```
docker-compose up -d

// access token
curl -u clientId:1234 http://localhost:8080/oauth/token -d  "grant_type=password&username=test&password=1234"

```


### 링크

- https://docs.spring.io/spring-security-oauth2-boot/
- https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/
- https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/html5/
- https://projects.spring.io/spring-security-oauth/docs/oauth2.html

#### JWT
- https://www.baeldung.com/spring-security-oauth-jwt

#### Resource Server
- https://docs.spring.io/spring-security/site/docs/5.3.8.RELEASE/reference/html5/#oauth2resourceserver