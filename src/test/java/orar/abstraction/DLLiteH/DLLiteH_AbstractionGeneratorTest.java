package orar.abstraction.DLLiteH;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.BasicTypeComputor;
import orar.abstraction.TypeComputor;
import orar.abstraction.DLLiteH.DLLiteR_AbstractionGenerator;
import orar.io.ontologyreader.DLLiteH_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;
import orar.util.PrintingHelper;

public class DLLiteH_AbstractionGeneratorTest {

	@Test
	public void test1() {
		String ontologyPath = "src/test/resources/abstraction_generator/dllite/test1.owl";
		getAbstraction(ontologyPath);

	}

	@Test
	public void test2() {
		String ontologyPath = "src/test/resources/abstraction_generator/dllite/test2.owl";
		getAbstraction(ontologyPath);
	}
	
	@Test
	public void test3() {
		String ontologyPath = "src/test/resources/abstraction_generator/dllite/test3.owl";
		getAbstraction(ontologyPath);
	}

	private OWLOntology getAbstraction(String ontologyPath) {
		OntologyReader ontologyReader = new DLLiteH_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(ontologyPath);

		TypeComputor typeComputor = new BasicTypeComputor(normalizedOrarOntology);
		Map<IndividualType, Set<Integer>> typeMap2Individuals = typeComputor
				.computeTypes();

		AbstractionGenerator abstractionGenerator = new DLLiteR_AbstractionGenerator(normalizedOrarOntology,
				typeMap2Individuals);
		OWLOntology abstractOntology = abstractionGenerator.getAbstractOntology();

		System.out.println("Abstract ontology:");
		PrintingHelper.printSet(abstractOntology.getAxioms());
		return abstractOntology;
	}
}
