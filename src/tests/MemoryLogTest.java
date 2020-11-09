package tests;

import memorylog.*;
import java.time.LocalDate;
import org.junit.*;
import static org.junit.Assert.*;

public class MemoryLogTest {

	@Test
	public void shouldOpenDefaultConfigAndXML() {
		try {
			MemoryLog m = new MemoryLog();
		} catch (java.io.FileNotFoundException e) {
			fail("FileNotFoundException abnormal case");
		} catch (ConfigLoadException e) {
			fail("ConfigLoadException abnormal case");
		} catch (javax.xml.bind.JAXBException e) {
			fail("JAXBException abnormal case");
		}
	}

	@Test
	public void optimizerShouldPickDefaultDayWhenRangeHasNoItems() {
		try {
			MemoryLog m = new MemoryLog("src/tests/optimizerShouldPickDefaultDayWhenRangeHasNoItems.txt");
			assertEquals(m.refineAddThis(10, false), 10);
		} catch (java.io.FileNotFoundException e) {
			fail("FileNotFoundException abnormal case");
		} catch (ConfigLoadException e) {
			fail("ConfigLoadException abnormal case");
		} catch (javax.xml.bind.JAXBException e) {
			fail("JAXBException abnormal case");
		}
	}

	@Test
	public void optimizerShouldPickDayWithZeroEntries() {
		try {
			MemoryLog m = new MemoryLog("src/tests/optimizerShouldPickDayWithZeroEntries.txt", LocalDate.of(2020,11,1));
			assertEquals(3, m.refineAddThis(4, false));
		} catch (java.io.FileNotFoundException e) {
			fail("FileNotFoundException abnormal case");
		} catch (ConfigLoadException e) {
			fail("ConfigLoadException abnormal case");
		} catch (javax.xml.bind.JAXBException e) {
			fail("JAXBException abnormal case");
		}
	}

	@Test
	public void optimizerShouldPickDayWithLowestEntries() {
		try {
			MemoryLog m = new MemoryLog("src/tests/optimizerShouldPickDayWithLowestEntries.txt", LocalDate.of(2020,11,1));
			assertEquals(5, m.refineAddThis(4, false));
		} catch (java.io.FileNotFoundException e) {
			fail("FileNotFoundException abnormal case");
		} catch (ConfigLoadException e) {
			fail("ConfigLoadException abnormal case");
		} catch (javax.xml.bind.JAXBException e) {
			fail("JAXBException abnormal case");
		}
	}

}
