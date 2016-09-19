package orar.debugger;

import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import x.io.ontologyreader.DLLiteHOD_OntologyReader;
import x.io.ontologyreader.OntologyReader;
import x.util.PrintingHelper;

public class OrarDebuggerDLLiteTest {

	private void testWith(String ontologyPath) {
		OntologyReader ontologyReader = new DLLiteHOD_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(ontologyPath);
		OrarDebugger debugger = new OrarDebugger_Hermit(normalizedOrarOntology);

		Set<Set<OWLAxiom>> explanations = debugger.getExplanationsForInconsistency(5, 1000);
		int count = 0;
		for (Set<OWLAxiom> eachExplanation : explanations) {
			System.out.println("Explanation number #" + ++count);

			PrintingHelper.printSet(eachExplanation);
		}

	}

	private void testWith(String tbox, String aboxList) {
		OntologyReader ontologyReader = new DLLiteHOD_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tbox, aboxList);
		OrarDebugger debugger = new OrarDebugger_Hermit(normalizedOrarOntology);

		Set<Set<OWLAxiom>> explanations = debugger.getExplanationsForInconsistency(5, 1000);
		int count = 0;
		for (Set<OWLAxiom> eachExplanation : explanations) {
			System.out.println("Explanation number #" + ++count);

			PrintingHelper.printSet(eachExplanation);
		}

	}

	@Test
	public void test1() throws OWLOntologyCreationException {
		String ontologyPath = "src/test/resources/explanation/inconsistentOnt1.owl";
		testWith(ontologyPath);
	}

	@Test
	public void test2() throws OWLOntologyCreationException {
		String ontologyPath = "src/test/resources/explanation/inconsistentOnt2.owl";
		testWith(ontologyPath);
	}

	@Test
	public void test3() throws OWLOntologyCreationException {
		String ontologyPath = "src/test/resources/explanation/consistencOnt1.owl";
		testWith(ontologyPath);
	}

	@Test
	public void test4() throws OWLOntologyCreationException {
		String ontologyPath = "/Users/kien/git/ORAR-HSHOIF/target/explanation-test/tbox.owl";
		String aboxList = "/Users/kien/git/ORAR-HSHOIF/target/explanation-test/aboxList.txt";
		testWith(ontologyPath, aboxList);
	}
}
