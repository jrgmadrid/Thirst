package punishers.thirst.data.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import punishers.thirst.server.CSVReaderServiceImpl;

public class ParsingTests {
	
	@Before
	public void setUp() {
		CSVReaderServiceImpl.updateData();
	}

	@Test
	public void parsedDataTest() {
		assertEquals(233, CSVReaderServiceImpl.getWaterFountains().size());
	}

}
