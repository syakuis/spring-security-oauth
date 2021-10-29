package io.github.syakuis.oauth2.clientregistration.domain;

import io.github.syakuis.oauth2.configuration.jpa.converter.GrantedAuthorityToStringConverter;
import io.github.syakuis.oauth2.configuration.jpa.converter.JsonToStringConverter;
import io.github.syakuis.oauth2.configuration.jpa.converter.ListToStringConverter;
import io.github.syakuis.oauth2.configuration.jpa.converter.SetToStringConverter;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

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
        @UniqueConstraint(name = "UK_client_registration_client_id", columnNames = "clientId")
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

    @Column
    @Convert(converter = ListToStringConverter.class)
    private List<String> resourceIds;

    @Column(name = "scope", nullable = false)
    @Convert(converter = ListToStringConverter.class)
    private List<String> scopes;

    @Column
    @Convert(converter = ListToStringConverter.class)
    private List<String> authorizedGrantTypes;

    @Column
    @Convert(converter = SetToStringConverter.class)
    private Set<String> webServerRedirectUri;

    @Column
    @Convert(converter = GrantedAuthorityToStringConverter.class)
    private List<GrantedAuthority> authorities;

    @Column(nullable = false, length = 11)
    private Integer accessTokenValidity;

    @Column(nullable = false, length = 11)
    private Integer refreshTokenValidity;

    @Column
    @Convert(converter = JsonToStringConverter.class)
    private String additionalInformation;

    @Column(name = "autoapprove")
    @Convert(converter = SetToStringConverter.class)
    private Set<String> autoApprove;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, length = 6)
    private LocalDateTime registeredOn;

    @Column(insertable = false, length = 6)
    private LocalDateTime updatedOn;

    @Builder
    public ClientRegistrationEntity(String clientId, String clientSecret, List<String> resourceIds, List<String> scopes, List<String> authorizedGrantTypes, Set<String> webServerRedirectUri, List<GrantedAuthority> authorities, Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, Set<String> autoApprove) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.resourceIds = resourceIds;
        this.scopes = scopes;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.webServerRedirectUri = webServerRedirectUri;
        this.authorities = authorities;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.additionalInformation = additionalInformation;
        this.autoApprove = autoApprove;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    public void updateClientSecret(UnaryOperator<String> passwordEncoder, String clientSecret) {
        this.clientSecret = passwordEncoder.apply(clientSecret);
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
        return Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
