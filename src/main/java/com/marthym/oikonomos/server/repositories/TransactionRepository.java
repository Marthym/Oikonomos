package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marthym.oikonomos.shared.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	public List<Transaction> findByOwner(String accountOwner);
	public Long countByOwner(String accountOwner);
}