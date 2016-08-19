package orar.modeling.sameas;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public interface SameAsBox {
	
	/**
	 * @param individual
	 * @return -A (possibly empty) set of individuals that are equal to
	 *         {@code individual}. <br>
	 *         -empty if there is no equal individual other than the
	 *         {@code individual} itself.
	 * 
	 */
	public Set<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual individual);

	/**
	 * Add a sameas assertion.
	 * 
	 * @param individual
	 * @param equalIndividual
	 * @return true if a new element has been added, false otherwise.
	 */
	public boolean addSameAsAssertion(OWLNamedIndividual individual, OWLNamedIndividual equalIndividual);

	/**
	 * add sameas assertions, where {@code individual} has a set of sameas
	 * individual {@code sameIndividuals }
	 * 
	 * @param individual
	 * @param sameIndividuals
	 * @return true if new elements have been added, false otherwise.
	 */
	public boolean addManySameAsAssertions(OWLNamedIndividual individual, Set<OWLNamedIndividual> manyEqualIndividuals);

	public Set<OWLNamedIndividual> getAllIndividuals();

	
	public boolean addSameasAssertions(Set<OWLNamedIndividual> setOfSameasIndividuals);
	/**
	 * @return a copy of sameas map.
	 */
	public Map<OWLNamedIndividual,Set<OWLNamedIndividual>> getSameasMap();
	public Set<OWLAxiom> getEntailedSameasOWLAxioms();
}
