package com.examen.badwallet_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	long countByWallet(Wallet wallet);

	List<Transaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);

	void deleteByWallet(Wallet wallet);
}
