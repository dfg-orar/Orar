package orar.owlconverter;

import x.io.ontologyreader.DLLiteHOD_OntologyReader;
import x.io.ontologyreader.OntologyReader;

public class DLLiteHOD_OWLOntologyConverter extends OWLOntologyConverterTemplate {

	@Override
	protected OntologyReader getOntologyReader() {

		return new DLLiteHOD_OntologyReader();
	}

}
