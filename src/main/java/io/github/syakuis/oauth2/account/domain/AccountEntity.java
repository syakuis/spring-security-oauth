package io.github.syakuis.oauth2.account.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.UnaryOperator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-05-21
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
@Entity
@Table(
    name = "account",
    indexes = {
        @Index(name = "IDX_account_uid_and_deleted_1", columnList = "uid, deleted"),
        @Index(name = "IDX_account_username_and_deleted_1", columnList = "username, deleted"),
        @Index(name = "IDX_account_disabled_and_deleted_1", columnList = "disabled, deleted"),
        @Index(name = "IDX_account_blocked_and_deleted_1", columnList = "blocked, deleted"),
        @Index(name = "IDX_account_deleted_1", columnList = "deleted")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_account_uid_1", columnNames = "uid"),
        @UniqueConstraint(name = "UK_account_username_1", columnNames = "username")
    }
)
@SQLDelete(sql = """
    UPDATE account
        SET deleted = true
        WHERE id = ?
    """)
@Where(clause = "deleted = false")
public class AccountEntity implements Account, AccountPassword {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150, updatable = false)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String password;

    @Column(nullable = false, columnDefinition = "bit", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean disabled;

    @Column(nullable = false, columnDefinition = "bit", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean blocked;

    @Column(nullable = false, columnDefinition = "bit", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean deleted;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, length = 6)
    private LocalDateTime registeredOn;

    @Column(length = 6)
    private LocalDateTime updatedOn;

    @Column(nullable = false, updatable = false, length = 16, columnDefinition = "binary(16)")
    private UUID uid;

    @PrePersist
    public void prePersist() {
        this.uid = UUID.randomUUID();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    @Builder
    public AccountEntity(@NotBlank String username,
        @NotBlank String name, @NotBlank String password, boolean disabled, boolean blocked) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.disabled = disabled;
        this.blocked = blocked;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePassword(String password, UnaryOperator<String> passwordEncoder) {
        if (StringUtils.hasText(password)) {
            this.password = passwordEncoder.apply(password);
        }
    }

    public void updateProfile(AccountEntity accountEntity) {
        this.name = accountEntity.getName();
    }

    public void update(AccountEntity accountEntity) {
        this.name = accountEntity.getName();
        this.disabled = accountEntity.isDisabled();
        this.blocked = accountEntity.isBlocked();
    }
}
