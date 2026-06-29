package com.examen.badwallet_api.service;

import com.examen.badwallet_api.dto.response.SeedWalletResponse;

public interface WalletSeederService {

	SeedWalletResponse seedWallets(int numWallets, int eventsPerWallet);
}
