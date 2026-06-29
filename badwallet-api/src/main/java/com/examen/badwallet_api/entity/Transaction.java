package com.examen.badwallet_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.examen.badwallet_api.enums.PaymentMethod;
import com.examen.badwallet_api.enums.TransactionStatus;
import com.examen.badwallet_api.enums.TransactionType;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wallet_id", nullable = false)
	private Wallet wallet;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private TransactionType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private TransactionStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private PaymentMethod paymentMethod;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 120)
	private String reference;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	void prePersist() {
		createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
