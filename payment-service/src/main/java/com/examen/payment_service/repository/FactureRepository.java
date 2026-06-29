package com.examen.payment_service.repository;

import com.examen.payment_service.entity.Facture;
import com.examen.payment_service.enums.FactureStatus;
import com.examen.payment_service.enums.ServiceName;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Long> {

	boolean existsByReference(String reference);

	Optional<Facture> findByReference(String reference);

	long countByWalletCodeIn(Collection<String> walletCodes);

	List<Facture> findByWalletCodeAndStatusAndDueDateBetween(
			String walletCode,
			FactureStatus status,
			LocalDate start,
			LocalDate end);

	List<Facture> findByWalletCodeAndStatusAndServiceNameAndDueDateBetween(
			String walletCode,
			FactureStatus status,
			ServiceName serviceName,
			LocalDate start,
			LocalDate end);
}
