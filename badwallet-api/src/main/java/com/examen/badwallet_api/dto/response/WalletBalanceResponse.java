package com.examen.badwallet_api.dto.response;

import com.examen.badwallet_api.enums.Currency;

import java.math.BigDecimal;

public class WalletBalanceResponse {

	private final String phoneNumber;
	private final String code;
	private final BigDecimal balance;
	private final Currency currency;

	public WalletBalanceResponse(
			String phoneNumber,
			String code,
			BigDecimal balance,
			Currency currency) {
		this.phoneNumber = phoneNumber;
		this.code = code;
		this.balance = balance;
		this.currency = currency;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Currency getCurrency() {
		return currency;
	}
}
