package orar.abstraction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import junit.framework.Assert;
import orar.modeling.ontology.MapbasedOrarOntology;
import orar.modeling.ontology.OrarOntology;
import orar.type.BasicIndividualTypeFactory;
import orar.type.BasicIndividualTypeFactory_UsingWeakHashMap;
import orar.type.IndividualType;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class Basic_TypeComputorTest {
	/*
	 * Signature
	 */
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

	/*
	 * Others
	 */
	BasicIndividualTypeFactory typeFactory = BasicIndividualTypeFactory_UsingWeakHashMap.getInstance();
	TypeComputor typeComputor = new BasicTypeComputor();

	@Test
	public void shouldComputeTypeProperly() {
		OrarOntology orarOntology = new MapbasedOrarOntology();
		/*
		 * Note to add individuals to the signature of the ontology. When we
		 * load ontology from file, the OntologyReader will do the job.
		 */
		orarOntology.addIndividualsToSignature(testData.getSetOfIndividuals(a1, a2, b1, b2));

		orarOntology.addConceptAssertion(a1, A1);
		orarOntology.addConceptAssertion(a2, A2);

		orarOntology.addConceptAssertion(b1, B1);
		orarOntology.addConceptAssertion(b2, B2);

		orarOntology.addRoleAssertion(a1, R1, b1);
		orarOntology.addRoleAssertion(a2, R2, b2);

		/*
		 * Note that the ontology should be closed under equality. In the
		 * procedure, the RuleReasoner will compute the closure before we
		 * compute types.
		 */
		orarOntology.addSameAsAssertion(a1, a2);
		orarOntology.addSameAsAssertion(a2, a1);

		/*
		 * Compute types
		 */
		Map<IndividualType, Set<OWLNamedIndividual>> typeMap2Individuals = typeComputor.computeTypes(orarOntology);
		PrintingHelper.printMap(typeMap2Individuals);
		/*
		 * Compare results
		 */
		// Type for a1 and a2
		Set<OWLClass> set_A1A2 = testData.getSetOfConcepts(A1, A2);
		Set<OWLObjectProperty> set_R1R2 = testData.getSetOfRoles("R1", "R2");
		Set<OWLObjectProperty> emptySetOfRoles = new HashSet<OWLObjectProperty>();
		IndividualType typeFor_a1a2 = typeFactory.getIndividualType(set_A1A2, emptySetOfRoles, set_R1R2);
		Set<OWLNamedIndividual> set_a1a2 = testData.getSetOfIndividuals(a1, a2);
		Assert.assertEquals(set_a1a2, typeMap2Individuals.get(typeFor_a1a2));

		// Type for b1
		IndividualType typeFor_b1 = typeFactory.getIndividualType(testData.getSetOfConcepts(B1),
				testData.getSetOfRoles(R1), emptySetOfRoles);
		Set<OWLNamedIndividual> set_b1 = testData.getSetOfIndividuals(b1);
		Assert.assertEquals(set_b1, typeMap2Individuals.get(typeFor_b1));

		// Type for b2
		IndividualType typeFor_b2 = typeFactory.getIndividualType(testData.getSetOfConcepts(B2),
				testData.getSetOfRoles(R2), emptySetOfRoles);
		Set<OWLNamedIndividual> set_b2 = testData.getSetOfIndividuals(b2);
		Assert.assertEquals(set_b2, typeMap2Individuals.get(typeFor_b2));

	}
}
