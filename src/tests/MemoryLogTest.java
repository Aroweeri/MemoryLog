package tests;

import memorylog.*;
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
			MemoryLog m = new MemoryLog("src/tests/testconfig1.txt");
			assertEquals(m.refineAddThis(10, false), 10);
		} catch (java.io.FileNotFoundException e) {
			fail("FileNotFoundException abnormal case");
		} catch (ConfigLoadException e) {
			fail("ConfigLoadException abnormal case");
		} catch (javax.xml.bind.JAXBException e) {
			fail("JAXBException abnormal case");
		}
	}
}
