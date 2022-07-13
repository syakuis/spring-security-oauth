package io.github.syakuis.identity.clientregistration.application;

import java.util.List;
import java.util.Set;
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
        List<String> resourceIds;
        @NotEmpty
        List<String> scopes;
        @NotEmpty
        List<String> authorizedGrantTypes;
        Set<String> webServerRedirectUri;
        List<GrantedAuthority> authorities;
        // todo 최소값 설정할 것
        @NotNull
        Integer accessTokenValidity;
        // todo 최소값 설정할 것
        @NotNull
        Integer refreshTokenValidity;
        String additionalInformation;
        Set<String> autoApprove;
    }
}
