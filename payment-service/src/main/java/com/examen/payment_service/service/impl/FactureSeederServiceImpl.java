package com.examen.payment_service.service.impl;

import com.examen.payment_service.dto.response.SeedFactureResponse;
import com.examen.payment_service.entity.Facture;
import com.examen.payment_service.enums.FactureStatus;
import com.examen.payment_service.enums.ServiceName;
import com.examen.payment_service.repository.FactureRepository;
import com.examen.payment_service.service.FactureSeederService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FactureSeederServiceImpl implements FactureSeederService {

	private static final List<String> WALLET_CODES = List.of(
			"WLT-0000001",
			"WLT-0000002",
			"WLT-0000003");

	private final FactureRepository factureRepository;

	public FactureSeederServiceImpl(FactureRepository factureRepository) {
		this.factureRepository = factureRepository;
	}

	@Override
	@Transactional
	public SeedFactureResponse seedFactures() {
		List<Facture> factures = buildFactures();
		int created = 0;

		for (Facture facture : factures) {
			if (!factureRepository.existsByReference(facture.getReference())) {
				factureRepository.save(facture);
				created++;
			}
		}

		String message = created == 0
				? "Aucune nouvelle facture creee, les donnees de seed existent deja"
				: "Factures de seed creees avec succes";

		return new SeedFactureResponse(WALLET_CODES, created, message);
	}

	private List<Facture> buildFactures() {
		LocalDate now = LocalDate.now();
		List<Facture> factures = new ArrayList<>();

		for (int i = 0; i < WALLET_CODES.size(); i++) {
			String walletCode = WALLET_CODES.get(i);
			int walletNumber = i + 1;

			factures.add(createFacture(
					"FAC-ISM-" + walletNumber + "-1",
					walletCode,
					ServiceName.ISM,
					new BigDecimal("15000.00"),
					FactureStatus.UNPAID,
					now.withDayOfMonth(10),
					null));
			factures.add(createFacture(
					"FAC-ISM-" + walletNumber + "-2",
					walletCode,
					ServiceName.ISM,
					new BigDecimal("18500.00"),
					FactureStatus.PAID,
					now.minusMonths(1).withDayOfMonth(15),
					LocalDateTime.now().minusDays(12)));
			factures.add(createFacture(
					"FAC-ISM-" + walletNumber + "-3",
					walletCode,
					ServiceName.ISM,
					new BigDecimal("21000.00"),
					FactureStatus.UNPAID,
					now.plusDays(20),
					null));
			factures.add(createFacture(
					"FAC-WOYAFAL-" + walletNumber + "-1",
					walletCode,
					ServiceName.WOYAFAL,
					new BigDecimal("7500.00"),
					FactureStatus.UNPAID,
					now.withDayOfMonth(20),
					null));
			factures.add(createFacture(
					"FAC-WOYAFAL-" + walletNumber + "-2",
					walletCode,
					ServiceName.WOYAFAL,
					new BigDecimal("12000.00"),
					FactureStatus.PAID,
					now.minusMonths(1).withDayOfMonth(25),
					LocalDateTime.now().minusDays(8)));
		}

		return factures;
	}

	private Facture createFacture(
			String reference,
			String walletCode,
			ServiceName serviceName,
			BigDecimal amount,
			FactureStatus status,
			LocalDate dueDate,
			LocalDateTime paidAt) {
		Facture facture = new Facture();
		facture.setReference(reference);
		facture.setWalletCode(walletCode);
		facture.setServiceName(serviceName);
		facture.setAmount(amount);
		facture.setStatus(status);
		facture.setDueDate(dueDate);
		facture.setPaidAt(paidAt);
		return facture;
	}
}
