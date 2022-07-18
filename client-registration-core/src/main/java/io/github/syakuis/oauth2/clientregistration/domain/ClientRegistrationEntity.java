package io.github.syakuis.oauth2.clientregistration.domain;

import io.github.syakuis.oauth2.core.AuthorizedGrantType;
import io.github.syakuis.oauth2.core.jpa.converter.AuthorizedGrantTypeToStringConverter;
import io.github.syakuis.oauth2.core.jpa.converter.GrantedAuthorityToStringConverter;
import io.github.syakuis.oauth2.core.jpa.converter.JsonToStringConverter;
import io.github.syakuis.oauth2.core.jpa.converter.SetToStringConverter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(
    name = "client_registration",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "clientId")
    }
)
public class ClientRegistrationEntity implements ClientRegistration {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String applicationName;

    @Column
    @Convert(converter = SetToStringConverter.class)
    private Set<String> resourceId;

    @Column(nullable = false)
    @Convert(converter = SetToStringConverter.class)
    private Set<String> scope;

    @Column(nullable = false)
    @Convert(converter = AuthorizedGrantTypeToStringConverter.class)
    private Set<AuthorizedGrantType> authorizedGrantType;

    @Column(nullable = false)
    @Convert(converter = SetToStringConverter.class)
    private Set<String> webServerRedirectUri;

    @Column
    @Convert(converter = GrantedAuthorityToStringConverter.class)
    private Set<GrantedAuthority> authority;

    @Column(nullable = false)
    private Integer accessTokenValidity;

    @Column(nullable = false)
    private Integer refreshTokenValidity;

    @Column
    @Convert(converter = JsonToStringConverter.class)
    private String additionalInformation;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, length = 6, columnDefinition="datetime(6)")
    private LocalDateTime registeredOn;

    @Column(nullable = false)
    private String registeredBy;

    @Column(length = 6, columnDefinition="datetime(6)")
    private LocalDateTime updatedOn;

    @Builder
    public ClientRegistrationEntity(String clientId, String clientSecret, String applicationName, Set<String> resourceId, Set<String> scope, Set<AuthorizedGrantType> authorizedGrantType, Set<String> webServerRedirectUri, Set<GrantedAuthority> authority, Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, String registeredBy) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.applicationName = applicationName;
        this.resourceId = resourceId;
        this.scope = scope;
        this.authorizedGrantType = authorizedGrantType;
        this.webServerRedirectUri = webServerRedirectUri;
        this.authority = authority;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.additionalInformation = additionalInformation;
        this.registeredBy = registeredBy;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    public void update(ClientRegistrationEntity clientRegistrationEntity) {
        this.resourceId = clientRegistrationEntity.getResourceId();
        this.scope = clientRegistrationEntity.getScope();
        this.authorizedGrantType = clientRegistrationEntity.getAuthorizedGrantType();
        this.webServerRedirectUri = clientRegistrationEntity.getWebServerRedirectUri();
        this.authority = clientRegistrationEntity.getAuthority();
        this.accessTokenValidity = clientRegistrationEntity.getAccessTokenValidity();
        this.refreshTokenValidity = clientRegistrationEntity.getRefreshTokenValidity();
        this.additionalInformation = clientRegistrationEntity.getAdditionalInformation();
    }

    public void refreshingClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ClientRegistrationEntity that = (ClientRegistrationEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
