package ru.plorum.reporterinitializr.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.plorum.reporterinitializr.model.Privilege;
import ru.plorum.reporterinitializr.model.Role;
import ru.plorum.reporterinitializr.model.User;
import ru.plorum.reporterinitializr.repository.PrivilegeRepository;
import ru.plorum.reporterinitializr.repository.RoleRepository;
import ru.plorum.reporterinitializr.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SetupDataLoader {

    private final PrivilegeRepository privilegeRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${administrator.login}")
    private String login;

    @Value("${administrator.password}")
    private String password;

    @PostConstruct
    protected void initialize() {
        if (!CollectionUtils.isEmpty(privilegeRepository.findAll())) return;
        final Map<String, String> privilegesMap = new HashMap<>() {{
            put("USER_CREATE_PRIVILEGE", "Создание пользователей");
            put("USER_EDIT_PRIVILEGE", "Редактирование пользователей");
        }};
        final Set<Privilege> adminPrivileges = privilegesMap.entrySet().stream()
                .map(this::createPrivilegeIfNotFound)
                .filter(p -> Arrays.asList("USER_CREATE_PRIVILEGE", "USER_EDIT_PRIVILEGE").contains(p.getName()))
                .collect(Collectors.toSet());
        createAdminIfNotFound(adminPrivileges);
    }

    @Transactional
    public void createAdminIfNotFound(final Set<Privilege> privileges) {
        final List<User> users = userRepository.findByLoginLike(login);
        final User user = CollectionUtils.isEmpty(users) ? new User() : users.iterator().next();
        user.setId(UUID.randomUUID());
        user.setLogin(login);
        user.setName(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(createAdminRoleIfNotFound(privileges)));
        user.setCreatedOn(LocalDateTime.now());
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public Role createAdminRoleIfNotFound(final Set<Privilege> privileges) {
        final String name = "ROLE_ADMIN";
        final Role role = roleRepository.findByName(name);
        if (Objects.nonNull(role)) return role;
        final Role roleToCreate = new Role(UUID.randomUUID());
        roleToCreate.setName(name);
        roleToCreate.setDescription("Администратор");
        roleToCreate.setPrivileges(privileges);
        return roleRepository.save(roleToCreate);
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final Map.Entry<String, String> data) {
        final Privilege privilege = privilegeRepository.findByName(data.getKey());
        if (Objects.nonNull(privilege)) return privilege;
        final Privilege privilegeToCreate = new Privilege();
        privilegeToCreate.setId(UUID.randomUUID());
        privilegeToCreate.setName(data.getKey());
        privilegeToCreate.setDescription(data.getValue());
        return privilegeRepository.save(privilegeToCreate);
    }

}
