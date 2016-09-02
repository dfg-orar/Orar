package orar.owlconverter;

import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class HornSHOIF_OWLOntologyConverter extends OWLOntologyConverterTemplate {

	@Override
	protected OntologyReader getOntologyReader() {

		return new HornSHOIF_OntologyReader();
	}
}