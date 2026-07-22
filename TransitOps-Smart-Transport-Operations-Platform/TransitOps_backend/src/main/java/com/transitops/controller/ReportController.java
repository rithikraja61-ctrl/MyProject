package com.transitops.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transitops.dto.ReportSummaryResponse;
import com.transitops.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_data:analytics')")
public class ReportController {

	private final ReportService reportService;

	@GetMapping("/summary")
	public ReportSummaryResponse summary() {
		return reportService.getSummary();
	}

	@GetMapping(value = "/export.csv", produces = "text/csv")
	public ResponseEntity<String> exportCsv() {
		String csv = reportService.exportCsv();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transitops-report.csv\"")
			.contentType(MediaType.parseMediaType("text/csv"))
			.body(csv);
	}
}
