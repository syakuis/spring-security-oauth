package io.github.syakuis.oauth2.account.application.model;

import io.github.syakuis.oauth2.account.domain.AccountEntity;
import java.util.Objects;
import java.util.function.UnaryOperator;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-06-28
 */
public interface AccountCommand {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    class Signup {
        private String username;
        private String name;
        private String password;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    class Profile {
        private String name;
        private String password;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    class Update {
        private String name;
        private String password;
        private boolean disabled;
        private boolean blocked;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    class ChangePassword {
        @NotBlank
        private String currentPassword;
        @NotBlank
        private String newPassword;
        @NotBlank
        private String newPasswordConfirm;

        public boolean matches() {
            return Objects.equals(newPassword, newPasswordConfirm);
        }
    }
}
