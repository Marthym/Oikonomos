package com.marthym.oikonomos.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marthym.oikonomos.shared.model.Payee;

public interface PayeeRepository extends JpaRepository<Payee, Long> {
}
