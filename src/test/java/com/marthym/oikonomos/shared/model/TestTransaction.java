package com.marthym.oikonomos.shared.model;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.marthym.oikonomos.server.repositories.AccountRepository;
import com.marthym.oikonomos.server.repositories.TransactionRepository;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestTransaction {
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------ TestTransaction: start -------");
		
	}

	@Test
	@Transactional
	public void testCreate() {		
		long initCount = transactionRepository.count();
		
		Transaction transac = new Transaction(accountRepository.findOne(1L));
		transac.setAccountingDocument("accounting doc");
		transac.setComment("Test comment");
		transac.setCredit(1000L);
		transac.setDate(new Date());
		transac.setPaiementMean(PaiementMeans.CREDIT_CARD);
		transac.setPayee(new Payee("Me"));
		
		transac = transactionRepository.save(transac);
		Assert.assertNotNull(transac);
		
		long count = transactionRepository.count();
		Assert.assertEquals(initCount+1, count);
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("------------ TestTransaction: end -------");
	}

}
