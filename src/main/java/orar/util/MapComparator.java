package orar.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapComparator {
	public static <K, V> boolean isEqual(Map<K, V> map1, Map<K, V> map2) {

		return (containsAll(map1, map2) && containsAll(map2, map1));

	}

	/**
	 * @param map1
	 * @param map2
	 * @return true if map1 contains all entry of map2; false otherwise
	 */
	public static <K, V> boolean containsAll(Map<K, V> map1, Map<K, V> map2) {
		Iterator<Entry<K, V>> iterator1 = map1.entrySet().iterator();
		boolean yes = true;
		while (iterator1.hasNext()) {
			Entry<K, V> entry = iterator1.next();
			K key1 = entry.getKey();
			V value1 = entry.getValue();

			if (!value1.equals(map2.get(key1))) {
				yes = false;
				break;
			}
		}
		return yes;

	}

}
