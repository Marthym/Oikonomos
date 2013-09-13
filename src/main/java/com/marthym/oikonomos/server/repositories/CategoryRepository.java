package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marthym.oikonomos.shared.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	public Long countByOwnerIsNullOrOwner(String owner);
	
	@Query("select c from Category c where (c.owner is null or c.owner = :owner) and parent_id is null")
	public List<Category> findRootCategoriesByOwner(@Param("owner") String owner);

	@Query("select c from Category c where (c.owner is null or c.owner = :owner) and parent_id = :parentId")
	public List<Category> findByParentId(@Param("owner") String owner, @Param("parentId") Long parentId);
}
