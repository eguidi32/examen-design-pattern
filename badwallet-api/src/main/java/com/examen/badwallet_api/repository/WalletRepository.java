package com.examen.badwallet_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.badwallet_api.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Optional<Wallet> findByCode(String code);

	Optional<Wallet> findByPhoneNumber(String phoneNumber);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByEmail(String email);

	boolean existsByCode(String code);
}
