package oar.util.MapComparator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import junit.framework.Assert;
import orar.util.MapComparator;

public class MapComparatorTest {

	@Test
	public void test() {
		Map<Integer, Set<Integer>> map1 = new HashMap<>();
		Set<Integer> set1 = new HashSet<>();
		set1.add(1);
		set1.add(2);

		Set<Integer> set2 = new HashSet<>();
		set2.add(1);
		set2.add(2);

		map1.put(1, set1);
		map1.put(2, set2);

		Map<Integer, Set<Integer>> map2 = new HashMap<>();
		map2.put(2, set1);
		map2.put(1, set2);

		Assert.assertTrue(map1.equals(map2));
		Assert.assertTrue(MapComparator.isEqual(map1, map2));
	}

}
