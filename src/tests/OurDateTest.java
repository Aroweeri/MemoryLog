package tests;

import common.OurDate;
import org.junit.*;
import static org.junit.Assert.*;

public class OurDateTest {

	@Test
	public void shouldSetDay() {
		OurDate date = new OurDate();
		date.setDay(10);
		assertEquals(date.getDay(), 10);
	}

	@Test
	public void calcDays1() {
		OurDate date = new OurDate();
		assertEquals(date.calcDays(), 730001);
	}

	@Test
	public void calcDaysLowYearBecomesDefault() {
		OurDate date = new OurDate(1,1,1);
		assertEquals(date.calcDays(), 730001);
	}
}
