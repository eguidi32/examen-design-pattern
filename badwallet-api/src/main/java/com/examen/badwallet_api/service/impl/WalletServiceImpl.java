package com.examen.badwallet_api.service.impl;

import com.examen.badwallet_api.dto.request.CreateWalletRequest;
import com.examen.badwallet_api.dto.response.WalletBalanceResponse;
import com.examen.badwallet_api.dto.response.WalletResponse;
import com.examen.badwallet_api.entity.Wallet;
import com.examen.badwallet_api.exception.BusinessException;
import com.examen.badwallet_api.repository.WalletRepository;
import com.examen.badwallet_api.service.WalletService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WalletServiceImpl implements WalletService {

	private final WalletRepository walletRepository;

	public WalletServiceImpl(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	@Override
	@Transactional
	public WalletResponse createWallet(CreateWalletRequest request) {
		validateUniqueWallet(request);

		Wallet wallet = new Wallet();
		wallet.setPhoneNumber(request.getPhoneNumber());
		wallet.setEmail(request.getEmail());
		wallet.setBalance(request.getInitialBalance());
		wallet.setCode(request.getCode());
		wallet.setCurrency(request.getCurrency());

		return toResponse(walletRepository.save(wallet));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<WalletResponse> listWallets(Pageable pageable) {
		return walletRepository.findAll(pageable).map(this::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public WalletResponse getWalletByPhoneNumber(String phoneNumber) {
		return toResponse(findWalletByPhoneNumber(phoneNumber));
	}

	@Override
	@Transactional(readOnly = true)
	public WalletBalanceResponse getWalletBalance(String phoneNumber) {
		return toBalanceResponse(findWalletByPhoneNumber(phoneNumber));
	}

	private void validateUniqueWallet(CreateWalletRequest request) {
		if (walletRepository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new BusinessException("Un wallet existe deja avec ce numero de telephone");
		}
		if (walletRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException("Un wallet existe deja avec cet email");
		}
		if (walletRepository.existsByCode(request.getCode())) {
			throw new BusinessException("Un wallet existe deja avec ce code");
		}
	}

	private Wallet findWalletByPhoneNumber(String phoneNumber) {
		return walletRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Aucun wallet trouve avec ce numero de telephone"));
	}

	private WalletResponse toResponse(Wallet wallet) {
		return new WalletResponse(
				wallet.getId(),
				wallet.getPhoneNumber(),
				wallet.getEmail(),
				wallet.getBalance(),
				wallet.getCode(),
				wallet.getCurrency(),
				wallet.getCreatedAt(),
				wallet.getUpdatedAt());
	}

	private WalletBalanceResponse toBalanceResponse(Wallet wallet) {
		return new WalletBalanceResponse(
				wallet.getPhoneNumber(),
				wallet.getCode(),
				wallet.getBalance(),
				wallet.getCurrency());
	}
}
