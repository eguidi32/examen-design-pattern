package com.examen.badwallet_api.dto.response;

import com.examen.badwallet_api.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletResponse {

	private final Long id;
	private final String phoneNumber;
	private final String email;
	private final BigDecimal balance;
	private final String code;
	private final Currency currency;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public WalletResponse(
			Long id,
			String phoneNumber,
			String email,
			BigDecimal balance,
			String code,
			Currency currency,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.balance = balance;
		this.code = code;
		this.currency = currency;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getCode() {
		return code;
	}

	public Currency getCurrency() {
		return currency;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
