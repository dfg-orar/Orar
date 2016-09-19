package orar.owlconverter;

import x.io.ontologyreader.HornSHOIF_OntologyReader;
import x.io.ontologyreader.OntologyReader;

public class HornSHOIF_Orar2TurtleConverter extends Orar2TurtleConverter{

	@Override
	protected OntologyReader getOntologyReader() {
		
		return new HornSHOIF_OntologyReader();
	}

}
