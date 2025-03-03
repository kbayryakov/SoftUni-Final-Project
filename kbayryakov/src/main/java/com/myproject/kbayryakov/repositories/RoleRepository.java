package com.myproject.kbayryakov.repositories;

import com.myproject.kbayryakov.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByAuthority(String authority);
}
