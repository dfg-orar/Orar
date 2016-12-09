package orar.sandbox;

import orar.indexing.IndividualIndexer;
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
		long start= System.currentTimeMillis();
		long totalMem=Runtime.getRuntime().totalMemory()/1000000;
		long maxMem=Runtime.getRuntime().maxMemory()/1000000;
		
		System.out.println("Total mem: "+totalMem);
		System.out.println("Maximal mem: "+maxMem);
		
//		IndividualIndexerMapAndArray indexer = IndividualIndexerMapAndArray.getInstance();
		IndividualIndexer indexer = IndividualIndexer.getInstance();
		String baseLengthOf200 = "http://www.movieontology.org/2009/10/01/movieontology.owl#South_East_Europeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
			
		}
		long end1=System.currentTimeMillis();
		long lap1 = (end1-start)/1000;
		System.out.println("done 1st round in seconds: "+ lap1);
		printMem();
		
		
		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
//			
		}
		long end2=System.currentTimeMillis();
		long lap2 = (end2-start)/1000;
		System.out.println("done 2st round in seconds: "+ lap2);
		printMem();
		
		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
//			
		}
		long end3=System.currentTimeMillis();
		long lap3 = (end3-start)/1000;
		System.out.println("done 3st round in seconds: "+ lap3);
		printMem();
		
		for (int i = 1; i < 6502978; i++) {
			String s = baseLengthOf200 + i;
			indexer.getIndexOfIndividualString(s);
//			
		}
		
		long end4=System.currentTimeMillis();
		long lap4 = (end4-start)/1000;
		System.out.println("done 4th round in seconds: "+ lap4);
		printMem();
		System.out.println("Size "+indexer.getSize());
	}
	
	private static void printMem(){
		long totalMem=Runtime.getRuntime().totalMemory()/1024000;
		System.out.println("Total mem: "+totalMem);
		long freeMem=Runtime.getRuntime().freeMemory()/1024000;
		System.out.println("Free mem: "+freeMem);
		long usedMem=totalMem-freeMem;
		System.out.println("Used mem: "+ usedMem);
//		System.out.println("GC");
//		System.gc();
	}
}
