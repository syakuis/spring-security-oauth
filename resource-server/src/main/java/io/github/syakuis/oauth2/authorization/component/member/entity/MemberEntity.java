package io.github.syakuis.oauth2.authorization.component.member.entity;

import io.github.syakuis.oauth2.authorization.component.authorization.entity.RoleEntity;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
@Getter
@Entity
@Table(name = "member")
@SQLDelete(sql =
    "UPDATE member " +
        "SET deleted = true " +
        "WHERE id = ?")
@Where(clause = "deleted = false")
public class MemberEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean disabled;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean locked;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDatetime;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime modificationDatetime;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean deleted;

    @ManyToMany
    @JoinTable(name = "member_role",
        joinColumns = {@JoinColumn(name = "member_id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<RoleEntity> roles;

    @Builder
    public MemberEntity(@NotBlank String username,
        @NotBlank String name, @NotBlank String password, boolean disabled, boolean locked,
        Set<RoleEntity> roles) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.disabled = disabled;
        this.locked = locked;
        this.roles = roles == null ? new HashSet<>() : new HashSet<>(roles);
    }

    public void addRole(RoleEntity roleEntity) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }

        this.roles.add(roleEntity);
    }
}
