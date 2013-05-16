package com.marthym.oikonomos.server.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractJpaDao<K, E extends Serializable> {
	private Class<E> clazz;

	@PersistenceContext
	private EntityManager entityManager;

	public void create(final E entity) {
		if (entity == null)
			throw new IllegalStateException();
		entityManager.persist(entity);
	}

	public E update(final E entity) {
		if (entity == null)
			throw new IllegalStateException();
		return (E) entityManager.merge(entity);
	}

	public void delete(final E entity) {
		if (entity == null)
			throw new IllegalStateException();
		entityManager.remove(entity);
	}

	public void deleteById(final K entityId) {
		final E entity = findById(entityId);
		if (entityId == null)
			throw new IllegalStateException();
		delete(entity);
	}

	public E findById(final K id) {
		if (id == null)
			throw new IllegalStateException();
		return entityManager.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		return entityManager.createQuery("from " + clazz.getName())
				.getResultList();
	}

	public Integer removeAll() {
		return (Integer) entityManager.createQuery(
				"DELETE FROM " + clazz.getName() + " h").getSingleResult();
	}

	public void setClazz(Class<E> clazzToSet) {
		this.clazz = clazzToSet;
	}

}
