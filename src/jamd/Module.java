package jamd;

import java.util.ArrayList;

public class Module {

	protected String id;
	protected String filename;
	protected String _package;
	protected boolean amd;
	protected String content;
	protected ArrayList<String> dependencies;



	public Module(String id, String filename, String _package, boolean amd,
			String content, ArrayList<String> dependencies) {
		super();
		this.id = id;
		this.filename = filename;
		this._package = _package;
		this.amd = amd;
		this.content = content;
		this.dependencies = dependencies;
	}

	public String getID() {
		return id;
	}

	public String getFilename() {
		return filename;
	}

	public String getPackage() {
		return _package;
	}

	public boolean isAmd() {
		return amd;
	}

	public String getContent() {
		return content;
	}

	public ArrayList<String> getDependencies() {
		return dependencies;
	}



}
