package io.github.syakuis.identity.clientregistration.application;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
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
        List<String> resourceIds;
        @NotEmpty
        List<String> scopes;
        @NotEmpty
        List<String> authorizedGrantTypes;
        Set<String> webServerRedirectUri;
        List<GrantedAuthority> authorities;
        Integer accessTokenValidity;
        Integer refreshTokenValidity;
        String additionalInformation;
        Set<String> autoApprove;
    }
}
