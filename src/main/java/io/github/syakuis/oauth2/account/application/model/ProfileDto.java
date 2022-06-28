package io.github.syakuis.oauth2.account.application.model;

import io.github.syakuis.oauth2.account.domain.Profile;
import java.time.LocalDateTime;
import java.util.UUID;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ProfileDto implements Profile {
    private String username;
    private String name;
    private LocalDateTime registeredOn;
    private LocalDateTime updatedOn;
    private UUID uid;
}
