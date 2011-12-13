package jamd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

public class ModulesTest {

	@Test
	public void testListIDs() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("simple");

		String[] list = modules.listIDs();
		assertEquals(1, list.length);
		assertArrayEquals("list", new String[] { "simple" }, list);

	}

	@Test
	public void testListFiles() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("one");

		String[] files = modules.listFiles();
		Arrays.sort(files);
		String abs = new File("test-fixtures").getAbsolutePath();
		assertArrayEquals(new String[]{
				abs + "/one.js",
				abs + "/three.js",
				abs + "/two.js"}, files);

	}

	@Test
	public void testDependencies() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("one");
		DependencyMap dependencies = modules.getDependencies();

		assertArrayEquals(new String[] {"two", "three"}, dependencies.get("one").toArray());
		assertEquals(0, dependencies.get("two").size());
		assertEquals(0, dependencies.get("three").size());

	}

	@Test
	public void testOutput() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("simple");

		String expected = "define(\"simple\",\n"
				+ "  function() {\n"
				+ "    return {\n"
				+ "      color: \"blue\"\n"
				+ "    };\n"
				+ "  }\n"
				+ ");\n";

		assertEquals(expected, modules.output());

	}

	@Test
	public void testOutputNoID() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("noid");

		String expected = "\n"
				+ "define('noid', function(){\n"
				+ "\n"
				+ "});\n"
				+ "\n";

		assertEquals(expected, modules.output());

	}

	// @Test
	public void testOutputHas() {
		// TODO
	}

	@Test
	public void testOutputObjectAsFactory() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("objectfactory");

		String expected = "\n"
				+ "define('objectfactory', {\n"
				+ "	a: 1,\n"
				+ "	b: 2\n"
				+ "});\n";

		assertEquals(expected, modules.output());

	}

	@Test
	public void testOutputByPackage() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		jamd.addPath("PackageA", "packageA");
		jamd.addPath("PackageB", "packageB");
		Modules modules = jamd.require("PackageA/a");

		HashMap<String, String> packages = modules.outputByPackage("//----");

		String expectedA = "\n"
				+ "define('PackageA/a', ['./b', 'PackageA/c', 'PackageB/b'], function(b1, b2){\n"
				+ "	return 'a';\n"
				+ "});\n//----\n"
				+ "define('PackageA/c', function(){\n"
				+ "	return 'c';\n"
				+ "});\n//----\n"
				+ "define('PackageA/b', function(){\n"
				+ "	return 'b';\n"
				+ "});\n";

		String expectedB = "\n"
				+ "define('PackageB/b', function(){\n"
				+ "	return 'b';\n"
				+ "});\n";

		assertEquals(expectedA, packages.get("PackageA"));
		assertEquals(expectedB, packages.get("PackageB"));

	}

	// @Test
	public void testSerialization() {
		// TODO
	}

	// @Test
	public void testReduce() {
		// TODO
	}

	// @Test
	public void testReduceExcludes() {
		// TODO
	}

	// @Test
	public void testExclude() {
		// TODO
	}

	// @Test
	public void testExcludeForced() {
		// TODO
	}

}
