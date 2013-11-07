package com.marthym.oikonomos.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marthym.oikonomos.shared.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	public Long countByOwnerIsNullOrOwner(String owner);
	
	@Query("select c from Category c join c.translations t where key(t) = :locale and (c.owner is null or c.owner = :owner) and LOWER(t.description) like LOWER(CONCAT('%',:description,'%'))")
	public List<Category> findByDescription(@Param("description") String description, @Param("owner") String owner, @Param("locale") String locale);
	
	@Query("select count(c) from Category c where (c.owner is null or c.owner = :owner) and parent_id is null")
	public Long countRootCategoriesByOwner(@Param("owner") String owner);
	
	@Query("select c from Category c where (c.owner is null or c.owner = :owner) and parent_id is null")
	public List<Category> findRootCategoriesByOwner(@Param("owner") String owner);

	@Query("select c from Category c where (c.owner is null or c.owner = :owner) and parent_id = :parentId")
	public List<Category> findByParentId(@Param("owner") String owner, @Param("parentId") Long parentId);
}
