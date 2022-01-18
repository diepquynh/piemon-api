package com.lillya.piemon.auth.authorities.data;

import com.lillya.piemon.auth.authorities.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
