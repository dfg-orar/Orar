package orar.modeling.conceptassertion;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import orar.modeling.conceptassertion.ConceptAssertionBox;
import orar.modeling.conceptassertion.MapbasedConceptAssertionBox;
import orar.util.DefaultTestDataFactory;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class MapbasedConceptAssertionBoxTest {

	private OWLClass A, B, C;
	private OWLNamedIndividual a, b, c;
	private OWLClassAssertionAxiom Aa, Ba, Ca, Ab, Bb, Cc;
	private Set<OWLClassAssertionAxiom> expectedAssertions;
	private ConceptAssertionBox conceptAssertionBox = new MapbasedConceptAssertionBox();

	@Before
	public void init() {
		DefaultTestDataFactory testDataFactory = DefaultTestDataFactory.getInsatnce();
		A = testDataFactory.getConcept("A");
		B = testDataFactory.getConcept("B");
		C = testDataFactory.getConcept("C");
		a = testDataFactory.getIndividual("a");
		b = testDataFactory.getIndividual("b");
		c = testDataFactory.getIndividual("c");

		Aa = testDataFactory.getConceptAssertion(A, a);
		Ba = testDataFactory.getConceptAssertion(B, a);
		Ca = testDataFactory.getConceptAssertion(C, a);

		Ab = testDataFactory.getConceptAssertion(A, b);
		Bb = testDataFactory.getConceptAssertion(B, b);

		Cc = testDataFactory.getConceptAssertion(C, c);

		expectedAssertions = new HashSet<OWLClassAssertionAxiom>();
		expectedAssertions.add(Aa);
		expectedAssertions.add(Ba);
		expectedAssertions.add(Ca);

		expectedAssertions.add(Ab);
		expectedAssertions.add(Bb);

		expectedAssertions.add(Cc);

		conceptAssertionBox.addConceptAssertion(a, A);
		conceptAssertionBox.addConceptAssertion(a, B);
		conceptAssertionBox.addConceptAssertion(a, C);

		conceptAssertionBox.addConceptAssertion(b, A);
		conceptAssertionBox.addConceptAssertion(b, B);

		conceptAssertionBox.addConceptAssertion(c, C);

		/*
		 * attempt to add duplicated axioms.
		 * 
		 */
		conceptAssertionBox.addConceptAssertion(a, A);
		conceptAssertionBox.addConceptAssertion(a, B);
		conceptAssertionBox.addConceptAssertion(a, C);

		conceptAssertionBox.addConceptAssertion(b, A);
		conceptAssertionBox.addConceptAssertion(b, B);

		conceptAssertionBox.addConceptAssertion(c, C);
	}

	@Test
	public void getAssertedConceptsTest() {

		Set<OWLClass> ABC = new HashSet<OWLClass>();
		ABC.add(A);
		ABC.add(B);
		ABC.add(C);

		Assert.assertEquals(ABC, conceptAssertionBox.getAssertedConcepts(a));

		Set<OWLClass> AB = new HashSet<OWLClass>();
		AB.add(A);
		AB.add(B);
		Assert.assertEquals(AB, conceptAssertionBox.getAssertedConcepts(b));

		HashSet<Object> CSet = new HashSet<Object>();
		CSet.add(C);
		Assert.assertEquals(CSet, conceptAssertionBox.getAssertedConcepts(c));

	}

	@Test
	public void getOWLAPIConceptAsertionsTest() {
		Assert.assertEquals(expectedAssertions, conceptAssertionBox.getOWLAPIConceptAssertions());
	}

	@Test
	public void getNumberOfConceptAssertionsTest() {
		Assert.assertEquals(6, conceptAssertionBox.getNumberOfConceptAssertions());
	}

	@Test
	public void addConceptAssertion_ShouldNotAddDuplicatedAxiom() {
		Assert.assertFalse(conceptAssertionBox.addConceptAssertion(a, A));
	}

}
