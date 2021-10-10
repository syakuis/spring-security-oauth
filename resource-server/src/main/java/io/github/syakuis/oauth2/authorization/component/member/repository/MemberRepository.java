package io.github.syakuis.oauth2.authorization.component.member.repository;

import io.github.syakuis.oauth2.authorization.component.member.entity.MemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository <MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);
}
