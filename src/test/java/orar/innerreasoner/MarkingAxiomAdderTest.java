package orar.innerreasoner;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.data.MetaDataOfOntology;
import orar.innerreasoner.MarkingAxiomAdder;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class MarkingAxiomAdderTest {

	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
	/*
	 * Signature
	 */
	OWLNamedIndividual a = testData.getIndividual("a");
	OWLNamedIndividual a1 = testData.getIndividual("a1");
	OWLNamedIndividual a2 = testData.getIndividual("a2");

	OWLNamedIndividual b = testData.getIndividual("b");
	OWLNamedIndividual b1 = testData.getIndividual("b1");
	OWLNamedIndividual b2 = testData.getIndividual("b2");

	OWLNamedIndividual c = testData.getIndividual("c");

	OWLClass A = testData.getConcept("A");
	OWLClass A1 = testData.getConcept("A1");
	OWLClass A2 = testData.getConcept("A2");

	OWLClass B = testData.getConcept("B");
	OWLClass B1 = testData.getConcept("B1");
	OWLClass B2 = testData.getConcept("B2");

	OWLClass C = testData.getConcept("C");
	/*
	 * Nominal-Concept, e.g. concept generated for each nominal.
	 */
	OWLClass NoC = testData.getConcept("NoC");

	OWLObjectProperty R = testData.getRole("R");
	OWLObjectProperty R1 = testData.getRole("R1");
	OWLObjectProperty R2 = testData.getRole("R2");

	OWLObjectProperty S = testData.getRole("S");
	OWLObjectProperty T = testData.getRole("T");
	OWLObjectProperty F = testData.getRole("F");
	OWLObjectProperty InvF = testData.getRole("InvF");
	/*
	 * others
	 */
	OWLOntologyManager ontoManager = OWLManager.createOWLOntologyManager();
	MetaDataOfOntology metaDataOfOntology = MetaDataOfOntology.getInstance();

	@Test
	public void shouldAddAxioms() throws OWLOntologyCreationException {
		this.metaDataOfOntology.getNominalConcepts().add(NoC);
		this.metaDataOfOntology.getFunctionalRoles().add(F);
		this.metaDataOfOntology.getTransitiveRoles().add(T);

		OWLOntology ontology = ontoManager.createOntology();
		MarkingAxiomAdder axiomAdder = new MarkingAxiomAdder(ontology);
		axiomAdder.addMarkingAxioms();
		PrintingHelper.printSet(ontology.getAxioms());
		// TODO: Looks good :-), write code to compare result
	}

}
