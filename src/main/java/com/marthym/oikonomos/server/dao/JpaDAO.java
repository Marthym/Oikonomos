package com.marthym.oikonomos.server.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public abstract class JpaDAO<K, E extends Serializable> {
	protected Class<E> clazz;

	@PersistenceContext
    private EntityManager entityManager;
	
	public JpaDAO(final Class<E> clazzToSet) {
        this.clazz = clazzToSet;
    } 

	public void create(final E entity) {
		if (entity == null) throw new IllegalStateException();
        getEntityManager().persist(entity);
    }
 
    public E update(final E entity) {
    	if (entity == null) throw new IllegalStateException();
        return (E) getEntityManager().merge(entity);
    }
 
    public void delete(final E entity) {
    	if (entity == null) throw new IllegalStateException();
        getEntityManager().remove(entity);
    }
 
    public void deleteById(final K entityId) {
        final E entity = findById(entityId);
        if (entityId == null) throw new IllegalStateException();
        delete(entity);
    }

	public E findById(final K id) {
		if (id == null) throw new IllegalStateException();
        return getEntityManager().find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		return getEntityManager().createQuery("from " + clazz.getName()).getResultList();
	}

	public Integer removeAll() {
		return (Integer)getEntityManager().createQuery("DELETE FROM " + clazz.getName() + " h").getSingleResult();
	}
	
    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }
 
    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
