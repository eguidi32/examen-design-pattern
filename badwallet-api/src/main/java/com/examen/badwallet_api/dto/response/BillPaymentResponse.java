package com.examen.badwallet_api.dto.response;

import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;
import java.math.BigDecimal;

public class BillPaymentResponse {

	private final String phoneNumber;
	private final String walletCode;
	private final String serviceName;
	private final BigDecimal amount;
	private final String factureReference;
	private final BigDecimal balance;
	private final TransactionType type;
	private final TransactionStatus status;
	private final String message;

	public BillPaymentResponse(
			String phoneNumber,
			String walletCode,
			String serviceName,
			BigDecimal amount,
			String factureReference,
			BigDecimal balance,
			TransactionType type,
			TransactionStatus status,
			String message) {
		this.phoneNumber = phoneNumber;
		this.walletCode = walletCode;
		this.serviceName = serviceName;
		this.amount = amount;
		this.factureReference = factureReference;
		this.balance = balance;
		this.type = type;
		this.status = status;
		this.message = message;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getWalletCode() {
		return walletCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getFactureReference() {
		return factureReference;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public TransactionType getType() {
		return type;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
