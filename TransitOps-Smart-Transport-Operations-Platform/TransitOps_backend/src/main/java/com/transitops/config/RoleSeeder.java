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
public class RoleSeeder {

	@Bean
	CommandLineRunner seedRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.count() > 0) {
				return;
			}

			List<Role> roles = new ArrayList<>();
			for (var entry : RoleScopeDefinitions.all().entrySet()) {
				roles.add(new Role(entry.getKey(), new ArrayList<>(entry.getValue())));
			}
			roleRepository.saveAll(roles);
		};
	}
}
