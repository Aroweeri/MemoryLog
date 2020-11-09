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

	@Test
	public void removeOneNormal() {
		OurDate date = new OurDate(10,10,2020);
		date.subtractOne();
		assertEquals(date.getDay(), 9);
		assertEquals(date.getMonth(), 10);
		assertEquals(date.getYear(), 2020);
	}

	@Test
	public void removeOneDayIs1() {
		OurDate date = new OurDate(1,10,2020);
		date.subtractOne();
		assertEquals(date.getDay(), 30);
		assertEquals(date.getMonth(), 9);
		assertEquals(date.getYear(), 2020);
	}

	@Test
	public void removeOneDayIs1MonthIs1() {
		OurDate date = new OurDate(1,1,2020);
		date.subtractOne();
		assertEquals(date.getDay(), 31);
		assertEquals(date.getMonth(), 12);
		assertEquals(date.getYear(), 2019);
	}

	@Test
	public void addOneNormal() {
		OurDate date = new OurDate(1,1,2020);
		date.addOne();
		assertEquals(date.getDay(), 2);
		assertEquals(date.getMonth(), 1);
		assertEquals(date.getYear(), 2020);
	}

	@Test
	public void addOneLastDay() {
		OurDate date = new OurDate(31,1,2020);
		date.addOne();
		assertEquals(date.getDay(), 1);
		assertEquals(date.getMonth(), 2);
		assertEquals(date.getYear(), 2020);
	}

	@Test
	public void addOneLastDayLastMonth() {
		OurDate date = new OurDate(31,12,2020);
		date.addOne();
		assertEquals(date.getDay(), 1);
		assertEquals(date.getMonth(), 1);
		assertEquals(date.getYear(), 2021);
	}


}
