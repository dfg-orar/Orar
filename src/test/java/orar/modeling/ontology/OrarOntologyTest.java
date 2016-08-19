package orar.modeling.ontology;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import junit.framework.Assert;
import orar.util.DefaultTestDataFactory;

public class OrarOntologyTest {
	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();

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

	OWLObjectProperty R = testData.getRole("R");
	OWLObjectProperty R1 = testData.getRole("R1");
	OWLObjectProperty R2 = testData.getRole("R2");

	OWLObjectProperty S = testData.getRole("S");

	@Test
	public void shouldAddAndGetConceptAssertionsProperly() {
		OrarOntology orarOntology = new MapbasedOrarOntology();

		orarOntology.addConceptAssertion(a1, A1);
		orarOntology.addConceptAssertion(a2, A2);

		orarOntology.addConceptAssertion(b1, B1);
		orarOntology.addConceptAssertion(b2, B2);

		orarOntology.addRoleAssertion(a1, R1, b1);
		orarOntology.addRoleAssertion(a2, R2, b2);

		orarOntology.addSameAsAssertion(a1, a2);

		Set<OWLClass> set_A1 = testData.getSetOfConcepts("A1");
		Set<OWLClass> set_A2 = testData.getSetOfConcepts("A2");

		Assert.assertEquals(set_A1, orarOntology.getAssertedConcepts(a1));
		Assert.assertEquals(set_A2, orarOntology.getAssertedConcepts(a2));
	}

	@Test
	public void shouldAddAndGetRoleAssertionsProperly() {
		OrarOntology orarOntology = new MapbasedOrarOntology();

		orarOntology.addRoleAssertion(a1, R1, b1);
		orarOntology.addRoleAssertion(a2, R2, b2);

		Map<OWLObjectProperty, Set<OWLNamedIndividual>> map_fora1 = testData.getMapFromRole2Individuals(R1,
				testData.getSetOfIndividuals(b1));
		Map<OWLObjectProperty, Set<OWLNamedIndividual>> map_fora2 = testData.getMapFromRole2Individuals(R2,
				testData.getSetOfIndividuals(b2));

		Map<OWLObjectProperty, Set<OWLNamedIndividual>> map_forb1 = testData.getMapFromRole2Individuals(R1,
				testData.getSetOfIndividuals(a1));
		Map<OWLObjectProperty, Set<OWLNamedIndividual>> map_forb2 = testData.getMapFromRole2Individuals(R2,
				testData.getSetOfIndividuals(a2));

		Assert.assertEquals(map_fora1, orarOntology.getSuccessorRoleAssertionsAsMap(a1));
		Assert.assertEquals(map_fora2, orarOntology.getSuccessorRoleAssertionsAsMap(a2));
		Assert.assertEquals(map_forb1, orarOntology.getPredecessorRoleAssertionsAsMap(b1));
		Assert.assertEquals(map_forb2, orarOntology.getPredecessorRoleAssertionsAsMap(b2));

	}

	@Test
	public void shouldAddAndGetSameasAssertionsProperly() {
		OrarOntology orarOntology = new MapbasedOrarOntology();

		orarOntology.addSameAsAssertion(a1, a2);

		Set<OWLNamedIndividual> set_a2 = testData.getSetOfIndividuals(a2);

		Assert.assertEquals(set_a2, orarOntology.getSameIndividuals(a1));

	}

}
