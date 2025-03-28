package com.myproject.kbayryakov.services;

import com.myproject.kbayryakov.models.Role;
import com.myproject.kbayryakov.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void seedRolesInDb() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            this.roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
            this.roleRepository.saveAndFlush(new Role("ROLE_USER"));
        }
    }

    public Set<Role> findAllRoles() {
        return new HashSet<>(this.roleRepository.findAll());
    }

    public Role findByAuthority (String authority) {
        return this.roleRepository.findByAuthority(authority);
    }
}
