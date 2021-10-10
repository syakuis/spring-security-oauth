package io.github.syakuis.oauth2.authorization.component.authorization.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"roleEntities"})
@Table(name = "role")
@Entity
public class RoleEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean disabled;

    @ManyToMany
    @JoinTable(name = "role_hierarchy",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "role_child_id"))
    private Set<RoleEntity> roleHierarchy;

    @Builder
    public RoleEntity(String name, boolean disabled, Set<RoleEntity> roleHierarchy) {
        this.name = name;
        this.disabled = disabled;
        this.roleHierarchy = roleHierarchy == null ? new HashSet<>() : new HashSet<>(roleHierarchy);
    }

    public void addRoleHierarchy(RoleEntity role) {
        if (this.roleHierarchy == null) {
            this.roleHierarchy = new HashSet<>();
        }

        this.roleHierarchy.add(role);
    }
}
