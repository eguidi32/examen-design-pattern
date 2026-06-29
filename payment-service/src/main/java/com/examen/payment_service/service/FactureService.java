package com.examen.payment_service.service;

import com.examen.payment_service.dto.response.FactureResponse;
import com.examen.payment_service.enums.ServiceName;
import java.time.LocalDate;
import java.util.List;

public interface FactureService {

	List<FactureResponse> findCurrentUnpaidFactures(String walletCode, ServiceName unite);

	List<FactureResponse> findUnpaidFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin);
}
