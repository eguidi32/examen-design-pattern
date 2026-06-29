package com.examen.badwallet_api.controller;

import com.examen.badwallet_api.dto.response.SeedWalletResponse;
import com.examen.badwallet_api.service.WalletSeederService;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/wallets")
public class WalletController {

	private final WalletSeederService walletSeederService;

	public WalletController(WalletSeederService walletSeederService) {
		this.walletSeederService = walletSeederService;
	}

	@PostMapping("/seed")
	public SeedWalletResponse seedWallets(
			@RequestParam(defaultValue = "10") @Min(1) int numWallets,
			@RequestParam(defaultValue = "100") @Min(0) int eventsPerWallet) {
		return walletSeederService.seedWallets(numWallets, eventsPerWallet);
	}
}
