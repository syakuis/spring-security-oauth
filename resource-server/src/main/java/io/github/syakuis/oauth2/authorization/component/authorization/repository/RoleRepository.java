package io.github.syakuis.oauth2.authorization.component.authorization.repository;

import io.github.syakuis.oauth2.authorization.component.authorization.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
}
