package com.examen.payment_service.controller;

import com.examen.payment_service.dto.response.FactureResponse;
import com.examen.payment_service.enums.ServiceName;
import com.examen.payment_service.service.FactureService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

	private final FactureService factureService;

	public FactureController(FactureService factureService) {
		this.factureService = factureService;
	}

	@GetMapping("/{walletCode}/current")
	public List<FactureResponse> getCurrentUnpaidFactures(
			@PathVariable String walletCode,
			@RequestParam(required = false) ServiceName unite) {
		return factureService.findCurrentUnpaidFactures(walletCode, unite);
	}

	@GetMapping("/{walletCode}/periode")
	public List<FactureResponse> getUnpaidFacturesByPeriod(
			@PathVariable String walletCode,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
		return factureService.findUnpaidFacturesByPeriod(walletCode, debut, fin);
	}
}
