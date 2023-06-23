package ru.plorum.reporterinitializr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.plorum.reporterinitializr.model.Role;
import ru.plorum.reporterinitializr.repository.RoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Long countAll() {
        return roleRepository.countAll();
    }

}
