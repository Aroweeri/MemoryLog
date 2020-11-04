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
}
