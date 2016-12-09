package orar.indexing;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import junit.framework.Assert;

public class IndividualIndexerUsingMapAndArrayTest {

	@Test
	public void test() {
		IndividualIndexer indexer = IndividualIndexer.getInstance();
		String baseLengthOf200 = "http://www.movieontology.org/2009/10/01/movieontology.owl#ind";
		String ind1 = "http://www.movieontology.org/2009/10/01/movieontology.owl#ind1";
		String ind2 = "http://www.movieontology.org/2009/10/01/movieontology.owl#ind2";
		String ind3 = "http://www.movieontology.org/2009/10/01/movieontology.owl#ind3";
		String ind4 = "http://www.movieontology.org/2009/10/01/movieontology.owl#ind4";
		String ind5 = "http://www.movieontology.org/2009/10/01/movieontology.owl#ind5";
		for (int i = 1; i <= 5; i++) {
			String s = baseLengthOf200 + i;
			System.out.print(s + " --> ");
			System.out.println(indexer.getIndexOfIndividualString(s));
		}

		int indexOfInd1 = indexer.getIndexOfIndividualString(ind1);
		Assert.assertEquals(0, indexOfInd1);

		int indexOfInd2 = indexer.getIndexOfIndividualString(ind2);
		Assert.assertEquals(1, indexOfInd2);

		int indexOfInd3 = indexer.getIndexOfIndividualString(ind3);
		Assert.assertEquals(2, indexOfInd3);

		int indexOfInd4 = indexer.getIndexOfIndividualString(ind4);
		Assert.assertEquals(3, indexOfInd4);

		int indexOfInd5 = indexer.getIndexOfIndividualString(ind5);
		Assert.assertEquals(4, indexOfInd5);

		/*
		 * test get all indexes
		 */
		Set<Integer> allInds = indexer.getAllEncodedIndividuals();
		Set<Integer> expectedAllIndexedIndividuals = new HashSet<>();
		expectedAllIndexedIndividuals.add(0);
		expectedAllIndexedIndividuals.add(1);
		expectedAllIndexedIndividuals.add(2);
		expectedAllIndexedIndividuals.add(3);
		expectedAllIndexedIndividuals.add(4);

		Assert.assertEquals(expectedAllIndexedIndividuals, allInds);
		/*
		 * test get string from index
		 * 
		 */
		Assert.assertEquals(ind1, indexer.getIndividualString(0));
		Assert.assertEquals(ind2, indexer.getIndividualString(1));
		Assert.assertEquals(ind3, indexer.getIndividualString(2));
		Assert.assertEquals(ind4, indexer.getIndividualString(3));
		Assert.assertEquals(ind5, indexer.getIndividualString(4));
	}

}
