package com.examen.badwallet_api.patterns.strategy;

import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;
import com.examen.badwallet_api.enums.PaymentMethod;
import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class WalletTargetDepositStrategy implements DepositStrategy {

	@Override
	public PaymentMethod getPaymentMethod() {
		return PaymentMethod.WALLET_TARGET;
	}

	@Override
	public Transaction deposit(Wallet wallet, BigDecimal amount) {
		wallet.setBalance(wallet.getBalance().add(amount));

		Transaction transaction = new Transaction();
		transaction.setWallet(wallet);
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setStatus(TransactionStatus.SUCCESS);
		transaction.setPaymentMethod(getPaymentMethod());
		transaction.setAmount(amount);
		transaction.setReference("DEP-WT-" + UUID.randomUUID());
		return transaction;
	}
}
