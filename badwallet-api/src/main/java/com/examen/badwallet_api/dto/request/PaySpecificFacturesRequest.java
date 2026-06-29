package com.examen.badwallet_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class PaySpecificFacturesRequest {

	@NotBlank(message = "Le numero de telephone est obligatoire")
	private String phoneNumber;

	@NotBlank(message = "Le service est obligatoire")
	private String serviceName;

	@NotEmpty(message = "La liste des references de factures est obligatoire")
	private List<@NotBlank(message = "La reference de facture est obligatoire") String> factureReferences;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<String> getFactureReferences() {
		return factureReferences;
	}

	public void setFactureReferences(List<String> factureReferences) {
		this.factureReferences = factureReferences;
	}
}
