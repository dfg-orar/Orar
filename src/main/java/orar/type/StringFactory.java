package orar.type;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class StringFactory {

	private final Map<String, WeakReference<String>> cache;
	private static StringFactory instance;

	private StringFactory() {
		this.cache = new WeakHashMap<String, WeakReference<String>>();

	}

	public static StringFactory getInstance() {
		if (instance == null) {
			instance = new StringFactory();
		}
		return instance;

	}

	public String getString(String inputString) {

		WeakReference<String> valueOfNewType = this.cache.get(inputString);
		if (valueOfNewType == null) {
			this.cache.put(inputString, new WeakReference<String>(inputString));
			return inputString;
		}

		return valueOfNewType.get();

	}

	public void clear() {
		this.cache.clear();
	}

}
