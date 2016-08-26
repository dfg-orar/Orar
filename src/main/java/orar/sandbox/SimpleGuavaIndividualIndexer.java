package orar.sandbox;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class SimpleGuavaIndividualIndexer {
	private static SimpleGuavaIndividualIndexer instance;

	private BiMap<String, Integer> biMap;

	// private Map<String, Integer> mapIndividualString2Number;
	// private Map<Integer, String> mapNumber2IndividualString;
	private Integer index;
	private OWLDataFactory owlDataFactory;

	private SimpleGuavaIndividualIndexer() {
		this.biMap = HashBiMap.create();
		index = 0;
		this.owlDataFactory = OWLManager.getOWLDataFactory();
	}

	public static SimpleGuavaIndividualIndexer getInstance() {

		if (instance == null) {
			instance = new SimpleGuavaIndividualIndexer();
		}
		return instance;
	}

	/**
	 * @param individualStringID
	 * @return an unique integer number, which is an index for this
	 *         individuslStringID; reused the existing number if this
	 *         individualStringID existed in the indexing map.
	 */
	public Integer getIndexOfIndividualString(String individualStringID) {
		Integer existingIndex = this.biMap.get(individualStringID);
		if (existingIndex == null) {
			index++;
			this.biMap.put(individualStringID, index);
			return index;
		}
		return existingIndex;
	}

	public String getIndividualString(Integer indexOfIndividualStringID) {
		return this.biMap.inverse().get(indexOfIndividualStringID);
	}

	public Integer getSize() {
		return this.index;
	}

}
