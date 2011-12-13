package jamd;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Analysis {

	private final String code;
	private final Map<Integer, String> strings;
	private final Map<Integer, String> arrays;

	public Analysis(String code, Map<Integer, String> strings,
			Map<Integer, String> arrays) {
		super();
		this.code = code;
		this.strings = strings;
		this.arrays = arrays;
	}

	public String getCode() {
		return code;
	}

	public Map<Integer, String> getStrings() {
		return strings;
	}

	public Map<Integer, String> getArrays() {
		return arrays;
	}

	protected ArrayList<String> lookupArrayStrings(int start){

		ArrayList<String> array = new ArrayList<String>();
		String rawArray = this.arrays.get(start);
		if (rawArray == null){
			return array;
		}

		int i = 0;
		int len = rawArray.length();
		do {
			String _string = strings.get(i + start + 1);
			if (_string != null) array.add(_string);
			i = rawArray.indexOf(',', i);
			if (i == -1) break;
		} while (++i < len);
		return array;
	}

	public static Analysis analyze(FileInputStream stream) throws IOException {
		int ch;
		String content = "";
		while ((ch = stream.read()) != -1) {
			content += (char) ch;
		}
		return analyze(content);
	}

	public static Analysis analyze(String code) {

		char string = '\0';
		int array = -1;
		char c;
		char rc = '\0';
		int count = 0;
		int stringStart = -1;

		HashMap<Integer, String> strings = new HashMap<Integer, String>();
		HashMap<Integer, String> arrays = new HashMap<Integer, String>();

		String _code = "";

		for (int current = 0, length = code.length(); current < length; current++) {

			c = code.charAt(current);
			char next;
			if ((current + 1) < length)
				next = code.charAt(current + 1);
			else
				next = '\0';

			// strip line comments
			if (string == '\0' && c == '/' && next == '/') {
				current = code.indexOf('\n', current);
				if (current == -1) break;
				continue;
			}

			// strip other comments
			if (string == '\0' && c == '/' && next == '*') {
				current = code.indexOf("*/", current) + 1;
				if (current == -1) break;
				continue;
			}

			// Strip whitespace
			if (string == '\0'
					&& (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f')) {
				continue;
			}

			char last = rc;
			rc = c;

			// Arrays
			if (string == '\0') {
				if (c == '[' && (last == '(' || last == ',' || last == '=')) {
					array = count;
				}
				if (c == ']')
					array = -1;
			}

			if (array != -1 && count > array) {
				String _array = arrays.get(array);
				if (_array == null)
					_array = "";
				arrays.put(array, _array + c);
			}

			// Collect strings
			boolean stringStartEnd = false;
			if ((c == '"' || c == '\'') && string == '\0') {
				string = c;
				stringStart = count;
				stringStartEnd = true;
			}

			if (!stringStartEnd && c == string && last != '\\') {
				string = '\0';
				stringStart = -1;
				stringStartEnd = true;
			}
			if (string != '\0' && !stringStartEnd) {
				String _string = strings.get(stringStart);
				if (_string == null)
					_string = "";
				strings.put(stringStart, _string + c);
			}

			_code += c;
			count++;

		}

		return new Analysis(_code, strings, arrays);
	}

}
