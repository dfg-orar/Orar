package orar.example;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.owlconverter.DLLiteHOD_OWLOntologyConverter;
import orar.owlconverter.OWLOntologyConverter;

public class RunOntologyConverter {
	static String tboxFileName = "/Users/kien/benchmarks/npd-v2/npd.owl";
	static String aboxListFileName = "/Users/kien/benchmarks/npd-v2/aboxList.txt";
	static final Logger logger =Logger.getLogger(MaterializeNPD.class);
	public static void main(String[] args) throws OWLOntologyCreationException {
			OWLOntologyConverter ontologyConverter= new DLLiteHOD_OWLOntologyConverter();
			ontologyConverter.convertToAllInOneOWLFunctionalSynxtax(tboxFileName, aboxListFileName, "npd.dlliteHOD.functionalsyntax.owl");
		

	}
}
