package com.marthym.oikonomos.server.dao;

import java.io.Serializable;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GenericJpaDao<K, E extends Serializable> 
extends AbstractJpaDao<K, E> implements IGenericDao<K, E> {

}
