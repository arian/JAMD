package jamd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

public class JAMDTest {

	@Test
	public void testSimple() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("simple");

		String[] list = modules.listIDs();

		assertEquals("number of modules", 1, list.length);
		assertArrayEquals("list", new String[] { "simple" }, list);

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
	public void testCustomID() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("idtest");

		String[] list = modules.listIDs();

		assertArrayEquals(new String[] {"customid"}, list);

	}

	@Test
	public void testDotInID() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("with.dots.in.filename");

		String[] list = modules.listIDs();

		assertArrayEquals(new String[] {"with.dots.in.filename"}, list);

	}

	@Test
	public void testAliasFile() {
		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		jamd.addPath("Moo", "MooTools/Core.js");

		Modules modules = jamd.require("Moo");
		Module module = modules.get("Moo");
		assertNotNull(module);
		assertEquals(new File("test-fixtures/MooTools/Core.js").getAbsolutePath(), module.getFilename());
		assertEquals("Moo", module.getID());
		assertEquals("", module.getPackage());
	}

	@Test
	public void testAliasPath() {
		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		jamd.addPath("Moo", "MooTools");

		Modules modules = jamd.require("Moo/Core");
		Module module = modules.get("Moo/Core");
		assertNotNull(module);
		assertEquals(new File("test-fixtures/MooTools/Core.js").getAbsolutePath(), module.getFilename());
		assertEquals("Moo/Core", module.getID());
		assertEquals("Moo", module.getPackage());
	}

	@Test
	public void testDOMNode() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		jamd.addPath("Core", "MooTools");

		Modules modules = jamd.require("Core/DOM/Node");

		assertArrayEquals(new String[] {
				"Core/Core/Class",
				"Core/Utility/typeOf",
				"Core/Host/Array",
				"Core/Host/String",
				"Core/Utility/uniqueID"
			}, modules.get("Core/DOM/Node").getDependencies().toArray());

		assertArrayEquals(new String[] {
				"Core/Utility/typeOf",
				"Core/Utility/merge"
			}, modules.get("Core/Core/Class").getDependencies().toArray());

		assertArrayEquals(new String[] {},
				modules.get("Core/Utility/typeOf").getDependencies().toArray());

		assertArrayEquals(new String[] {},
				modules.get("Core/Utility/merge").getDependencies().toArray());

		assertArrayEquals(new String[] {
				"Core/Core/Host"
			}, modules.get("Core/Host/Array").getDependencies().toArray());

		assertArrayEquals(new String[] {},
				modules.get("Core/Core/Host").getDependencies().toArray());

		assertArrayEquals(new String[] {
				"Core/Core/Host"
			}, modules.get("Core/Host/String").getDependencies().toArray());

		assertArrayEquals(new String[] {},
				modules.get("Core/Utility/uniqueID").getDependencies().toArray());

	}

	@Test
	public void testCrossPackageDependencies() {
		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		jamd.addPath("PackageA", "packageA");
		jamd.addPath("PackageB", "packageB");
		Modules modules = jamd.require("PackageA/a");

		assertArrayEquals(new String[] {
				"PackageA/b",
				"PackageA/c",
				"PackageB/b"
			}, modules.get("PackageA/a").getDependencies().toArray());

		assertEquals(0, modules.get("PackageA/b").getDependencies().size());
		assertEquals(0, modules.get("PackageA/c").getDependencies().size());
		assertEquals(0, modules.get("PackageB/b").getDependencies().size());

	}

	@Test
	public void testCircular() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		jamd.require("circular/a");

	}

	@Test
	public void testRequireInBody() {

		JAMD jamd = new JAMD();
		jamd.setBaseURL("test-fixtures");
		Modules modules = jamd.require("requireInBody");

		DependencyMap dependencies = modules.getDependencies();
		assertArrayEquals(new String[] {
				"one",
				"two",
				"three",
				"simple"}, dependencies.get("requireInBody").toArray());

	}

	// @Test
	public void testOptions() {

//		$packager = new Packager;
//		$packager->setBaseUrl($this->fixtures);
//		$packager->setOptions(Path::resolve($this->fixtures, 'options.js'), array(
//			'foo' => 'bar'
//		));
//		$builder = $packager->req(array('options'));
//
//		$modules = $builder->loaded();
//		$options = $modules['options']['options'];
//
//		$this->assertEquals(array(
//			'foo' => 'bar',
//			'modules' => 'true',
//			'amd' => true
//		), $options);

	}

	// @Test
	public void testCommonJSModulesNoAMD() {

//		$packager = new Packager;
//		$packager->setBaseUrl($this->fixtures);
//		$builder = $packager->req(array('modules/no-amd'));
//
//		$this->assertEquals(array(
//			'modules/no-amd',
//			'modules/a',
//			'modules/module'
//		), $builder->modules());

	}


}
