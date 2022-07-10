package io.github.syakuis.identity.account.application.model;

import io.github.syakuis.identity.account.domain.Account;
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
public class AccountDto implements Account {
    private Long id;
    private String username;
    private String name;
    private boolean disabled;
    private boolean blocked;
    private LocalDateTime registeredOn;
    private LocalDateTime updatedOn;
    private UUID uid;
}
