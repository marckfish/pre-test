package com.priceminister.account;


import static org.fest.assertions.Assertions.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.priceminister.account.implementation.*;


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
	CustomerAccountRule rule;
    @Mock
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
    
    @Test
    public void testBalance(){
    	customerAccount.setAccountBank(accountBank);
    	customerAccount.setAccountRule(rule);
    	customerAccount.start();
    	customerAccount.run();
    	accountBank.getBalance();
    }
    // Also implement missing unit tests for the above functionalities.

}
