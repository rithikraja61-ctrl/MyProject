package com.transitops.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transitops.entity.Driver;
import com.transitops.entity.DriverStatus;
import com.transitops.entity.TripStatus;
import com.transitops.entity.VehicleStatus;

public interface DriverRepository extends JpaRepository<Driver, Long> {

	boolean existsByLicenseNumber(String licenseNumber);

	boolean existsByLicenseNumberAndIdNot(String licenseNumber, Long id);

	long countByStatus(DriverStatus status);

	long countByStatusIn(Collection<DriverStatus> statuses);

	List<Driver> findByStatusAndLicenseExpiryDateGreaterThanEqual(DriverStatus status, LocalDate minExpiry);

	@Query("""
		SELECT COUNT(DISTINCT d.id) FROM Trip t
		JOIN t.driver d
		WHERE t.status = :tripStatus
		AND (:type IS NULL OR t.vehicle.type = :type)
		AND (:vehicleStatus IS NULL OR t.vehicle.status = :vehicleStatus)
		""")
	long countDistinctDriversOnTripsForVehicleScope(
		@Param("tripStatus") TripStatus tripStatus,
		@Param("type") String type,
		@Param("vehicleStatus") VehicleStatus vehicleStatus
	);
}
