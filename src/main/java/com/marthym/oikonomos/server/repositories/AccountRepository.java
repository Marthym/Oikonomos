package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marthym.oikonomos.shared.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	public List<Account> findByAccountOwner(String accountOwner);
	public Long countByAccountOwner(String accountOwner);
}
