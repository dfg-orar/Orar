package orar.owlconverter;

import x.io.ontologyreader.HornSHOIF_OntologyReader;
import x.io.ontologyreader.OntologyReader;

public class HornSHOIF_OWLOntologyConverter extends OWLOntologyConverterTemplate {

	@Override
	protected OntologyReader getOntologyReader() {

		return new HornSHOIF_OntologyReader();
	}
}