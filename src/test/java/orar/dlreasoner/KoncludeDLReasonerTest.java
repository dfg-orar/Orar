package orar.dlreasoner;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.config.Configuration;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class KoncludeDLReasonerTest {
	@Test
	public void testRoleAssertionByTran1() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionByTran1.owl";
		runKonclude(ontologyPath);
	}

	@Test
	public void testRoleAssertionByTran2() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionByTran2.owl";
		runKonclude(ontologyPath);
	}
	@Test
	 public void testUOBM_OXSmall() {
	 String ontologyPath = "src/test/resources/uobm-ox/u1/univ0-small1.owl";
	 runKonclude(ontologyPath);
	 }
//	 @Test
//	 public void testUOBM_OX() {
//	 String ontologyPath = "src/test/resources/uobm-ox/u1/univ0.owl";
//	 runKonclude(ontologyPath);
//	 }
	@Test
	public void testLUBM() {

		String ontologyPath = "src/test/resources/lubm/full-lubm.owl";
		runKonclude(ontologyPath);
	}

	
	private void runKonclude(String ontologyPath) {
		
		OntologyReader ontoReader= new HornSHOIF_OntologyReader();
		OWLOntology owlOntology=ontoReader.getOWLAPIOntology(ontologyPath);
		DLReasoner dlReasoner= new KoncludeDLReasoner(owlOntology);
		dlReasoner.computeEntailments();
		
	}

}
