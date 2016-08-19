package orar.normalization;

import org.semanticweb.owlapi.model.OWLOntology;

public interface Normalizer {

	/**
	 * @return ontology in normal form for abstraction refinement
	 */
	public OWLOntology getNormalizedOntology();
}
