package com.examen.badwallet_api.patterns.proxy;

import com.examen.badwallet_api.dto.response.ExternalFactureResponse;
import java.time.LocalDate;
import java.util.List;

public interface PaymentServiceProxy {

	List<ExternalFactureResponse> getCurrentFactures(String walletCode, String unite);

	List<ExternalFactureResponse> getFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin);
}
