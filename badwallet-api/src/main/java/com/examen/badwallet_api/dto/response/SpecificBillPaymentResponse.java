package com.examen.badwallet_api.dto.response;

import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;
import java.math.BigDecimal;
import java.util.List;

public class SpecificBillPaymentResponse {

	private final String phoneNumber;
	private final String walletCode;
	private final String serviceName;
	private final List<String> factureReferences;
	private final BigDecimal totalAmount;
	private final BigDecimal balance;
	private final TransactionType type;
	private final TransactionStatus status;
	private final String message;

	public SpecificBillPaymentResponse(
			String phoneNumber,
			String walletCode,
			String serviceName,
			List<String> factureReferences,
			BigDecimal totalAmount,
			BigDecimal balance,
			TransactionType type,
			TransactionStatus status,
			String message) {
		this.phoneNumber = phoneNumber;
		this.walletCode = walletCode;
		this.serviceName = serviceName;
		this.factureReferences = factureReferences;
		this.totalAmount = totalAmount;
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

	public List<String> getFactureReferences() {
		return factureReferences;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
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
