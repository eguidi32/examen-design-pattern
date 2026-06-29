package com.examen.badwallet_api.service.impl;

import com.examen.badwallet_api.dto.response.SeedWalletResponse;
import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;
import com.examen.badwallet_api.enums.Currency;
import com.examen.badwallet_api.enums.PaymentMethod;
import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;
import com.examen.badwallet_api.repository.TransactionRepository;
import com.examen.badwallet_api.repository.WalletRepository;
import com.examen.badwallet_api.service.WalletSeederService;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletSeederServiceImpl implements WalletSeederService {

	private final WalletRepository walletRepository;
	private final TransactionRepository transactionRepository;

	public WalletSeederServiceImpl(
			WalletRepository walletRepository,
			TransactionRepository transactionRepository) {
		this.walletRepository = walletRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	@Transactional
	public SeedWalletResponse seedWallets(int numWallets, int eventsPerWallet) {
		int walletsCreated = 0;
		int transactionsCreated = 0;

		for (int walletIndex = 1; walletIndex <= numWallets; walletIndex++) {
			Wallet wallet = walletRepository.findByCode(formatCode(walletIndex))
					.orElseGet(Wallet::new);

			boolean isNewWallet = wallet.getId() == null;
			wallet.setPhoneNumber(formatPhoneNumber(walletIndex));
			wallet.setCode(formatCode(walletIndex));
			wallet.setEmail(formatEmail(walletIndex));
			wallet.setCurrency(Currency.XOF);
			wallet.setBalance(BigDecimal.ZERO);

			wallet = walletRepository.save(wallet);
			if (isNewWallet) {
				walletsCreated++;
			} else {
				transactionRepository.deleteByWallet(wallet);
			}

			BigDecimal balance = BigDecimal.ZERO;
			for (int eventIndex = 1; eventIndex <= eventsPerWallet; eventIndex++) {
				Transaction transaction = buildTransaction(wallet, walletIndex, eventIndex, balance);
				transactionRepository.save(transaction);
				transactionsCreated++;

				if (transaction.getStatus() == TransactionStatus.SUCCESS) {
					balance = applyTransaction(balance, transaction);
				}
			}

			if (balance.compareTo(BigDecimal.ZERO) <= 0) {
				balance = baseSeedAmount(walletIndex);
			}
			wallet.setBalance(balance);
			walletRepository.save(wallet);
		}

		return new SeedWalletResponse(
				numWallets,
				eventsPerWallet,
				walletsCreated,
				transactionsCreated,
				"Wallet seed completed");
	}

	private Transaction buildTransaction(
			Wallet wallet,
			int walletIndex,
			int eventIndex,
			BigDecimal currentBalance) {
		Transaction transaction = new Transaction();
		transaction.setWallet(wallet);
		transaction.setType(resolveType(eventIndex));
		transaction.setStatus(resolveStatus(eventIndex));
		transaction.setPaymentMethod(resolvePaymentMethod(transaction.getType()));
		transaction.setAmount(resolveAmount(walletIndex, eventIndex, transaction.getType(), currentBalance));
		transaction.setReference("SEED-" + wallet.getCode() + "-" + String.format("%05d", eventIndex));
		return transaction;
	}

	private TransactionType resolveType(int eventIndex) {
		if (eventIndex == 1) {
			return TransactionType.SEED;
		}

		TransactionType[] types = {
				TransactionType.DEPOSIT,
				TransactionType.WITHDRAW,
				TransactionType.TRANSFER,
				TransactionType.BILL_PAYMENT
		};
		return types[(eventIndex - 2) % types.length];
	}

	private TransactionStatus resolveStatus(int eventIndex) {
		if (eventIndex == 1 || eventIndex % 5 != 0) {
			return TransactionStatus.SUCCESS;
		}
		return eventIndex % 10 == 0 ? TransactionStatus.FAILED : TransactionStatus.PENDING;
	}

	private PaymentMethod resolvePaymentMethod(TransactionType type) {
		if (type == TransactionType.TRANSFER) {
			return PaymentMethod.MOBILE_MONEY;
		}
		return PaymentMethod.WALLET;
	}

	private BigDecimal resolveAmount(
			int walletIndex,
			int eventIndex,
			TransactionType type,
			BigDecimal currentBalance) {
		if (type == TransactionType.SEED) {
			return baseSeedAmount(walletIndex);
		}

		BigDecimal amount = BigDecimal.valueOf(500L + ((long) eventIndex * 100L));
		if (type == TransactionType.WITHDRAW || type == TransactionType.TRANSFER || type == TransactionType.BILL_PAYMENT) {
			BigDecimal maxDebit = currentBalance.subtract(BigDecimal.valueOf(1_000L));
			if (maxDebit.compareTo(BigDecimal.valueOf(500L)) < 0) {
				return BigDecimal.valueOf(500L);
			}
			return amount.min(maxDebit);
		}
		return amount;
	}

	private BigDecimal applyTransaction(BigDecimal balance, Transaction transaction) {
		if (transaction.getType() == TransactionType.SEED || transaction.getType() == TransactionType.DEPOSIT) {
			return balance.add(transaction.getAmount());
		}
		return balance.subtract(transaction.getAmount()).max(BigDecimal.ZERO);
	}

	private BigDecimal baseSeedAmount(int walletIndex) {
		return BigDecimal.valueOf(50_000L + ((long) walletIndex * 1_000L));
	}

	private String formatPhoneNumber(int walletIndex) {
		return "+22177" + String.format("%07d", walletIndex);
	}

	private String formatCode(int walletIndex) {
		return "WLT-" + String.format("%07d", walletIndex);
	}

	private String formatEmail(int walletIndex) {
		return "client" + walletIndex + "@gmail.com";
	}
}
