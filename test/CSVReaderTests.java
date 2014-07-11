import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import punishers.thirst.server.CSVReaderServiceImpl;
import punishers.thirst.server.WaterFountain;


public class CSVReaderTests {
	Set<WaterFountain> wfs;
	

	@Before
	public void setUp() throws Exception {
		CSVReaderServiceImpl csvReader = new CSVReaderServiceImpl();
		csvReader.updateData();
		wfs = CSVReaderServiceImpl.getFountains();
	}

	@Test
	public void test() {
		assertEquals(233, wfs.size());
	}

}
