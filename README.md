# Spring Security OAuth2 - Authorization Server

Spring Security OAuth2를 기반으로 인증/인가를 제공하기 위한 목적으로 개발된 Identity Provider(IDP) Service 입니다.

Spring Security팀에서 [spring security oauth](https://github.com/spring-attic/spring-security-oauth) 프로젝트를 더 이상 지원하지 않기로 하였습니다. 하지만 사용하는 데에 문제가 없고 많은 커뮤니티 정보를 보유하고 있는 프로젝트라서 판단하여 레거시 버전으로 IDP를 개발하게 되었습니다.

추후 고도화된 [Spring Security Authorization](https://github.com/spring-projects/spring-authorization-server) 버전으로 제공할 계획입니다.

## 서비스 소개

### Account

계정을 관리하는 API 서비스입니다. 사용사가 인증을 받기 위한 최소한의 필수 정보를 담고있습니다. `username`이 일반적으로 `id` 정보에 해당합니다.

`account-core` 모듈은 다른 서비스에서도 사용될 클래스를 제공하기 위한 공통적인 모듈입니다.

### Client Registration

특정 애플리케이션(이하 웹서비스, 모바일 애플리케이션, 클라이언트 애플리케이션)에서 인증과 인가를 받기 위해서는 사전에 애플리케이션 정보를 등록해야 합니다.
애플리케이션 정보를 관리하는 API 서비스입니다.

`client-registration-core` 모듈은 다른 서비스에서도 사용될 클래스를 제공하기 위한 공통적인 모듈입니다.

### Authorization

인증/인가를 제공하는 API 서비스입니다. 인증 방식은 `authorization code`, `client credentials`, `password` 총 3가지를 제공합니다.

## 개발 사양

- java 17
- gradle 7
- spring boot
- spring security oauth 2.5.2
- spring data jpa & mysql 8
- redis 7

## 설치

## 자원 서버 인증 설정

Spring Configuration

```java
@EnableResourceServer
@SpringBootApplication
public class Application {
}

or

@EnableResourceServer
@EnableWebSecurity
public class WebMvcSecurityConfiguration extends WebSecurityConfigurerAdapter implements ResourceServerConfigurer {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(
                authorize -> authorize
                    .anyRequest().authenticated());
    }
}
```

### 불투명 토큰(opaque token) 인증 설정

https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/opaque-token.html

application.yml

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        opaque-token:
          introspection-uri: https://idp.example.com/introspect
          client-id: client
          client-secret: secret
```

### jwt 토큰 인증 설정

https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html

application.yml

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri:
```

### 인증 후 인증 속성 조회

https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/opaque-token.html#oauth2resourceserver-opaque-attributes

```java
@GetMapping("/foo")
public String foo(BearerTokenAuthentication authentication) {
    return authentication.getTokenAttributes().get("sub") + " is the subject";
}

--- loggin ---
BearerTokenAuthentication [Principal=org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal@695466d8, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=127.0.0.1, SessionId=null], Granted Authorities=[]]

or

@GetMapping("/foo")
public String foo(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
    return principal.getAttribute("sub") + " is the subject";
}
```
