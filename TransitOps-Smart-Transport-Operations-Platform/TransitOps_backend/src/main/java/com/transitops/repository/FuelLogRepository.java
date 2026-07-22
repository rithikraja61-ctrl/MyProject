package com.transitops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transitops.entity.FuelLog;

public interface FuelLogRepository extends JpaRepository<FuelLog, Long> {

	List<FuelLog> findAllByOrderByLogDateDescIdDesc();
}
