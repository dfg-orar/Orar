package orar.io.ontologyreader;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.OWLOntologyValidator;
import orar.dlfragmentvalidator.HornSHOIF.HornSHOIF_OWLOntology_Validator;
import orar.normalization.Normalizer;
import orar.normalization.ALCHOIF.ALCHOIF_Normalizer;

public class HornSHOIF_OntologyReader extends OntologyReaderTemplate {

	@Override
	protected OWLOntologyValidator getOntologyValidator(OWLOntology owlOntology) {

		return new HornSHOIF_OWLOntology_Validator(owlOntology);
	}

	@Override
	protected Normalizer getNormalizer(OWLOntology owlOntology) {
		/*
		 * As transitivity is "normalized" separately, we use ALCHOIF normalizer
		 */
		return new ALCHOIF_Normalizer(owlOntology);
	}

}
