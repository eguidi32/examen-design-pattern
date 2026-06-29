package com.examen.badwallet_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferRequest {

	@NotBlank(message = "Le numero de telephone expediteur est obligatoire")
	private String senderPhone;

	@NotBlank(message = "Le numero de telephone destinataire est obligatoire")
	private String receiverPhone;

	@NotNull(message = "Le montant est obligatoire")
	@Positive(message = "Le montant doit etre superieur a 0")
	private BigDecimal amount;

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
