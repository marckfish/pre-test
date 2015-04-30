package com.priceminister.account;


import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.priceminister.account.implementation.AccountBank;
import com.priceminister.account.implementation.CustomerAccount;
import com.priceminister.account.implementation.CustomerAccountRule;


/**
 * Please create the business code, starting from the unit tests below.
 * Implement the first test, the develop the code that makes it pass.
 * Then focus on the second test, and so on.
 * 
 * We want to see how you "think code", and how you organize and structure a simple application.
 * 
 * When you are done, please zip the whole project (incl. source-code) and send it to recrutement-dev@priceminister.com
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerAccountTest {
	@InjectMocks
	private CustomerAccount customerAccount;
	@InjectMocks
	private CustomerAccount customerAccount2;
	@InjectMocks
	CustomerAccountRule rule;
	private AccountBank accountBank;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		accountBank = new AccountBank();
	}

	/**
	 * Tests that an empty account always has a balance of 0.0, not a NULL.
	 */
	@Test
	public void testAccountWithoutMoneyHasZeroBalance() {
		accountBank = new AccountBank();
		assertThat(accountBank.getBalance()).isEqualTo(0);
		assertThat(accountBank.getBalance()).isNotEqualTo(null);
	}

	/**
	 * Adds money to the account and checks that the new balance is as expected.
	 */
	@Test
	public void testAddPositiveAmount() {
		accountBank.setBalance(1200);
		customerAccount.add(new Double(2000), accountBank);
		//Balance expected is 3200
		assertThat(customerAccount.getBalance(accountBank)).isEqualTo(3200);
	}

	/**
	 * Tests that an illegal withdrawal throws the expected exception.
	 * Use the logic contained in CustomerAccountRule; feel free to refactor the existing code.
	 * @throws IllegalBalanceException 
	 */
	@Test(expected = IllegalBalanceException.class)
	public void testWithdrawAndReportBalanceIllegalBalance_Ko() throws IllegalBalanceException {
		accountBank.setBalance(1200);
		Double result = customerAccount.withdrawAndReportBalance(new Double(2000), rule, accountBank);
		//Balance expected is -800
		assertThat(result).isEqualTo(-800);
	}

	@Test
	public void testWithdrawAndReportBalance() throws IllegalBalanceException{
		accountBank.setBalance(1200);
		accountBank.setBalance(customerAccount.withdrawAndReportBalance(new Double(800), rule, accountBank));
		//Balance expected is 400
		assertThat(customerAccount.getBalance(accountBank)).isEqualTo(400);
	}

	/**
	 * Cette méthode simule le retrait et l'ajout d'argent sur un compte bancaire
	 * jusqu'à ce que la balance soit égale à 0 
	 */
	@Test
	public void testZeroBalanceAccount(){
		customerAccount.setAccountBank(accountBank);
		customerAccount.setAccountRule(rule);
		Thread thread = new Thread(customerAccount);
		thread.start();
		assertThat(customerAccount.getBalance(accountBank)).isEqualTo(0);
	}

	/**
	 * Cette méthode permet le ratrait et l'ajout d'argent par 2 instance distinctes sur le même compte banacaire 
	 *
	 */
	@Test
	public void testZeroBalanceAccountConcurrentAccess(){
		customerAccount.setAccountBank(accountBank);
		customerAccount.setAccountRule(rule);
		customerAccount2.setAccountBank(accountBank);
		customerAccount2.setAccountRule(rule);
		Thread thread_1 = new Thread(customerAccount);
		Thread thread_2 = new Thread(customerAccount2);
		thread_1.start();
		thread_2.start();
		assertThat(customerAccount.getBalance(accountBank)).isEqualTo(0);
	}
}
