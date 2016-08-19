package orar.owlconverter;

import orar.io.ontologyreader.DLLiteHOD_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class DLLiteHOD_OWLOntologyConverter extends OWLOntologyConverterTemplate {

	@Override
	protected OntologyReader getOntologyReader() {

		return new DLLiteHOD_OntologyReader();
	}

}
