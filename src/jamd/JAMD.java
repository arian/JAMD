package jamd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JAMD {

	protected HashMap<String, String> paths;
	protected String baseURL;

	protected String[] skip = { "exports", "require", "module" };

	public JAMD() {
		paths = new HashMap<String, String>();
	}

	public JAMD(String _baseURL) {
		this();
		baseURL = _baseURL;
	}

	public void setBaseURL(String url) {
		baseURL = url;
	}

	public void addPath(String alias, String path) {
		paths.put(alias, path);
	}

	public Modules require(String module) {
		return require(new String[] { module });
	}

	public Modules require(String[] modules) {
		Modules _modules = new Modules();
		for (String module : modules) {
			_require(module, _modules);
		}
		return _modules;
	}

	protected void _require(String id, Modules modules) {
		if (Arrays.asList(skip).contains(id) || id.contains("!"))
			return;

		String filename = id;
		String extension = "";
		boolean amd = false;
		String pack = "";

		int pos = filename.lastIndexOf('.');
		if (pos != -1) {
			extension = filename.substring(pos);
		}

		if (!Arrays.asList(new String[] { ".js", ".css" /* more? */}).contains(
				extension)) {
			amd = true;
		}

		if (amd) {
			filename += ".js";
		}

		for (Entry<String, String> path : paths.entrySet()) {
			String _alias = path.getKey();
			String _url = path.getValue();
			if (id == _alias) {
				filename = _url;
			} else {
				int len = _alias.length();
				if (filename.substring(0, len).equals(_alias)) {
					filename = _url + filename.substring(len);
					pack = _alias;
					break;
				}
			}
		}

		File _file = new File(baseURL + "/" + filename);
		filename = _file.getAbsolutePath();

		if (!_file.exists()) {
			System.out.println("the file " + filename + " does not exist");
			return;
		}

		// TODO it'd be better if the analysis method would accept the buffer
		// or a FileInputStream
		String content = JAMD.readFile(_file);

		Dependencies deps = new Dependencies();
		String _id = "";
		amd = amd && content.contains("define");

		if (amd) {
			Analysis info = Analysis.analyze(content);
			String code = info.getCode();
			Map<Integer,String> arrays = info.getArrays();
			Map<Integer,String> strings = info.getStrings();

			// define(id?, dependencies?, factory)
			int defStart = code.indexOf("define(") + 7;
			if (strings.containsKey(defStart)) {
				_id = strings.get(defStart);
				defStart += _id.length() + 3; // ",[
			}

			ArrayList<String> _deps;
			if (arrays.containsKey(defStart)) {
				_deps = info.lookupArrayStrings(defStart);
				for (String dep : _deps)
					deps.add(dep);
			}

			// require(module) / require(modules)
			int len = code.length();
			int i = defStart;

			do {
				i = code.indexOf("require(", i);
				if (i == -1)
					break;
				else
					i += 8;
				if (strings.containsKey(i)) {
					deps.add(strings.get(i));
				} else {
					_deps = info.lookupArrayStrings(i);
					for (String dep : _deps)
						deps.add(dep);
				}

			} while (i < len);

			if (!_id.isEmpty())
				id = _id;

			for (int j = 0; j < deps.size(); j++) {
				String dep = deps.get(j);
				int index = dep.indexOf("/");
				if (
						index != -1
						&& !dep.substring(0, index).equals(pack)
						&& ".".equals(dep.substring(0, 1))) {
					deps.set(j, URI.create(id + "/../" + dep).normalize().toString());
				}
			}

			Module module = new Module(id, filename, pack, amd, content, deps);
			modules.add(module);

			for (String dep : deps) {
				_require(dep, modules);
			}

		}

	}

	protected static String readFile(File filename) {
		String content = "";
		try {
			FileInputStream stream = new FileInputStream(filename);
			int ch;
			while ((ch = stream.read()) != -1) {
				content += (char) ch;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
