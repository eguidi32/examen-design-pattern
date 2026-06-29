package com.examen.badwallet_api.controller;

import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import com.examen.badwallet_api.service.ExternalFactureService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/factures")
public class ExternalFactureController {

	private final ExternalFactureService externalFactureService;

	public ExternalFactureController(ExternalFactureService externalFactureService) {
		this.externalFactureService = externalFactureService;
	}

	@GetMapping("/{walletCode}/current")
	public List<ExternalFactureResponse> getCurrentFactures(
			@PathVariable String walletCode,
			@RequestParam(required = false) String unite) {
		return externalFactureService.getCurrentFactures(walletCode, unite);
	}

	@GetMapping("/{walletCode}/periode")
	public List<ExternalFactureResponse> getFacturesByPeriod(
			@PathVariable String walletCode,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
		return externalFactureService.getFacturesByPeriod(walletCode, debut, fin);
	}
}
