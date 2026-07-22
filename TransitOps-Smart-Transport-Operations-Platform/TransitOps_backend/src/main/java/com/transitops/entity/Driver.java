package com.transitops.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, unique = true, length = 50)
	private String licenseNumber;

	@Column(nullable = false, length = 20)
	private String licenseCategory;

	@Column(nullable = false)
	private LocalDate licenseExpiryDate;

	@Column(nullable = false, length = 20)
	private String contactNumber;

	@Column(nullable = false)
	private Integer safetyScore;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private DriverStatus status = DriverStatus.AVAILABLE;
}
