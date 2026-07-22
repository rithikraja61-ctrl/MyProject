package com.transitops.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transitops.entity.Role;
import com.transitops.entity.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
