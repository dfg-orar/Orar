package orar.dlfragmentvalidator;

import static org.junit.Assert.*;

import org.junit.Test;

import orar.modeling.ontology2.OrarOntology2;
import x.io.ontologyreader.HornSHOIF_OntologyReader;
import x.io.ontologyreader.OntologyReader;
import x.util.PrintingHelper;

public class TestOntologyProfile {

	@Test
	public void shouldReturnDLLiteExtension() {
		
		String tboxFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/dllite1.owl";
		String aboxListFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/aboxListFileName";
		OntologyReader reader= new HornSHOIF_OntologyReader();
		OrarOntology2 orarOntology= reader.getNormalizedOrarOntology(tboxFileName, aboxListFileName);
		PrintingHelper.printSet(orarOntology.getActualDLConstructors());
	}
	
}
