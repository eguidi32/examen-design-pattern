package com.examen.badwallet_api.patterns.factory;

import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import java.util.Optional;

public interface BillPaymentHandler {

	boolean supports(String serviceName);

	Optional<ExternalFactureResponse> findCurrentUnpaidFacture(String walletCode);

	String serviceName();
}
