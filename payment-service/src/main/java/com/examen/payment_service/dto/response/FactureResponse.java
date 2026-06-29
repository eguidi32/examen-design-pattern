package com.examen.payment_service.dto.response;

import com.examen.payment_service.enums.FactureStatus;
import com.examen.payment_service.enums.ServiceName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FactureResponse {

	private final Long id;
	private final String reference;
	private final String walletCode;
	private final ServiceName serviceName;
	private final BigDecimal amount;
	private final FactureStatus status;
	private final LocalDate dueDate;
	private final LocalDateTime paidAt;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public FactureResponse(
			Long id,
			String reference,
			String walletCode,
			ServiceName serviceName,
			BigDecimal amount,
			FactureStatus status,
			LocalDate dueDate,
			LocalDateTime paidAt,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.reference = reference;
		this.walletCode = walletCode;
		this.serviceName = serviceName;
		this.amount = amount;
		this.status = status;
		this.dueDate = dueDate;
		this.paidAt = paidAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getReference() {
		return reference;
	}

	public String getWalletCode() {
		return walletCode;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public FactureStatus getStatus() {
		return status;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
