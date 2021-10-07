package io.github.syakuis.oauth2.authorization.security;

public enum AuthorizedGrantType {
    AUTHORIZATION_CODE("authorization_code"),
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials"),
    IMPLICIT("implicit"),
    REFRESH_TOKEN("refresh_token");

    private final String value;

    AuthorizedGrantType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
