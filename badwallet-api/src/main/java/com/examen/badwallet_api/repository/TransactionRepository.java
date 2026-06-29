package com.examen.badwallet_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.badwallet_api.entity.Transaction;
import com.examen.badwallet_api.entity.Wallet;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	long countByWallet(Wallet wallet);

	void deleteByWallet(Wallet wallet);
}
