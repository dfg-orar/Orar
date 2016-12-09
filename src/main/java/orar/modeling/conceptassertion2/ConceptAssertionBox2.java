package orar.modeling.conceptassertion2;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;

import orar.modeling.roleassertion.RoleAssertionBox;

/**
 * Interface for internal data structure of class assertions. ABox consists of
 * ClassAssertionBox and {@link RoleAssertionBox}
 * 
 * @author kien
 *
 */
public interface ConceptAssertionBox2 {

	/**
	 * @param individual
	 * @return - A (possibly empty) set of concept names occurring in concept
	 *         assertions of the {@code individual}.
	 * 
	 */
	public Set<OWLClass> getAssertedConcepts(Integer individual);

	/**
	 * @param concept
	 * @param individual
	 * @return true if this assertion is NEWLY added, false otherwise.
	 */
	public boolean addConceptAssertion(Integer individual, OWLClass concept);

	/**
	 * @param concept
	 * @param individual
	 * @return true if this assertion is NEWLY added, false otherwise.
	 */
	public boolean addConceptAssertion(OWLClass concept, Integer individual);

	/**
	 * Add many concept assertions.
	 * 
	 * @param concepts
	 * @param individual
	 * @return true if NEW assertions are added, false otherwise.
	 */
	public boolean addManyConceptAssertions(Integer individual, Set<OWLClass> concepts);

	/**
	 * @return OWLAPI class assertions, including one for concepts and
	 *         individuals generated during normalization. This should be used
	 *         for testing only.
	 */
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertions();

	/**
	 * @return OWLAPI class assertions, including one for concepts and
	 *         individuals generated during normalization. This should be used
	 *         for testing only.
	 */
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWithoutNormalizationSymbols();

	/**
	 * @return the up-to-date number of concept assertions occurring in the
	 *         ontology.
	 */
	public int getNumberOfConceptAssertions();

	/**
	 * @return all number of concept assertions without normalization symbols
	 *         and NOT taking SAMEAS into account.
	 */
	public int getNumberOfConceptAssertionsWithoutNormalizationSymbols();

	/**
	 * @return a set (copy) of all individuals occurring in all concept
	 *         assertions.
	 */
	public Set<Integer> getAllIndividuals();

	public void setAssertedConcepts(Integer individual, Set<OWLClass> assertedConcepts);
}
