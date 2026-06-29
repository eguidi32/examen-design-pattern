package com.examen.badwallet_api.service.impl;

import com.examen.badwallet_api.dto.request.CreateWalletRequest;
import com.examen.badwallet_api.dto.request.DepositRequest;
import com.examen.badwallet_api.dto.response.TransactionResponse;
import com.examen.badwallet_api.dto.response.WalletBalanceResponse;
import com.examen.badwallet_api.dto.response.WalletResponse;
import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;
import com.examen.badwallet_api.exception.BusinessException;
import com.examen.badwallet_api.patterns.strategy.DepositStrategy;
import com.examen.badwallet_api.patterns.strategy.DepositStrategyFactory;
import com.examen.badwallet_api.repository.TransactionRepository;
import com.examen.badwallet_api.repository.WalletRepository;
import com.examen.badwallet_api.service.WalletService;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WalletServiceImpl implements WalletService {

	private final WalletRepository walletRepository;
	private final TransactionRepository transactionRepository;
	private final DepositStrategyFactory depositStrategyFactory;

	public WalletServiceImpl(
			WalletRepository walletRepository,
			TransactionRepository transactionRepository,
			DepositStrategyFactory depositStrategyFactory) {
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
		this.depositStrategyFactory = depositStrategyFactory;
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

	@Override
	@Transactional
	public TransactionResponse deposit(Long id, DepositRequest request) {
		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Le montant du depot doit etre superieur a 0");
		}

		Wallet wallet = walletRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Aucun wallet trouve avec cet id"));

		DepositStrategy strategy = depositStrategyFactory.getStrategy(request.getPaymentMethod());
		Transaction transaction = strategy.deposit(wallet, request.getAmount());

		Wallet savedWallet = walletRepository.saveAndFlush(wallet);
		Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);

		return toTransactionResponse(savedTransaction, savedWallet);
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

	private TransactionResponse toTransactionResponse(Transaction transaction, Wallet wallet) {
		return new TransactionResponse(
				transaction.getId(),
				wallet.getId(),
				transaction.getAmount(),
				wallet.getBalance(),
				transaction.getPaymentMethod(),
				transaction.getType(),
				transaction.getStatus(),
				transaction.getReference(),
				transaction.getCreatedAt(),
				"Depot effectue avec succes");
	}
}
