package com.marthym.oikonomos.server.dao;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao <K, E extends Serializable> {
	public void create(final E entity);
	public E update(final E entity);
	public void delete(final E entity);
	public void deleteById(final K entityId);
	public E findById(final K id);
	public List<E> findAll();
	public Integer removeAll();
	public void setClazz(Class<E> clazzToSet);
}
