package jamd;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class AnalysisTest {

	Analysis analysis;

	public AnalysisTest() {

		try {

			FileInputStream stream = new FileInputStream("test-fixtures/analyze.js");
			analysis = Analysis.analyze(stream);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testGetCode() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetStrings() {
		Map<Integer, String> strings = analysis.getStrings();

		assertEquals(11, strings.size());
		assertEquals("../Core/Class", strings.get(8));
		assertEquals("../Utility/typeOf", strings.get(24));
		assertEquals("../Host/Array", strings.get(44));
		assertEquals("../Host/String", strings.get(60));
		assertEquals("../Utility/uniqueID", strings.get(77));
		assertEquals("foo", strings.get(152));
		assertEquals("bla", strings.get(159));
		assertEquals("2 3", strings.get(175));
		assertEquals("foo bar", strings.get(191));
		assertEquals("four", strings.get(233));
		assertEquals("[not an array] and {not an object}", strings.get(249));
	}

	@Test
	public void testGetArrays() {
		Map<Integer, String> arrays = analysis.getArrays();

		assertEquals(3, arrays.size());
		assertEquals("'../Core/Class','../Utility/typeOf','../Host/Array','../Host/String','../Utility/uniqueID'", arrays.get(7));
		assertEquals("1,'2 3',4", arrays.get(172));
		assertEquals("'foo bar'", arrays.get(190));

	}

	@Test
	public void testLookupArrayStrings() {
//		fail("Not yet implemented");
	}

}
