package orar.innerreasoner;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import orar.dlreasoner.DLReasoner;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;

/**
 * Interface for reasoners used to materialize abstraction. This interface is
 * different from {@link DLReasoner } interface as we only use some of the
 * entailments of the abstraction to transfer assertions back to the original
 * ABox.
 * 
 * @author kien
 *
 */
public interface InnerReasoner {

	/**
	 * Get concept assertions in form of a map.
	 * 
	 * 
	 * @return <b> Newly </b> entailed concept assertions of individuals from
	 *         combined type,e.g. x,y,and z.<br>
	 *         Note that we don't need to transfer concept assertions from the
	 *         representatives of concept types. <br>
	 *         The result is stored in the form of a map between individuals and
	 *         their concepts.
	 */
	public Map<OWLNamedIndividual, Set<OWLClass>> getEntailedConceptAssertionsAsMap();

	/**
	 * @return entailed role assertions stored in several lists. See
	 *         {@link AbstractRoleAssertionBox}.
	 */
	public AbstractRoleAssertionBox getEntailedRoleAssertions();

	/**
	 * Get SameAs assertions in form of a map.
	 * 
	 * @return Map for SameAs assertions, each individual is mapped to its
	 *         equivalent individuals, <b>EXCLUDING itself</b>. Note that: for a
	 *         equivalent b, we only need to get a--> {b}; Map b-->{a} is
	 *         unnecessary.
	 * 
	 */
	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getSameAsMap();

	/**
	 * compute entailments: concept, role, and equality assertions.
	 */
	public void computeEntailments();

	/**
	 * compute concept assertions.
	 */
	public void computeConceptAssertions();

	/**
	 * @return reasoning time
	 */
	public long getReasoningTime();
	
	public long getOverheadTimeToSetupReasoner();
	
	public boolean isOntologyConsistent();
}
