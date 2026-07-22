package com.transitops.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.transitops.entity.Role;
import com.transitops.entity.RoleName;
import com.transitops.repository.RoleRepository;

@Configuration
public class RoleScopeUpdater {

	@Bean
	CommandLineRunner syncRoleScopes(RoleRepository roleRepository) {
		return args -> {
			for (var entry : RoleScopeDefinitions.all().entrySet()) {
				RoleName roleName = entry.getKey();
				List<String> canonical = entry.getValue();

				Role role = roleRepository.findByName(roleName).orElse(null);
				if (role == null) {
					roleRepository.save(new Role(roleName, new ArrayList<>(canonical)));
					continue;
				}

				if (!role.getScopes().equals(canonical)) {
					role.getScopes().clear();
					role.getScopes().addAll(canonical);
					roleRepository.save(role);
				}
			}
		};
	}
}
