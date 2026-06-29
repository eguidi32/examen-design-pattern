package com.examen.badwallet_api.patterns.factory;

import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import com.examen.badwallet_api.patterns.proxy.PaymentServiceProxy;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class WoyafalBillPaymentHandler implements BillPaymentHandler {

	private static final String SERVICE_NAME = "WOYAFAL";

	private final PaymentServiceProxy paymentServiceProxy;

	public WoyafalBillPaymentHandler(PaymentServiceProxy paymentServiceProxy) {
		this.paymentServiceProxy = paymentServiceProxy;
	}

	@Override
	public boolean supports(String serviceName) {
		return SERVICE_NAME.equalsIgnoreCase(serviceName);
	}

	@Override
	public Optional<ExternalFactureResponse> findCurrentUnpaidFacture(String walletCode) {
		return paymentServiceProxy.getCurrentFactures(walletCode, SERVICE_NAME).stream()
				.filter(facture -> SERVICE_NAME.equalsIgnoreCase(facture.getServiceName()))
				.filter(facture -> "UNPAID".equalsIgnoreCase(facture.getStatus()))
				.findFirst();
	}

	@Override
	public String serviceName() {
		return SERVICE_NAME;
	}
}
