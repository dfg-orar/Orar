package orar.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapOperator {

	/**
	 * add {@code addedValues} to the existing value of {@code key}. Create a
	 * new entry if necessary.
	 * 
	 * @param map
	 * @param key
	 * @param addedValues
	 */
	public static <T1, T2> void addValuesToMap(Map<T1, Set<T2>> map, T1 key,
			Set<T2> addedValues) {
		Set<T2> existingValues = map.get(key);
		if (existingValues == null) {
			existingValues = new HashSet<T2>();
		}
		existingValues.addAll(addedValues);
		map.put(key, existingValues);

	}
}
