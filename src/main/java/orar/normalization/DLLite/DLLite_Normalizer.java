package orar.normalization.DLLite;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.normalization.Normalizer;

/**
 * For DLLite-family, we do not need to normalize the input TBox.
 * 
 * @author kien
 *
 */
public class DLLite_Normalizer implements Normalizer {
	private final OWLOntology inputOntology;

	public DLLite_Normalizer(OWLOntology inputOntology) {
		this.inputOntology = inputOntology;
	}

	@Override
	public OWLOntology getNormalizedOntology() {

		return this.inputOntology;
	}

}
