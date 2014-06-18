package punishers.thirst.data.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import punishers.thirst.server.CSVReader;

public class ParsingTests {
	
	@Before
	public void setUp() {
		CSVReader.updateData();
	}

	@Test
	public void parsedDataTest() {
		assertEquals(233, CSVReader.getWaterFountains().size());
	}

}
