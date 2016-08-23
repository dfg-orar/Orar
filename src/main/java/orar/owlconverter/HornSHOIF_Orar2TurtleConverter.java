package orar.owlconverter;

import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class HornSHOIF_Orar2TurtleConverter extends Orar2TurtleConverter{

	@Override
	protected OntologyReader getOntologyReader() {
		
		return new HornSHOIF_OntologyReader();
	}

}
