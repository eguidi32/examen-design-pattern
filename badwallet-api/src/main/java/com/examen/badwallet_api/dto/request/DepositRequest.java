package com.examen.badwallet_api.dto.request;

import com.examen.badwallet_api.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class DepositRequest {

	@NotNull(message = "Le montant est obligatoire")
	@Positive(message = "Le montant doit etre superieur a 0")
	private BigDecimal amount;

	@NotNull(message = "La methode de paiement est obligatoire")
	private PaymentMethod paymentMethod;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
