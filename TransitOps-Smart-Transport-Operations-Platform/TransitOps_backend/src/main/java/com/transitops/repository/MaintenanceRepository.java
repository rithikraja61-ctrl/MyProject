package com.transitops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transitops.entity.MaintenanceLog;
import com.transitops.entity.MaintenanceStatus;

public interface MaintenanceRepository extends JpaRepository<MaintenanceLog, Long> {

	boolean existsByVehicleIdAndStatus(Long vehicleId, MaintenanceStatus status);
}
