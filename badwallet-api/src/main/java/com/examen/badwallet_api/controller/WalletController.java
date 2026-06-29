package com.examen.badwallet_api.controller;

import com.examen.badwallet_api.dto.request.CreateWalletRequest;
import com.examen.badwallet_api.dto.response.SeedWalletResponse;
import com.examen.badwallet_api.dto.response.WalletResponse;
import com.examen.badwallet_api.service.WalletService;
import com.examen.badwallet_api.service.WalletSeederService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/wallets")
public class WalletController {

	private final WalletService walletService;
	private final WalletSeederService walletSeederService;

	public WalletController(
			WalletService walletService,
			WalletSeederService walletSeederService) {
		this.walletService = walletService;
		this.walletSeederService = walletSeederService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public WalletResponse createWallet(@Valid @RequestBody CreateWalletRequest request) {
		return walletService.createWallet(request);
	}

	@PostMapping("/seed")
	public SeedWalletResponse seedWallets(
			@RequestParam(defaultValue = "10") @Min(1) int numWallets,
			@RequestParam(defaultValue = "100") @Min(0) int eventsPerWallet) {
		return walletSeederService.seedWallets(numWallets, eventsPerWallet);
	}
}
