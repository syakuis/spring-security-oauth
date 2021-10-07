package io.github.syakuis.oauth2.authorization.security;

public enum EncodeId {
    BCRYPT("bcrypt"), PBKDF2("pbkdf2"), SCRYPT("scrypt");

    private final String value;

    EncodeId(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
