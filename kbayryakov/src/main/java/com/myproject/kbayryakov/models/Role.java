package com.myproject.kbayryakov.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @NotBlank
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }
}
