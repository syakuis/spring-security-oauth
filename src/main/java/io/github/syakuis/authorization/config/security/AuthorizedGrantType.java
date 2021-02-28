package io.github.syakuis.authorization.config.security;

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

    public String getValue() {
        return value;
    }
}
