package com.examen.payment_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class FacturesByReferencesRequest {

	@NotEmpty(message = "La liste des references est obligatoire")
	private List<@NotBlank(message = "La reference est obligatoire") String> references;

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}
}
