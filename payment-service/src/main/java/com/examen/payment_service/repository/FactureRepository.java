package com.examen.payment_service.repository;

import com.examen.payment_service.entity.Facture;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Long> {

	boolean existsByReference(String reference);

	long countByWalletCodeIn(Collection<String> walletCodes);
}
