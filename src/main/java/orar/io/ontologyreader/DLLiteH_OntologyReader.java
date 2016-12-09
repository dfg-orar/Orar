package orar.io.ontologyreader;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.OWLOntologyValidator;
import orar.dlfragmentvalidator.DLLiteH.DLLiteH_OWLOntology_Validator;
import orar.normalization.Normalizer;
import orar.normalization.DLLite.DLLite_Normalizer;

public class DLLiteH_OntologyReader extends OntologyReaderTemplate {

	@Override
	protected OWLOntologyValidator getOntologyValidator(OWLOntology owlOntology) {

		return new DLLiteH_OWLOntology_Validator(owlOntology);
	}

	@Override
	protected Normalizer getNormalizer(OWLOntology owlOntology) {
		/*
		 * As transitivity is already "normalized", we use ALCHOIF normalizer
		 */
		return new DLLite_Normalizer(owlOntology);
	}

}
