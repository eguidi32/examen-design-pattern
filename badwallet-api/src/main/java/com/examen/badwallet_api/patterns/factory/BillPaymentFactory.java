package com.examen.badwallet_api.patterns.factory;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class BillPaymentFactory {

	private final List<BillPaymentHandler> handlers;

	public BillPaymentFactory(List<BillPaymentHandler> handlers) {
		this.handlers = handlers;
	}

	public BillPaymentHandler getHandler(String serviceName) {
		return handlers.stream()
				.filter(handler -> handler.supports(serviceName))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Service de paiement non supporte: " + serviceName));
	}
}
