package com.examen.badwallet_api.service;

import com.examen.badwallet_api.dto.request.CreateWalletRequest;
import com.examen.badwallet_api.dto.response.WalletResponse;

public interface WalletService {

	WalletResponse createWallet(CreateWalletRequest request);
}
