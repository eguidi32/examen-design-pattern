package com.examen.badwallet_api.service;

import com.examen.badwallet_api.dto.request.CreateWalletRequest;
import com.examen.badwallet_api.dto.request.DepositRequest;
import com.examen.badwallet_api.dto.request.PayCurrentFactureRequest;
import com.examen.badwallet_api.dto.request.TransferRequest;
import com.examen.badwallet_api.dto.request.WithdrawRequest;
import com.examen.badwallet_api.dto.response.BillPaymentResponse;
import com.examen.badwallet_api.dto.response.TransactionResponse;
import com.examen.badwallet_api.dto.response.WalletBalanceResponse;
import com.examen.badwallet_api.dto.response.WalletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {

	WalletResponse createWallet(CreateWalletRequest request);

	Page<WalletResponse> listWallets(Pageable pageable);

	WalletResponse getWalletByPhoneNumber(String phoneNumber);

	WalletBalanceResponse getWalletBalance(String phoneNumber);

	TransactionResponse deposit(Long id, DepositRequest request);

	TransactionResponse withdraw(WithdrawRequest request);

	TransactionResponse transfer(TransferRequest request);

	BillPaymentResponse payCurrentFacture(PayCurrentFactureRequest request);
}
