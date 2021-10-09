package io.github.syakuis.oauth2.authorization.account.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-09
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(
    name = "account",
    indexes = {
        @Index(name = "IDX_account_uid_and_deleted", columnList = "uid, deleted"),
        @Index(name = "IDX_account_username_and_deleted", columnList = "username, deleted"),
        @Index(name = "IDX_account_deleted", columnList = "deleted")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_account_uid", columnNames = "uid"),
        @UniqueConstraint(name = "UK_account_username", columnNames = "username")
    }
)
@SQLDelete(sql =
    "UPDATE account " +
        "SET deleted = true " +
        "WHERE id = ?")
@Where(clause = "deleted = false")
public class AccountEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150, updatable = false)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, updatable = false, length = 16, columnDefinition = "binary(16)")
    private UUID uid;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean disabled;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean blocked;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean deleted;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, length = 6)
    private LocalDateTime registeredOn;

    @Column(insertable = false, length = 6)
    private LocalDateTime updatedOn;

    @Builder
    public AccountEntity(String username, String name, String password, boolean disabled,
        boolean blocked) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.disabled = disabled;
        this.blocked = blocked;
    }

    @PrePersist
    public void prePersist() {
        this.uid = UUID.randomUUID();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
