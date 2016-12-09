package orar.modeling.roleassertion;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class MapbasedRoleAssertionBoxTest {
	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
	OWLObjectProperty R = testData.getRole("R");
	OWLNamedIndividual a = testData.getIndividual("a");
	OWLNamedIndividual b = testData.getIndividual("b");
	OWLNamedIndividual c = testData.getIndividual("c");
	OWLObjectPropertyAssertionAxiom R_ab = testData.getRoleAssertion(a, R, b);
	OWLObjectPropertyAssertionAxiom R_ac = testData.getRoleAssertion(a, R, c);
	RoleAssertionBox roleAssertionBox = new MapbasedRoleAssertionBox();

	@Before
	public void setup() {
		roleAssertionBox.addRoleAssertion(a, R, b);
		roleAssertionBox.addRoleAssertion(a, R, c);
		roleAssertionBox.addRoleAssertion(a, R, b);
		roleAssertionBox.addRoleAssertion(a, R, c);
	}

	@Test
	public void getOWLAPIRoleAssertionsTest() {

		Set<OWLObjectPropertyAssertionAxiom> set_Rab_Rac = testData.getSetOfRoleAssertions(R_ab, R_ac);

		PrintingHelper.printSet(roleAssertionBox.getOWLAPIRoleAssertions());
		Assert.assertEquals(set_Rab_Rac, roleAssertionBox.getOWLAPIRoleAssertions());
	}

	@Test
	public void getSubjectsInRoleAssertionsTest() {
		PrintingHelper.printSet(roleAssertionBox.getSubjectsInRoleAssertions(R));
		Set<OWLNamedIndividual> set_a = testData.getSetOfIndividuals("a");
		Assert.assertEquals(set_a, roleAssertionBox.getSubjectsInRoleAssertions(R));

	}

	@Test
	public void getObjectsInRoleAssertionsTest() {
		Set<OWLNamedIndividual> set_bc = testData.getSetOfIndividuals("b", "c");
		PrintingHelper.printSet(roleAssertionBox.getObjectsInRoleAssertions(R));
		Assert.assertEquals(set_bc, roleAssertionBox.getObjectsInRoleAssertions(R));

	}

	@Test
	public void getNumberOfRoleAsesrtionsTest() {

		PrintingHelper.printString(this.toString(), roleAssertionBox.getNumberOfRoleAssertions() + "");
		Assert.assertEquals(2, roleAssertionBox.getNumberOfRoleAssertions());
	}

	@Test
	public void addRoleAssertion_ShouldNotAddDuplicatedAssertion() {
		Assert.assertFalse(roleAssertionBox.addRoleAssertion(a, R, b));
	}

}
