package jamd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Module {

	protected String id;
	protected String filename;
	protected String _package;
	protected boolean amd;
	protected String content;
	protected Dependencies dependencies;
	protected boolean anonymous = true;

	public Module(String id, String filename, String _package, boolean amd,
			String content, Dependencies dependencies) {
		super();
		this.id = id;
		this.filename = filename;
		this._package = _package;
		this.amd = amd;
		this.content = content;
		this.dependencies = dependencies;
	}

	/**
	 * Gets the full ID of the module
	 * @return
	 */
	public String getID() {
		return id;
	}

	/**
	 * Get the filename of the module
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Get the package this module belongs to
	 * @return
	 */
	public String getPackage() {
		return _package;
	}

	/**
	 * Is it a AMD module with define() and jazz
	 * @return
	 */
	public boolean isAmd() {
		return amd;
	}

	/**
	 * Get the JavaScript module content
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set the JavaScript module content
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get the module dependencies
	 * @return
	 */
	public Dependencies getDependencies() {
		return dependencies;
	}

	/**
	 * If the module is anonymous, for example it has this signature:
	 * define(function(){ ... }) it will insert the ID in the content,
	 * so it will become define('moduleID', function(){ ... });
	 */
	public void replaceAnonymousID() {
		if (amd && anonymous) {
			Pattern pattern = Pattern.compile("define\\((\\[|\\{|function)");
			Matcher matcher = pattern.matcher(content);
			content = matcher.replaceFirst("define('" + id + "', $1");
			anonymous = false;
		}
	}

}
