package com.priceminister.account.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.priceminister.account.*;

@Service
public class CustomerAccount implements Account, Runnable{
	private Logger LOG = LoggerFactory.getLogger(CustomerAccount.class);
	private AccountBank accountBank;
	private AccountRule accountRule;
	public synchronized void add(Double addedAmount, AccountBank accountBank) {
		if (addedAmount > 0 && accountBank != null){
			accountBank.setCounter(accountBank.getCounter() + 1);
			accountBank.setBalance(accountBank.getBalance()+addedAmount);
		}
	}

	public synchronized Double withdrawAndReportBalance(Double withdrawnAmount,
			AccountRule rule, AccountBank accountBank)
					throws IllegalBalanceException {
		if (accountBank != null && withdrawnAmount > 0 && accountBank.getBalance() >= withdrawnAmount){
			if (rule.withdrawPermitted(accountBank.getBalance() - withdrawnAmount)){
				accountBank.setCounter(accountBank.getCounter() + 1);
				return accountBank.getBalance() - withdrawnAmount;
			}
		}
		throw new IllegalBalanceException(withdrawnAmount);
	}

	public Double getBalance(AccountBank accountBank) {
		return accountBank.getBalance();
	}

	public void run() {
		//Generate a money amount randomly
		Double amount = new Double(0);
		//Randomly, we choose, if we withdraw or we add money to the accountBank. 
		int addOrWithdraw = 0;
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
			if ( accountBank.getCounter() == 25 && accountBank.getBalance() > 0){
				try {
					accountBank.setBalance(withdrawAndReportBalance(getBalance(accountBank), accountRule, accountBank));
					accountBank.setCounter(0);
				} catch (IllegalBalanceException e) {
					LOG.error("Error while withdrawal money, the balance must be greater than 0" + e.toString());
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

	public AccountRule getAccountRule() {
		return accountRule;
	}

	public void setAccountRule(AccountRule accountRule) {
		this.accountRule = accountRule;
	}

}
