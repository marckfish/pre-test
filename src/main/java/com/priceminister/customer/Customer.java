package com.priceminister.customer;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.priceminister.account.implementation.AccountBank;

@Entity
public class Customer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private Long id;
	@Column
	private String fistName;
	@Column
	private String lastName;

	@OneToMany(mappedBy="customer")
	private ArrayList<AccountBank> accountBanks;

	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFistName() {
		return fistName;
	}

	public void setFistName(String fistName) {
		this.fistName = fistName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<AccountBank> getAccountBanks() {
		return accountBanks;
	}

	public void setAccountBanks(ArrayList<AccountBank> accountBanks) {
		this.accountBanks = accountBanks;
	}
	
}
