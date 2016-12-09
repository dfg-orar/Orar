package orar.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
	public static <T1, T2> boolean addValuesToMap(Map<T1, Set<T2>> map, T1 key, Set<T2> addedValues) {
		Set<T2> existingValues = map.get(key);
		if (existingValues == null) {
			existingValues = new HashSet<T2>();
			map.put(key, existingValues);
		}
		boolean hasNewElements = existingValues.addAll(addedValues);

		return hasNewElements;
	}

	public static <T1, T2> boolean addValueToMap(Map<T1, Set<T2>> map, T1 key, T2 addedValue) {
		Set<T2> existingValues = map.get(key);
		if (existingValues == null) {
			existingValues = new HashSet<T2>();
			map.put(key, existingValues);
		}
		boolean hasNewElement = existingValues.add(addedValue);

		return hasNewElement;
	}

	/**
	 * Add a map
	 * 
	 * @param map
	 * @param addedMap
	 *            will be added to {@code map}
	 */
	public static <T1, T2> boolean addAnotherMap(Map<T1, Set<T2>> map, Map<T1, Set<T2>> addedMap) {
		Iterator<Entry<T1, Set<T2>>> iterator = addedMap.entrySet().iterator();
		boolean hasNewElements = false;
		while (iterator.hasNext()) {
			Entry<T1, Set<T2>> entry = iterator.next();
			T1 key = entry.getKey();
			Set<T2> values = entry.getValue();
			if (addValuesToMap(map, key, values)) {
				hasNewElements = true;
			}
		}
		return hasNewElements;
	}

}
