package Junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import methods.Money;
import methods.MoneyBag;

public class MoneyBagTest {
	
	private Money m12chf;
	private Money m14usd;
	private Money m16ils;
	private Money m18eur;
	private MoneyBag mb;
	private MoneyBag mb1;
	
	@Before
	public void setUp() throws Exception {
		m12chf= new Money(12,"CHF");
		m14usd= new Money(14,"USD");
		m16ils = new Money(16,"ILS");
		m18eur = new Money(18,"EUR");
		mb = new MoneyBag(m12chf, m14usd);
		mb1 = new MoneyBag(m16ils, m18eur);
	}

	@Test
	public void testAddMoney() {
		MoneyBag expected1 = new MoneyBag(new Money[] {m12chf,m14usd,m16ils});
		MoneyBag result1 = (MoneyBag) mb.addMoney(m16ils);
		assertEquals(expected1, result1);
		
		MoneyBag expected2 = new MoneyBag(new Money[] {m12chf,m14usd,m16ils,m18eur});
		MoneyBag result2 = (MoneyBag) mb.addMoneyBag(mb1);
		assertEquals(expected2, result2);
		
		MoneyBag result3 = (MoneyBag) mb.addMoney(m16ils);
		assertEquals(expected1,result3);
		
		MoneyBag expected3 = new MoneyBag (new Money[] {m12chf,m14usd,new Money(32, "ILS")});
		MoneyBag result4=(MoneyBag)result3.addMoney(new Money(16, "ILS"));
		assertEquals(expected3,result4);
	}
	
	@Test
	public void testContains() {
		assertTrue(mb.contains(m12chf));
		assertTrue(mb.contains(m14usd));
		assertFalse(mb.contains(m16ils));
	}

}
