package com.transitops.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleName name;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "role_scopes", joinColumns = @JoinColumn(name = "role_id"))
	@Column(name = "scope", nullable = false)
	private List<String> scopes = new ArrayList<>();

	public Role(RoleName name, List<String> scopes) {
		this.name = name;
		this.scopes = scopes;
	}
}
