package orar.innerconsistencychecking;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Check consistency and compute explanations for inconsistent ontologies
 * 
 * @author kien
 *
 */
public interface InnerConsistencyChecker {

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
}
