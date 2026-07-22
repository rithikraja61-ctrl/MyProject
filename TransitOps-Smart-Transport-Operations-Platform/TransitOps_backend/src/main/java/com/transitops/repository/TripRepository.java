package com.transitops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transitops.entity.Trip;
import com.transitops.entity.TripStatus;
import com.transitops.entity.VehicleStatus;

public interface TripRepository extends JpaRepository<Trip, Long> {

	long countByStatus(TripStatus status);

	List<Trip> findByStatus(TripStatus status);

	@Query("""
		SELECT COUNT(t) FROM Trip t
		WHERE t.status = :tripStatus
		AND (:type IS NULL OR t.vehicle.type = :type)
		AND (:vehicleStatus IS NULL OR t.vehicle.status = :vehicleStatus)
		""")
	long countByStatusAndVehicleFilters(
		@Param("tripStatus") TripStatus tripStatus,
		@Param("type") String type,
		@Param("vehicleStatus") VehicleStatus vehicleStatus
	);
}
