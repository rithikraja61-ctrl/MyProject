package com.transitops.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transitops.entity.Vehicle;
import com.transitops.entity.VehicleStatus;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	boolean existsByRegistrationNumber(String registrationNumber);

	boolean existsByRegistrationNumberAndIdNot(String registrationNumber, Long id);

	long countByStatus(VehicleStatus status);

	long countByStatusNot(VehicleStatus status);

	long countByTypeAndStatus(String type, VehicleStatus status);

	long countByTypeAndStatusNot(String type, VehicleStatus status);

	@Query("""
		SELECT COUNT(v) FROM Vehicle v
		WHERE v.status = :status
		AND (:type IS NULL OR v.type = :type)
		""")
	long countByStatusAndOptionalType(
		@Param("status") VehicleStatus status,
		@Param("type") String type
	);

	Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

	List<Vehicle> findByStatus(VehicleStatus status);

	List<Vehicle> findByStatusNot(VehicleStatus status);
}
