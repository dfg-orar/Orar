package orar.modeling.sameas;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.util.DefaultTestDataFactory;

public class MapBasedSameAsBoxTest {
	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
	OWLObjectProperty R = testData.getRole("R");
	OWLNamedIndividual a = testData.getIndividual("a");
	OWLNamedIndividual b = testData.getIndividual("b");
	OWLNamedIndividual c = testData.getIndividual("c");
	Set<OWLNamedIndividual> bc = testData.getSetOfIndividuals("b", "c");
	Set<OWLNamedIndividual> abc = testData.getSetOfIndividuals("a", "b", "c");

	@Test
	public void addSameAsAssertionTest_ShouldAddNewAssertion() {
		SameAsBox sameasBox = new MapbasedSameAsBox();
		Assert.assertTrue(sameasBox.addSameAsAssertion(a, b));

	}

	@Test
	public void addSameAsAssertionTest_ShouldNotAddDuplicatedAssertion() {
		SameAsBox sameasBox = new MapbasedSameAsBox();
		sameasBox.addSameAsAssertion(a, b);
		Assert.assertFalse(sameasBox.addSameAsAssertion(a, b));

	}

	@Test
	public void addManySameAsAssertion_ShouldAddNewAssertions() {
		SameAsBox sameasBox = new MapbasedSameAsBox();
		Assert.assertTrue(sameasBox.addManySameAsAssertions(a, bc));

	}

	@Test
	public void addManySameAsAssertion_ShouldNotAddDuplicatedAssertions() {
		SameAsBox sameasBox = new MapbasedSameAsBox();
		Assert.assertTrue(sameasBox.addManySameAsAssertions(a, bc));
		Assert.assertFalse(sameasBox.addManySameAsAssertions(a, bc));

	}

	@Test
	public void getSameIndividualsTest() {
		SameAsBox sameasBox = new MapbasedSameAsBox();
		sameasBox.addManySameAsAssertions(a, bc);
		Assert.assertEquals(bc, sameasBox.getSameIndividuals(a));

		sameasBox.addSameAsAssertion(a, a);
		Assert.assertEquals(abc, sameasBox.getSameIndividuals(a));

	}

}
