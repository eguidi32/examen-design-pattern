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
		int updated = 0;

		for (Facture facture : factures) {
			Facture existingFacture = factureRepository.findByReference(facture.getReference()).orElse(null);
			if (existingFacture == null) {
				factureRepository.save(facture);
				created++;
			} else if (updateFacture(existingFacture, facture)) {
				factureRepository.save(existingFacture);
				updated++;
			}
		}

		String message = created == 0 && updated == 0
				? "Aucune facture modifiee, les donnees de seed sont deja a jour"
				: "Factures de seed creees ou mises a jour avec succes";

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
					FactureStatus.UNPAID,
					LocalDate.of(2026, 5, 15),
					null));
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
					LocalDateTime.of(2026, 5, 26, 10, 0)));
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

	private boolean updateFacture(Facture target, Facture source) {
		boolean changed = false;

		if (!target.getWalletCode().equals(source.getWalletCode())) {
			target.setWalletCode(source.getWalletCode());
			changed = true;
		}
		if (target.getServiceName() != source.getServiceName()) {
			target.setServiceName(source.getServiceName());
			changed = true;
		}
		if (target.getAmount().compareTo(source.getAmount()) != 0) {
			target.setAmount(source.getAmount());
			changed = true;
		}
		if (target.getStatus() != source.getStatus()) {
			target.setStatus(source.getStatus());
			changed = true;
		}
		if (!target.getDueDate().equals(source.getDueDate())) {
			target.setDueDate(source.getDueDate());
			changed = true;
		}
		if ((target.getPaidAt() == null && source.getPaidAt() != null)
				|| (target.getPaidAt() != null && !target.getPaidAt().equals(source.getPaidAt()))) {
			target.setPaidAt(source.getPaidAt());
			changed = true;
		}

		return changed;
	}
}
