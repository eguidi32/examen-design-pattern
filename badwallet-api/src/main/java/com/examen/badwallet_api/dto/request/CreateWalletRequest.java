package com.examen.badwallet_api.dto.request;

import com.examen.badwallet_api.enums.Currency;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class CreateWalletRequest {

	@NotBlank(message = "Le numero de telephone est obligatoire")
	private String phoneNumber;

	@NotBlank(message = "L'email est obligatoire")
	@Email(message = "L'email doit etre valide")
	private String email;

	@NotNull(message = "Le solde initial est obligatoire")
	@PositiveOrZero(message = "Le solde initial doit etre positif ou egal a zero")
	private BigDecimal initialBalance;

	@NotBlank(message = "Le code est obligatoire")
	private String code;

	@NotNull(message = "La devise est obligatoire")
	private Currency currency;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
