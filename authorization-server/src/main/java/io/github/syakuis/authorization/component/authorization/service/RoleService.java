package io.github.syakuis.authorization.component.authorization.service;

import io.github.syakuis.authorization.component.authorization.entity.RoleEntity;
import io.github.syakuis.authorization.component.authorization.repository.RoleRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;

    public String getHierarchy() {
        Map<String, List<String>> result = new HashMap<>();
        StreamSupport.stream(roleRepository.findAll().spliterator(), false)
            .forEach(roleEntity -> {
                if (!roleEntity.getRoleHierarchy().isEmpty()) {
                    List<String> roles = roleEntity.getRoleHierarchy().stream()
                        .map(RoleEntity::getName).collect(Collectors.toList());
                    result.put(roleEntity.getName(), new ArrayList<>(roles));
                }
            });

        return  result.isEmpty() ? "" : RoleHierarchyUtils.roleHierarchyFromMap(result);
    }
}
