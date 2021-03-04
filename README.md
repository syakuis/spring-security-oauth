# Authorization Server

## Objective

JWT 인증 서버 개발.

## TODO

- [x] 기본 인증 구현
- [x] OAuth2 Token 인증 방식 적용
- [x] JWT 인증 방식 적용
    - [x] 디비 이용
    - [x] 비대칭키 암호 적용
- [x] 인증 서버와 자원 서버 분리하기 (멀티 프로젝트)
- [ ] Resource Server 테스트 확인
- [ ] Token 을 관리할 수 있는 기능 제공
    - [ ] 사용자가 인증한 토큰 목록 보기 (삭제)
    - [ ] 만료일과 관계없이 토큰 블록 처리
    - [ ] 블록 처리된 토큰은 만료일이 지나면 삭제되어야 함.
- [ ] Spring Security OAuth 내장 쿼리사용하지 않고 직접 JPA 구현
- [ ] Redis 사용 인증 정보 캐싱 처리

### Addition

- [x] Docker 서비스 구동
- [ ] Github Actions 적용


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