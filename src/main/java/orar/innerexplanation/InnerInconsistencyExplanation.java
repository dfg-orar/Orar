package orar.innerexplanation;

import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 * InconsistencyExplanation using Hermit
 * 
 * @author kien
 *
 */
public interface InnerInconsistencyExplanation {
	/**
	 * @param maxNumberOfExplanations
	 *            maximal number of explanations the reasoner needs to compute.
	 * @return a set of explanations for inconsistency of the ontology; empty
	 *         set if the ontology is consistent.
	 */
	public Set<Set<OWLAxiom>> getExplanations(OWLOntology owlOntology, int maxNumberOfExplanations, long timeout);
}


