package orar.data;

import static org.junit.Assert.*;

import org.junit.Test;

import orar.indexing.IndividualIndexer;
import orar.util.PrintingHelper;

public class DataEncodingTest {

	@Test
	public void test() {
		IndividualIndexer indexer = IndividualIndexer.getInstance();
		assertEquals(1, indexer.getIndexOfIndividualString("string1"));
		assertEquals(2, indexer.getIndexOfIndividualString("string2"));
		assertEquals(1, indexer.getIndexOfIndividualString("string1"));
		assertEquals(2, indexer.getIndexOfIndividualString("string2"));

		
		PrintingHelper.printMap(indexer.viewMapIndividuslString2Long());
		PrintingHelper.printMap(indexer.viewMapLong2IndividuslString());

		assertEquals("string1", indexer.getIndividualString(1));
		assertEquals("string2", indexer.getIndividualString(2));
		assertEquals(2, indexer.getSize());
	}

}
