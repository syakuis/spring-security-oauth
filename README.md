# Authorization Server

## Objective

JWT 인증 서버 개발.

## TODO

- 기본 인증 구현
- OAuth2 Token 인증 방식 적용
- JWT 인증 방식 적용
- Resource Server 테스트 확인 (서버 분리 필수)
- 인증 저장을 내장 저장소를 DB 사용
- Spring Security OAuth 내장 쿼리사용하지 않고 직접 JPA 구현
- Redis 사용 인증 정보 캐싱 처리

### Addition

- Docker 서비스 구동
- Github Actions 적용

## Done

- 기본 인증 구현
    - form 인증, Http Basic 인증
- OAuth2 Token 인증 방식 적용
- JWT 인증 방식 적용

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


### 링크

- https://docs.spring.io/spring-security-oauth2-boot/
- https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/
- https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/html5/
- https://projects.spring.io/spring-security-oauth/docs/oauth2.html

#### JWT
- https://www.baeldung.com/spring-security-oauth-jwt