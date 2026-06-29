package com.examen.payment_service.controller;

import com.examen.payment_service.dto.response.SeedFactureResponse;
import com.examen.payment_service.service.FactureSeederService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/factures")
public class FactureSeederController {

	private final FactureSeederService factureSeederService;

	public FactureSeederController(FactureSeederService factureSeederService) {
		this.factureSeederService = factureSeederService;
	}

	@PostMapping("/seed")
	public SeedFactureResponse seedFactures() {
		return factureSeederService.seedFactures();
	}
}
