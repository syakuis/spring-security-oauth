package io.github.syakuis.oauth2.clientregistration.application;

import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-12
 */
public interface ClientRegistrationRequestBody {

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    class Register {
        @NotEmpty
        String applicationName;
        Set<String> resourceId;
        @NotEmpty
        Set<String> scope;
        @NotEmpty
        Set<AuthorizedGrantType> authorizedGrantType;
        @NotEmpty
        Set<String> webServerRedirectUri;
        Set<GrantedAuthority> authority;
        @NotNull
        @Min(1000)
        Integer accessTokenValidity;
        @NotNull
        @Min(2000)
        Integer refreshTokenValidity;
        String additionalInformation;
    }
}
