package com.examen.badwallet_api.service.impl;

import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import com.examen.badwallet_api.patterns.proxy.PaymentServiceProxy;
import com.examen.badwallet_api.service.ExternalFactureService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExternalFactureServiceImpl implements ExternalFactureService {

	private final PaymentServiceProxy paymentServiceProxy;

	public ExternalFactureServiceImpl(PaymentServiceProxy paymentServiceProxy) {
		this.paymentServiceProxy = paymentServiceProxy;
	}

	@Override
	public List<ExternalFactureResponse> getCurrentFactures(String walletCode, String unite) {
		return paymentServiceProxy.getCurrentFactures(walletCode, unite);
	}

	@Override
	public List<ExternalFactureResponse> getFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
		return paymentServiceProxy.getFacturesByPeriod(walletCode, debut, fin);
	}
}
