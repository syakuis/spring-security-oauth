package io.github.syakuis.oauth2.authorization.component.member.repository;

import io.github.syakuis.oauth2.authorization.component.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository <MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);
}
