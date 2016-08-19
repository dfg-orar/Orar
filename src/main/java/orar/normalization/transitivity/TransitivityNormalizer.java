package orar.normalization.transitivity;

import org.semanticweb.owlapi.model.OWLOntology;

public interface TransitivityNormalizer {
	/**
	 * add corresponding axioms wrt transitivity to the input ontology
	 */
	public void normalizeTransitivity();

	/**
	 * @return the resulting ontology, e.g. the input ontology with added
	 *         axioms.
	 * 
	 */
	public OWLOntology getResultingOntology();
}
