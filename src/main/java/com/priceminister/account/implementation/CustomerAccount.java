package com.priceminister.account.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.priceminister.account.*;

@Service
public class CustomerAccount extends Thread implements Account{
	@SuppressWarnings("unused")
	private Logger LOG = LoggerFactory.getLogger(CustomerAccount.class);
	private AccountBank accountBank;
	private Double amount;
	private AccountRule accountRule;
	public synchronized void add(Double addedAmount, AccountBank accountBank) {
		if (addedAmount > 0 && accountBank != null){
			accountBank.setBalance(accountBank.getBalance()+addedAmount);
		}
	}

	public synchronized Double withdrawAndReportBalance(Double withdrawnAmount,
			AccountRule rule, AccountBank accountBank)
			throws IllegalBalanceException {
		if (accountBank != null && withdrawnAmount > 0 && accountBank.getBalance() >= withdrawnAmount){
			if (rule.withdrawPermitted(accountBank.getBalance() - withdrawnAmount)){
				return accountBank.getBalance() - withdrawnAmount;
			}
		}
		throw new IllegalBalanceException(withdrawnAmount);
	}

	public Double getBalance(AccountBank accountBank) {
		return accountBank.getBalance();
	}

	public void run() {
		//générer un montant de façon aléatoire
		Double amount = new Double(0);
		//de manière alétoire on choisit si l'on ajoute de l'argent ou l'on retire 
		int addOrWithdraw = 0;
		int index = 0;
		do{
			amount = (double) Math.round(Math.random()*1000);
			addOrWithdraw = (int) (Math.random()*2);
			if (addOrWithdraw == 0){
				add(amount, accountBank);
			}else{
				try {
					accountBank.setBalance(withdrawAndReportBalance(amount, accountRule, accountBank));
				} catch (IllegalBalanceException e) {
					add(amount, accountBank);
				}
			}
			index++;
			if (index == 10 && getBalance(accountBank) > 0){
				try {
					accountBank.setBalance(withdrawAndReportBalance(getBalance(accountBank), accountRule, accountBank));
				} catch (IllegalBalanceException e) {
					LOG.error("Error while withdrawing, the balance must be greaten than 0" + e.toString());
				}
			}
		}while(getBalance(accountBank) > 0);
	}

	public AccountBank getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(AccountBank accountBank) {
		this.accountBank = accountBank;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public AccountRule getAccountRule() {
		return accountRule;
	}

	public void setAccountRule(AccountRule accountRule) {
		this.accountRule = accountRule;
	}
	
	

}
