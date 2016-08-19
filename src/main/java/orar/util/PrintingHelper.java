package orar.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.MapDifference;

/**
 * @author T.Kien Tran
 */
public class PrintingHelper {
	static Logger loggerOfHelper = Logger.getLogger(PrintingHelper.class);

	public static <K, V> void printMapToFile(FileWriter fileWriter, Map<K, V> map) throws IOException {

		Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<K, V> entry = iterator.next();
			fileWriter.write("Key:" + entry.getKey() + "\n");
			fileWriter.write("Value:" + entry.getValue() + "\n");
			fileWriter.write("===================" + "\n");

		}

	}

	public static <K, V> void printMap(Map<K, V> map) {

		if (map == null) {
			loggerOfHelper.info("null");
		}
		Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
		loggerOfHelper.info("Element of the map:");
		if (map.size() == 0)
			loggerOfHelper.info("Map is empty");

		while (iterator.hasNext()) {
			Map.Entry<K, V> entry = iterator.next();
			loggerOfHelper.info("Key:" + entry.getKey());
			loggerOfHelper.info("Value:" + entry.getValue());
			loggerOfHelper.info("===================");
		}

	}

	public static <K, V> void printMapDifference(MapDifference<K, V> map) {

		if (map == null) {
			loggerOfHelper.info("null");
		}

		loggerOfHelper.info("Maps are equal? " + map.areEqual());

		if (!map.areEqual()) {
			loggerOfHelper.info("Entires only on the left:");
			printMap(map.entriesOnlyOnLeft());
			loggerOfHelper.info("Entires only on the right:");
			printMap(map.entriesOnlyOnRight());
			
		}
	}

	public static <K, V> void printMap(Logger logger, Map<K, V> map) {

		if (map == null) {
			logger.info("null");
		}
		Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
		logger.info("Element of the map:");
		if (map.size() == 0)
			logger.info("Map is empty");

		while (iterator.hasNext()) {
			Map.Entry<K, V> entry = iterator.next();
			logger.info("Key:" + entry.getKey());
			logger.info("Value:" + entry.getValue());
			logger.info("===================");
		}

	}

	public static <T> void printSet(Set<T> set) {
		if (set == null) {
			loggerOfHelper.info("null");
			return;
		}
		if (!set.isEmpty()) {
			loggerOfHelper.info("Element of the set:");
			for (T element : set) {
				loggerOfHelper.info(element);
			}
		} else {
			loggerOfHelper.info("Empty set");
		}
	}

	public static <T> void printSet(Logger logger, Set<T> set) {
		if (set == null) {
			logger.info("null");
			return;
		}
		if (!set.isEmpty()) {
			logger.info("Element of the set:");
			for (T element : set) {
				logger.info(element);
			}
		}
		if (set.isEmpty()) {
			logger.info("Empty");

		}

	}

	public static void printString(String str) {
		loggerOfHelper.info(str);
	}

	public static void printString(Logger logger, String str) {
		logger.info(str);
	}

	public static void printString(String classThatPrints, String str) {
		System.out.println(classThatPrints + ": " + str);
	}
}
