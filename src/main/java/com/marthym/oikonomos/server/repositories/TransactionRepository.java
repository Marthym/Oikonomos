package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	public List<Transaction> findByAccount(Account account);
	public List<Transaction> findByAccount(Account account, Sort sort);
	public Long countByAccount(Account account);
}
