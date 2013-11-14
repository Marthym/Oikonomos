package com.marthym.oikonomos.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.server.repositories.PayeeRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.services.PayeeService;

@Repository
@Service("payeeService")
public class PayeeServiceImpl extends RemoteServiceServlet implements PayeeService {
	private static final long serialVersionUID = 1L;

	@Autowired
	private PayeeRepository payeeRepository;

	@Override
	@Secured("ROLE_USER")
	public long getCount() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		return payeeRepository.count();
	}

	@Override
	@Secured("ROLE_USER")
	public Payee find(long id) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		Payee payee = payeeRepository.findOne(Long.valueOf(id));
		if (payee != null)
			return payee;
		else 
			throw new OikonomosException(
					"error.message.entity.notfound", 
					"Payee "+id+" not found");
	}

	@Override
	@Secured("ROLE_USER")
	public Payee addOrUpdateEntity(Payee payee) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		return payeeRepository.save(payee);
	}

	@Override
	@Secured("ROLE_USER")
	public void delete(long payeeId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		//TODO: Check if payee is used in transactions ?
		payeeRepository.delete(Long.valueOf(payeeId));
	}

	@Override
	@Secured("ROLE_USER")
	public List<Payee> findAll() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		return payeeRepository.findAll(sortByName());
	}

	@Override
	@Secured("ROLE_USER")
	public List<Payee> getEntitiesByDescription(String search) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");

		return payeeRepository.findByNameContaining(search, sortByName());
	}

	private static final Sort sortByName() {
		return new Sort(Direction.ASC, "name");
	}
}
