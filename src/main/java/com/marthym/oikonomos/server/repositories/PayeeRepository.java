package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marthym.oikonomos.shared.model.Payee;

public interface PayeeRepository extends JpaRepository<Payee, Long> {
	@Query("select p from Payee p where LOWER(p.name) like LOWER(CONCAT('%',:description,'%'))")
	public List<Payee> findByNameContaining(@Param("description") String description);
	
	@Query("select p from Payee p where LOWER(p.name) like LOWER(CONCAT('%',:description,'%'))")
	public List<Payee> findByNameContaining(@Param("description") String description, Sort sort);
}
