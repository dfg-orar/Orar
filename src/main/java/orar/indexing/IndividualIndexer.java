package orar.indexing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * To index individuals (using its StringID) to an integer number. This indexing
 * is 1-to-1
 * 
 * @author kien
 *
 */
public class IndividualIndexer {
	private static IndividualIndexer instance;
	private BiMap<String, Integer> biMapString2Integer;

	private Integer index;
	private OWLDataFactory owlDataFactory;

	private IndividualIndexer() {
		this.biMapString2Integer = HashBiMap.create();

		index = 0;
		this.owlDataFactory = OWLManager.getOWLDataFactory();
	}

	public static IndividualIndexer getInstance() {

		if (instance == null) {
			instance = new IndividualIndexer();
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
		Integer existingIndex = this.biMapString2Integer.get(individualStringID);
		if (existingIndex == null) {
			index++;
			this.biMapString2Integer.put(individualStringID, index);
			return index;
		}
		return existingIndex;
	}

	/**
	 * @param individualStrings
	 * @return their indexes
	 */
	public Set<Integer> getIndexesOfIndividualString(Set<String> individualStrings) {
		Set<Integer> indexes = new HashSet<>();
		for (String eachString : individualStrings) {
			indexes.add(getIndexOfIndividualString(eachString));
		}
		return indexes;

	}

	/**
	 * @param individualStrings
	 * @return their indexes
	 */
	public Set<Integer> getIndexesOfOWLIndividuals(Set<OWLNamedIndividual> individuals) {
		Set<Integer> indexes = new HashSet<>();
		for (OWLNamedIndividual eachIndividual : individuals) {
			indexes.add(getIndexOfIndividualString(eachIndividual.getIRI().toString()));
		}
		return indexes;

	}

	/**
	 * @param individualStrings
	 * @return their indexes
	 */
	public Integer getIndexOfOWLIndividual(OWLNamedIndividual individual) {

		return getIndexOfIndividualString(individual.getIRI().toString());

	}

	public String getIndividualString(Integer indexOfIndividualStringID) {
		return this.biMapString2Integer.inverse().get(indexOfIndividualStringID);
	}

	/**
	 * only for testing
	 * 
	 * @param indexOfIndividualStringID
	 * @return
	 */
	public OWLNamedIndividual getOWLIndividual(Integer indexOfIndividualStringID) {
		String indString = getIndividualString(indexOfIndividualStringID);
		return this.owlDataFactory.getOWLNamedIndividual(IRI.create(indString));
	}

	/**
	 * only for testing
	 * 
	 * @param indexOfIndividuals
	 * @return a set of OWLNamedIndividual corresponding to the given indexes
	 */
	public Set<OWLNamedIndividual> getOWLIndividuals(Set<Integer> indexOfIndividuals) {
		Set<OWLNamedIndividual> owlIndividuals = new HashSet<>();
		for (Integer eachInd : indexOfIndividuals) {
			String eachIndString = getIndividualString(eachInd);
			OWLNamedIndividual owlInd = this.owlDataFactory.getOWLNamedIndividual(IRI.create(eachIndString));
			owlIndividuals.add(owlInd);
		}
		return owlIndividuals;
	}
	
	

	public Integer getSize() {
		return this.index;
	}

	/**
	 * @return a copy of a mapping from Individual(StringID) to an integer. This
	 *         method is used mostly for testing.
	 */
	public Map<String, Integer> viewMapIndividuslString2Integer() {
		Map<String, Integer> currentView = new HashMap<String, Integer>();
		currentView.putAll(this.biMapString2Integer);
		return currentView;
	}

	/**
	 * @return a copy of a mapping from an integer to an Individual(StringID).
	 *         This method is used mostly for testing.
	 */
	public Map<Integer, String> viewMapInteger2IndividuslString() {
		Map<Integer, String> currentView = new HashMap<Integer, String>();
		currentView.putAll(this.biMapString2Integer.inverse());
		return currentView;
	}

	public Set<Integer> getAllEncodedIndividuals() {
		return this.biMapString2Integer.values();
	}

	/**
	 * clear indexing data
	 */
	public void clear() {
		this.biMapString2Integer.clear();
	}
}
