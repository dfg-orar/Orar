package orar.consistencychecking;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

public interface ConsistencyChecker {

	/**
	 * @return true if the consider ontology is consistent, false otherwise
	 */
	public boolean isConsistent();

	public long getReasoningTimeInSeconds();

	/**
	 * @param maxNumberOfExplanations
	 *            maximal number of explanations the reasoner needs to compute.
	 * @return a set of explanations for inconsistency of the ontology; empty
	 *         set if the ontology is consistent.
	 */
	public Set<Set<OWLAxiom>> getExplanations(int maxNumberOfExplanations);

	/**
	 * Release all resource used by the abstraction-refinement-reasoner
	 */
	public void dispose();
}
