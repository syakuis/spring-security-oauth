package io.github.syakuis.oauth2.authorization.token.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-11-01
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Table(name = "oauth_access_token_mapping",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_oauth_access_token_mapping_authorization_id", columnNames = "authorization_id")
    },
    indexes = {
    @Index(name = "IDX_oauth_access_token_mapping_expired", columnList = "expired")
    }
)
@Entity
public class OAuthAccessTokenMappingEntity {
    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false, updatable = false)
    private String authorizationId;
    @Column(nullable = false, updatable = false)
    private String accessToken;
    @Column(nullable = false, updatable = false)
    private Integer expired;

    @Builder
    public OAuthAccessTokenMappingEntity(String authorizationId, String accessToken, Integer expired) {
        this.authorizationId = authorizationId;
        this.accessToken = accessToken;
        this.expired = expired;
    }
}
