package orar.modeling.sameas2;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;


public interface SameAsBox2 {
	
	/**
	 * @param individual
	 * @return -A (possibly empty) set of individuals that are equal to
	 *         {@code individual}. <br>
	 *         -empty if there is no equal individual other than the
	 *         {@code individual} itself.
	 * 
	 */
	public Set<Integer> getSameIndividuals(Integer individual);

	/**
	 * Add a sameas assertion.
	 * 
	 * @param individual
	 * @param equalIndividual
	 * @return true if a new element has been added, false otherwise.
	 */
	public boolean addSameAsAssertion(Integer individual, Integer equalIndividual);

	/**
	 * add sameas assertions, where {@code individual} has a set of sameas
	 * individual {@code sameIndividuals }
	 * 
	 * @param individual
	 * @param sameIndividuals
	 * @return true if new elements have been added, false otherwise.
	 */
	public boolean addManySameAsAssertions(Integer individual, Set<Integer> manyEqualIndividuals);
	public boolean addNewManySameAsAssertions(Set<Integer> equalIndividuals);
	public Set<Integer> getAllIndividuals();

	
	public boolean addSameasAssertions(Set<Integer> setOfSameasIndividuals);
	/**
	 * @return a copy of sameas map.
	 */
	public Map<Integer,Set<Integer>> getSameasMap();
	public Set<OWLAxiom> getOWLAPISameasAssertions();
	public Integer getNumberOfEntailedSameasAssertions();
}
