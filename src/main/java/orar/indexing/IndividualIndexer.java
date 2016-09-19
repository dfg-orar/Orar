package orar.indexing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * To index individuals (using its StringID) to an integer number. This indexing
 * is 1-to-1
 * 
 * @author kien
 *
 */
public class IndividualIndexer {
	private static IndividualIndexer instance;
	private Map<String, Integer> mapIndividualString2Number;
	private Map<Integer, String> mapNumber2IndividualString;
	private Integer index;
	private OWLDataFactory owlDataFactory;

	private IndividualIndexer() {
		this.mapIndividualString2Number = new HashMap<String, Integer>();
		this.mapNumber2IndividualString = new HashMap<Integer, String>();
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
		Integer existingIndex = this.mapIndividualString2Number.get(individualStringID);
		if (existingIndex == null) {
			index++;
			this.mapIndividualString2Number.put(individualStringID, index);
			this.mapNumber2IndividualString.put(index, individualStringID);
			return index;
		}
		return existingIndex;
	}

	/**
	 * @param individualStrings
	 * @return their indexes
	 */
	public Set<Integer> getIndexesOfIndividualString(Set<String> individualStrings) {
		Set<Integer> indexes = new HashSet<Integer>();
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
		Set<Integer> indexes = new HashSet<Integer>();
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
		return this.mapNumber2IndividualString.get(indexOfIndividualStringID);
	}

	/**
	 * only for testing
	 * 
	 * @param indexOfIndividualStringID
	 * @return
	 */
	public OWLNamedIndividual getOWLIndividual(Integer indexOfIndividualStringID) {
		String indString = this.mapNumber2IndividualString.get(indexOfIndividualStringID);
		return this.owlDataFactory.getOWLNamedIndividual(IRI.create(indString));
	}

	/**
	 * only for testing
	 * @param indexOfIndividuals
	 * @return a set of OWLNamedIndividual corresponding to the given indexes
	 */
	public Set<OWLNamedIndividual> getOWLIndividuals(Set<Integer> indexOfIndividuals) {
		Set<OWLNamedIndividual> owlIndividuals = new HashSet<OWLNamedIndividual>();
		for (Integer eachInd : indexOfIndividuals) {
			String eachIndString = this.mapNumber2IndividualString.get(eachInd);
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
		currentView.putAll(this.mapIndividualString2Number);
		return currentView;
	}

	/**
	 * @return a copy of a mapping from an integer to an Individual(StringID).
	 *         This method is used mostly for testing.
	 */
	public Map<Integer, String> viewMapInteger2IndividuslString() {
		Map<Integer, String> currentView = new HashMap<Integer, String>();
		currentView.putAll(this.mapNumber2IndividualString);
		return currentView;
	}

	public Set<Integer> getAllEncodedIndividuals() {
		return this.mapNumber2IndividualString.keySet();
	}
	/**
	 * clear indexing data
	 */
	public void clear(){
		this.mapIndividualString2Number.clear();
		this.mapNumber2IndividualString.clear();
	}
}
