package orar.sandbox;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import orar.indexing.IndividualIndexer;

public class GuavaBiMapPerformance {
	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		SimpleGuavaIndividualIndexer indexer = SimpleGuavaIndividualIndexer.getInstance();
		String baseLengthOf200 = "http://www.movieontology.org/2009/10/01/movieontology.owl#South_East_Europeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);

		}
		long end1 = System.currentTimeMillis();
		long lap1 = (end1 - start) / 1000;
		System.out.println("done 1st round in seconds: " + lap1);

		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
		}
		long end2 = System.currentTimeMillis();
		long lap2 = (end2 - start) / 1000;
		System.out.println("done 2st round in seconds: " + lap2);

		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
		}
		long end3 = System.currentTimeMillis();
		long lap3 = (end3 - start) / 1000;
		System.out.println("done 3 round in seconds: " + lap3);

		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
			//
		}

		long end4 = System.currentTimeMillis();
		long lap4 = (end4 - start) / 1000;
		System.out.println("done 4 round in seconds: " + lap4);
		System.out.println("Size " + indexer.getSize());

	}
}
