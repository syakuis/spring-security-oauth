# Spring Gradle Template

## Installing Gradle 6.5

https://docs.gradle.org/current/userguide/installation.html

spring boot 2.3.x 부터 gradle 6.x 버전 필요

```
$ ./gradlew wrapper --gradle-version=6.5
```

## 개발

기본적으로 개발 모드 profile 의 설정으로 구동됩니다.

개인적으로 로컬 설정이 필요한 경우 `application-default.yml` 파일을 생성하여 사용하세요.

## 테스트

참고: https://www.jetbrains.com/help/idea/work-with-tests-in-gradle.html#configure_gradle_test_runner

테스트 실행시 spring profile 이 자동으로 test 설정되도록 아래와 같이 설정이 필요합니다.

```
IntelliJ IDEA > Build, Execution, Deployment > Build Tools > Gradle 메뉴에서

Build and run using: Gradle
Run tests using: Gradle
```