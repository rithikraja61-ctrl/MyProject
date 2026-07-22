package com.transitops.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.DriverRequest;
import com.transitops.dto.DriverResponse;
import com.transitops.entity.Driver;
import com.transitops.exception.DuplicateResourceException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

	private final DriverRepository driverRepository;

	@Transactional
	public DriverResponse create(DriverRequest request) {
		String licenseNumber = request.getLicenseNumber().trim();
		if (driverRepository.existsByLicenseNumber(licenseNumber)) {
			throw new DuplicateResourceException("licenseNumber", "License number already exists");
		}

		Driver driver = new Driver();
		applyRequest(driver, request, licenseNumber);
		if (request.getStatus() != null) {
			driver.setStatus(request.getStatus());
		}

		return toResponse(driverRepository.save(driver));
	}

	@Transactional(readOnly = true)
	public List<DriverResponse> findAll() {
		return driverRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public DriverResponse findById(Long id) {
		return toResponse(getDriver(id));
	}

	@Transactional
	public DriverResponse update(Long id, DriverRequest request) {
		Driver driver = getDriver(id);
		String licenseNumber = request.getLicenseNumber().trim();
		if (driverRepository.existsByLicenseNumberAndIdNot(licenseNumber, id)) {
			throw new DuplicateResourceException("licenseNumber", "License number already exists");
		}

		applyRequest(driver, request, licenseNumber);
		if (request.getStatus() != null) {
			driver.setStatus(request.getStatus());
		}

		return toResponse(driverRepository.save(driver));
	}

	@Transactional
	public void delete(Long id) {
		if (!driverRepository.existsById(id)) {
			throw new ResourceNotFoundException("Driver not found: " + id);
		}
		driverRepository.deleteById(id);
	}

	private Driver getDriver(Long id) {
		return driverRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + id));
	}

	private void applyRequest(Driver driver, DriverRequest request, String licenseNumber) {
		driver.setName(request.getName().trim());
		driver.setLicenseNumber(licenseNumber);
		driver.setLicenseCategory(request.getLicenseCategory().trim());
		driver.setLicenseExpiryDate(request.getLicenseExpiryDate());
		driver.setContactNumber(request.getContactNumber().trim());
		driver.setSafetyScore(request.getSafetyScore());
	}

	private DriverResponse toResponse(Driver driver) {
		return DriverResponse.builder()
			.id(driver.getId())
			.name(driver.getName())
			.licenseNumber(driver.getLicenseNumber())
			.licenseCategory(driver.getLicenseCategory())
			.licenseExpiryDate(driver.getLicenseExpiryDate())
			.contactNumber(driver.getContactNumber())
			.safetyScore(driver.getSafetyScore())
			.status(driver.getStatus())
			.build();
	}
}
