
## Configuration

### jks 인증 발급

비대칭

```
keytool -genkeypair -alias syaku -keyalg RSA -keystore authorization.jks -storepass storepass
```

- storepass 는 키 저장소에 액세스하는 데 사용
- keypass 는 특정 키 쌍의 개인 키에 액세스하는 데 사용

공개키

```
keytool -list -rfc --keystore authorization.jks | openssl x509 -inform pem -pubkey > publicKey.txt
```