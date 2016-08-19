package orar.dlreasoner;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

/**
 * Interface for DL Reasoners like HermiT, Fact, Konclude, Pellet.<br>
 * Note: When using with Konclude via OWLLink, Konclude server must be started
 * first.
 * 
 * @author kien
 */
public interface DLReasoner {

	/**
	 * @return a set of entailed atomic concept assertions, excluding trivial
	 *         concept assertions of Top/OWL:Thing
	 */
	public Set<OWLClassAssertionAxiom> getEntailedConceptAssertions();

	/**
	 * @return a set of entailed atomic role assertions.
	 */
	public Set<OWLObjectPropertyAssertionAxiom> getEntailedRoleAssertions();

	/**
	 * @return entailed equality/sameas assertions stored as a map from
	 *         individual to its equivalent individuals
	 */
	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getEntailedSameasAssertions();

	/**
	 * compute all entailments
	 */
	public void computeEntailments();
	
	/**
	 * compute all entailments
	 */
	public void computeConceptAssertions();

	/**
	 * @return reasoning time in seconds
	 */
	public long getReasoningTime();
	
	public void classifiesOntology();
	public boolean isOntologyConsistent();
}
