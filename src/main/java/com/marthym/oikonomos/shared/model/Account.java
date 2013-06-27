package com.marthym.oikonomos.shared.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ACCOUNT")
public class Account implements java.io.Serializable {
	private static final long serialVersionUID = -390708280442917715L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull @Column(name = "owner", unique = true, nullable = false, length = 255)
	private String accountOwner;
	
	@NotNull @Column(name = "name", nullable = false, length = 255)
	private String accountName;
	
	@Column(name = "type", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@Column(name = "currency", nullable = false, length = 3)
	private String accountCurrency;
	
	@Column(name = "closed", nullable = false, 
			columnDefinition="boolean default false")
	private boolean isClosed;
	
	@Column(name = "bank_name", length = 80)
	private String bankName;
	
	@Column(name = "bank_code", length = 5)
	private int bankCode;
	
	@Column(name = "bank_desk", length = 5)
	private int bankDesk;
	
	@Column(name = "account_number", length = 11)
	private long accountNumber;
	
	@Column(name = "account_key", length = 3)
	private int accountKey;
	
	@Column(name = "initial_amount", nullable = false)
	private double initialAmount;
	
	@Column(name = "minimal_amount")
	private double minimalAmount;
	@Column(name = "maximal_amount")
	private double maximalAmount;
	@Column(name = "current_amount", nullable = false)
	private double currentAmount;
	@Column(name = "pointed_amount", nullable = false)
	private double pointedAmount;
}
