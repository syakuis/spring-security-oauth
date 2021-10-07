package io.github.syakuis.oauth2.authorization.component.member.service;

import io.github.syakuis.oauth2.authorization.component.authorization.entity.RoleEntity;
import io.github.syakuis.oauth2.authorization.component.member.entity.MemberEntity;
import io.github.syakuis.oauth2.authorization.component.member.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberUserDetailsService implements UserDetailsService {
    private final MessageSourceAccessor i18n;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(
        String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(i18n.getMessage("MemberUserDetailsService.memberNotFound", username)));

        Set<RoleEntity> roleEntities = memberEntity.getRoles();

        List<GrantedAuthority> grantedAuthorities = Collections.emptyList();
        if (roleEntities != null) {
            grantedAuthorities = roleEntities.stream()
                .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName())).collect(Collectors.toList());
        }

        return User.withUsername(memberEntity.getUsername())
//            .accountExpired(false)
//            .credentialsExpired(false)
            .password(memberEntity.getPassword())
            .authorities(grantedAuthorities)
            .accountLocked(memberEntity.isLocked())
            .disabled(memberEntity.isDisabled()).build();
    }
}
