package com.marthym.oikonomos.server.dao;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.marthym.oikonomos.shared.dto.UserDTO;

@Repository
public class UserDAO extends JpaDAO<String, UserDTO> {

	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	public UserDAO() {
		super(UserDTO.class);
	}

}
