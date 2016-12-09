package orar.io.ontologyreader;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.OWLOntologyValidator;
import orar.dlfragmentvalidator.DLLiteHOD.DLLiteHOD_OWLOntology_Validator;
import orar.normalization.Normalizer;
import orar.normalization.DLLite.DLLite_Normalizer;

public class DLLiteHOD_OntologyReader extends OntologyReaderTemplate {

	@Override
	protected OWLOntologyValidator getOntologyValidator(OWLOntology owlOntology) {

		return new DLLiteHOD_OWLOntology_Validator(owlOntology);
	}

	@Override
	protected Normalizer getNormalizer(OWLOntology owlOntology) {
		/*
		 * As transitivity is already "normalized", we use ALCHOIF normalizer
		 */
		return new DLLite_Normalizer(owlOntology);
	}

}
