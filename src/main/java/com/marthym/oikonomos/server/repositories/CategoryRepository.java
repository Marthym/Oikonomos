package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marthym.oikonomos.shared.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	public List<Category> findByParentId(Long parentId);
}
