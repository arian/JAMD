package jamd;

import java.util.ArrayList;
import java.util.HashMap;

public class Modules extends HashMap<String, Module> {

	private static final long serialVersionUID = 1L;

	public void add(Module module){
		put(module.getID(), module);
	}

	public boolean contains(String module){
		return containsKey(module);
	}

	public String[] list() {
		String[] list = new String[size()];
		int i = 0;
		for (Module module : values())
			list[i++] = module.getID();
		return list;
	}

	public HashMap<String, ArrayList<String>> getDependencies() {
		HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
		for (Module module : values())
			list.put(module.getID(), module.getDependencies());
		return list;
	}

}
