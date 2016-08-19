package orar.sandbox;

import orar.indexing.IndividualIndexer;

public class IndexingPerformance {
	public static void main(String[] args) {
//		String s1 = "a";
//		IndividualIndexer indexer = IndividualIndexer.getInstance();
//		indexer.encodeIndividualString(s1);
//		s1 = null;
//		System.out.println(indexer.viewMapIndividuslString2Long());
		test1();

	}

	public static void test1() {
		IndividualIndexer indexer = IndividualIndexer.getInstance();
		String baseLengthOf200 = "http://www.movieontology.org/2009/10/01/movieontology.owl#South_East_Europeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
			
		}

		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
//			
		}

		System.out.println("done");
		System.out.println(indexer.getSize());
	}
}
