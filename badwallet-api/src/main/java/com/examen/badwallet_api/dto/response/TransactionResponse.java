package com.examen.badwallet_api.dto.response;

import com.examen.badwallet_api.enums.PaymentMethod;
import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

	private final Long id;
	private final Long walletId;
	private final BigDecimal amount;
	private final BigDecimal fees;
	private final BigDecimal total;
	private final BigDecimal balance;
	private final PaymentMethod paymentMethod;
	private final TransactionType type;
	private final TransactionStatus status;
	private final String reference;
	private final LocalDateTime createdAt;
	private final String message;
	private final String senderPhone;
	private final String receiverPhone;

	public TransactionResponse(
			Long id,
			Long walletId,
			BigDecimal amount,
			BigDecimal balance,
			PaymentMethod paymentMethod,
			TransactionType type,
			TransactionStatus status,
			String reference,
			LocalDateTime createdAt,
			String message) {
		this(id, walletId, amount, null, null, balance, paymentMethod, type, status, reference, createdAt, message);
	}

	public TransactionResponse(
			Long id,
			Long walletId,
			BigDecimal amount,
			BigDecimal fees,
			BigDecimal total,
			BigDecimal balance,
			PaymentMethod paymentMethod,
			TransactionType type,
			TransactionStatus status,
			String reference,
			LocalDateTime createdAt,
			String message) {
		this(id, walletId, amount, fees, total, balance, paymentMethod, type, status, reference, createdAt, message, null, null);
	}

	public TransactionResponse(
			Long id,
			Long walletId,
			BigDecimal amount,
			BigDecimal fees,
			BigDecimal total,
			BigDecimal balance,
			PaymentMethod paymentMethod,
			TransactionType type,
			TransactionStatus status,
			String reference,
			LocalDateTime createdAt,
			String message,
			String senderPhone,
			String receiverPhone) {
		this.id = id;
		this.walletId = walletId;
		this.amount = amount;
		this.fees = fees;
		this.total = total;
		this.balance = balance;
		this.paymentMethod = paymentMethod;
		this.type = type;
		this.status = status;
		this.reference = reference;
		this.createdAt = createdAt;
		this.message = message;
		this.senderPhone = senderPhone;
		this.receiverPhone = receiverPhone;
	}

	public Long getId() {
		return id;
	}

	public Long getWalletId() {
		return walletId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public TransactionType getType() {
		return type;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public String getReference() {
		return reference;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getMessage() {
		return message;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}
}
