package com.lillya.piemon.auth.authorities.data;

import com.lillya.piemon.auth.authorities.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
