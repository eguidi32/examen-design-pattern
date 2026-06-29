package com.examen.badwallet_api.patterns.strategy;

import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;
import com.examen.badwallet_api.enums.PaymentMethod;

import java.math.BigDecimal;

public interface DepositStrategy {

	PaymentMethod getPaymentMethod();

	Transaction deposit(Wallet wallet, BigDecimal amount);
}
