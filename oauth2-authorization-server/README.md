## jks 인증 발급

### 비대칭키 생성하기

```
keytool -genkeypair -alias syaku -keyalg RSA -keystore authorization.jks -storepass storepass
```

`storepass`는 키 저장소에 접근할때 사용하는 암호입니다.

### 공개키 생성하기

```
keytool -list -rfc --keystore authorization.jks | openssl x509 -inform pem -pubkey > publicKey.txt
```

`storepass` 암호를 입력해야 합니다.