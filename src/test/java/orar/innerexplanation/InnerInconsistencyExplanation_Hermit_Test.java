package orar.innerexplanation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.innerconsistencychecking.InnerConsistencyChecker;
import orar.innerconsistencychecking.InnerConsistencyChecker_Hermit;
import orar.util.PrintingHelper;

public class InnerInconsistencyExplanation_Hermit_Test {
	@Test
	public void test1() throws OWLOntologyCreationException {
		String ontologyPath = "src/test/resources/explanation/inconsistentOnt1.owl";
		Set<Set<OWLAxiom>> explanations = getExplanations(ontologyPath);
		int count = 0;
		for (Set<OWLAxiom> eachExplanation : explanations) {
			System.out.println("Explanation number #" + ++count);

			PrintingHelper.printSet(eachExplanation);
		}

	}

	@Test
	public void test2() throws OWLOntologyCreationException {
		String ontologyPath = "src/test/resources/explanation/inconsistentOnt2.owl";
		Set<Set<OWLAxiom>> explanations = getExplanations(ontologyPath);
		int count = 0;
		for (Set<OWLAxiom> eachExplanation : explanations) {
			System.out.println("Explanation number #" + ++count);

			PrintingHelper.printSet(eachExplanation);
		}

	}
	
	@Test
	public void test3() throws OWLOntologyCreationException {
		String ontologyPath = "src/test/resources/explanation/consistencOnt1.owl";
		Set<Set<OWLAxiom>> explanations = getExplanations(ontologyPath);
		int count = 0;
		for (Set<OWLAxiom> eachExplanation : explanations) {
			System.out.println("Explanation number #" + ++count);

			PrintingHelper.printSet(eachExplanation);
		}

	}


	private Set<Set<OWLAxiom>> getExplanations(String ontologyPath) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyPath));
		InnerInconsistencyExplanation innerConsistencyChecker = new InnerInconsistencyExplanationHermit();
		return innerConsistencyChecker.getExplanations(ontology,5,1000);
	}


}
