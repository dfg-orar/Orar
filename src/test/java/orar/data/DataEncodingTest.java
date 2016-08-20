package orar.data;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import orar.indexing.IndividualIndexer;

public class DataEncodingTest {

	@Test
	public void test() {
		IndividualIndexer indexer = IndividualIndexer.getInstance();
		Assert.assertTrue( indexer.getIndexOfIndividualString("string1")==1);
		
		Assert.assertTrue(2== indexer.getIndexOfIndividualString("string2"));
		Assert.assertTrue(1==indexer.getIndexOfIndividualString("string1"));
		Assert.assertTrue(2== indexer.getIndexOfIndividualString("string2"));

		
		// PrintingHelper.printMap(indexer.viewMapIndividuslString2Long());
		// PrintingHelper.printMap(indexer.viewMapLong2IndividuslString());

		assertEquals("string1", indexer.getIndividualString(1));
		assertEquals("string2", indexer.getIndividualString(2));
		Assert.assertTrue(2== indexer.getSize());
	}

}
