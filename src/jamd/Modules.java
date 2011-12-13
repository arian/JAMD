package jamd;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

public class Modules extends HashMap<String, Module> {

	private static final long serialVersionUID = 1L;

	public void add(Module module){
		put(module.getID(), module);
	}

	public boolean contains(String module){
		return containsKey(module);
	}

	public boolean contains(Module module) {
		return containsValue(module);
	}

	/**
	 * List all the IDs of the selected modules
	 * @return
	 */
	public String[] listIDs() {
		String[] list = new String[size()];
		int i = 0;
		for (Module module : values())
			list[i++] = module.getID();
		return list;
	}

	/**
	 * Return all the files of the selected modules
	 * @return
	 */
	public String[] listFiles() {
		String[] list = new String[size()];
		int i = 0;
		for (Module module : values())
			list[i++] = module.getFilename();
		return list;
	}

	/**
	 * Get the dependencies of each module
	 * @return
	 */
	public DependencyMap getDependencies() {
		DependencyMap list = new DependencyMap();
		for (Module module : values())
			list.put(module.getID(), module.getDependencies());
		return list;
	}

	/**
	 * Group the modules into packages
	 * @return
	 */
	public HashMap<String, Modules> getPackages() {
		HashMap<String, Modules> packages = new HashMap<String, Modules>();
		for (Module module : values()) {
			String pack = module.getPackage();
			if (!packages.containsKey(pack)) {
				packages.put(pack, new Modules());
			}
			packages.get(pack).add(module);
		}
		return packages;
	}

	/**
	 * Generates the concatenated module content and gives every define() an ID
	 * @param glue The glue which joins the code of the different modules together
	 * @return
	 */
	public String output(String glue) {
		return output(this, glue);
	}

	/**
	 * Generates the concatenated module content and gives every define() an ID
	 * @return
	 */
	public String output() {
		return output("\n\n");
	}

	/**
	 * Concatenates the files by Package
	 * @param glue The glue which joins the code of the different modules together
	 * @return
	 */
	public HashMap<String, String> outputByPackage(String glue) {
		HashMap<String, Modules> packages = getPackages();
		HashMap<String, String> codes = new HashMap<String, String>();
		for (Entry<String, Modules> pack : packages.entrySet()) {
			codes.put(pack.getKey(), pack.getValue().output(glue));
		}
		return codes;
	}

	/**
	 * Concatenates the files by Package
	 * @return
	 */
	public HashMap<String, String> outputByPackage() {
		return outputByPackage("\n\n");
	}

	protected static String output(Modules modules, String glue) {
		String[] code = new String[modules.size()];
		int i = 0;
		for (Module module : modules.values()) {
			String content = module.getContent();
			if (content.isEmpty() && !module.getFilename().isEmpty()) {
				content = JAMD.readFile(new File(module.getFilename()));
				module.setContent(content);
			}
			module.replaceAnonymousID();
			code[i++] = module.getContent();
		}
		return joinString(code, glue);
	}

	protected static String joinString(String[] strings, String delimiter) {
		if (strings.length == 0) return "";
		String result = strings[0];
		for (int i = 1; i < strings.length; i++) {
			result += delimiter + strings[i];
		}
		return result;
	}

}
