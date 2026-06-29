package com.examen.payment_service.service.impl;

import com.examen.payment_service.dto.response.FactureResponse;
import com.examen.payment_service.entity.Facture;
import com.examen.payment_service.enums.FactureStatus;
import com.examen.payment_service.enums.ServiceName;
import com.examen.payment_service.repository.FactureRepository;
import com.examen.payment_service.service.FactureService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FactureServiceImpl implements FactureService {

	private final FactureRepository factureRepository;

	public FactureServiceImpl(FactureRepository factureRepository) {
		this.factureRepository = factureRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<FactureResponse> findCurrentUnpaidFactures(String walletCode, ServiceName unite) {
		YearMonth currentMonth = YearMonth.now();
		LocalDate start = currentMonth.atDay(1);
		LocalDate end = currentMonth.atEndOfMonth();

		if (unite != null) {
			return factureRepository
					.findByWalletCodeAndStatusAndServiceNameAndDueDateBetween(
							walletCode,
							FactureStatus.UNPAID,
							unite,
							start,
							end)
					.stream()
					.map(this::toResponse)
					.toList();
		}

		return factureRepository
				.findByWalletCodeAndStatusAndDueDateBetween(walletCode, FactureStatus.UNPAID, start, end)
				.stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<FactureResponse> findUnpaidFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
		return factureRepository
				.findByWalletCodeAndStatusAndDueDateBetween(walletCode, FactureStatus.UNPAID, debut, fin)
				.stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	@Transactional
	public FactureResponse payFacture(String reference) {
		Facture facture = factureRepository.findByReference(reference)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Aucune facture trouvee avec cette reference"));

		if (FactureStatus.PAID == facture.getStatus()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La facture est deja payee");
		}

		facture.setStatus(FactureStatus.PAID);
		facture.setPaidAt(LocalDateTime.now());

		return toResponse(factureRepository.saveAndFlush(facture));
	}

	private FactureResponse toResponse(Facture facture) {
		return new FactureResponse(
				facture.getId(),
				facture.getReference(),
				facture.getWalletCode(),
				facture.getServiceName(),
				facture.getAmount(),
				facture.getStatus(),
				facture.getDueDate(),
				facture.getPaidAt(),
				facture.getCreatedAt(),
				facture.getUpdatedAt());
	}
}
