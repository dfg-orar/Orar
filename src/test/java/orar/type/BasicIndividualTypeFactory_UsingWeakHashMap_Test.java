package orar.type;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.type.BasicIndividualTypeFactory;
import orar.type.BasicIndividualTypeFactory_UsingWeakHashMap;
import orar.type.IndividualType;
import orar.util.DefaultTestDataFactory;

public class BasicIndividualTypeFactory_UsingWeakHashMap_Test {

	OWLClass A, B;
	OWLObjectProperty R, S;
	BasicIndividualTypeFactory typeFactory = BasicIndividualTypeFactory_UsingWeakHashMap.getInstance();
	Set<OWLClass> conceptNamesA;
	Set<OWLClass> conceptNamesAB;
	Set<OWLClass> conceptNamesBA;
	Set<OWLObjectProperty> rolesR;
	Set<OWLObjectProperty> rolesS;
	Set<OWLObjectProperty> rolesRS;
	Set<OWLObjectProperty> rolesSR;

	@Before
	public void init() {
		DefaultTestDataFactory testDataFactory = DefaultTestDataFactory.getInsatnce();
		A = testDataFactory.getConcept("#A");
		B = testDataFactory.getConcept("#B");
		R = testDataFactory.getRole("#R");
		S = testDataFactory.getRole("#S");
		conceptNamesA = new HashSet<OWLClass>();
		conceptNamesA.add(A);

		conceptNamesAB = new HashSet<OWLClass>();
		conceptNamesAB.add(A);
		conceptNamesAB.add(B);

		conceptNamesBA = new HashSet<OWLClass>();
		conceptNamesBA.add(B);
		conceptNamesBA.add(A);

		rolesR = new HashSet<OWLObjectProperty>();
		rolesR.add(R);

		rolesS = new HashSet<OWLObjectProperty>();
		rolesS.add(S);

		rolesRS = new HashSet<OWLObjectProperty>();
		rolesRS.add(R);
		rolesRS.add(S);

		rolesSR = new HashSet<OWLObjectProperty>();
		rolesSR.add(S);
		rolesSR.add(R);

	}

	/**
	 * Given the same inputs, the typeFactory should return the same output
	 */
	@Test
	public void test1() {

		Set<OWLObjectProperty> sucPropreties = new HashSet<OWLObjectProperty>();
		sucPropreties.add(S);

		IndividualType type1 = typeFactory.getIndividualType(conceptNamesA, rolesR, rolesS);
		IndividualType expectedType1 = typeFactory.getIndividualType(conceptNamesA, rolesR, rolesS);

		IndividualType type2 = typeFactory.getIndividualType(conceptNamesAB, rolesRS, rolesRS);
		IndividualType expectedType2 = typeFactory.getIndividualType(conceptNamesAB, rolesRS, rolesRS);

		Assert.assertEquals(expectedType1, type1);
		Assert.assertEquals(expectedType1, expectedType1);
		Assert.assertEquals(type2, type2);
		Assert.assertEquals(type2, expectedType2);

	}

	/**
	 * Given structurally equal inputs, the typeFactory should return the same
	 * output
	 */
	@Test
	public void test2() {

		IndividualType type1 = typeFactory.getIndividualType(conceptNamesBA, rolesR, rolesSR);
		IndividualType expectedType1 = typeFactory.getIndividualType(conceptNamesAB, rolesR, rolesRS);

		IndividualType type2 = typeFactory.getIndividualType(conceptNamesAB, rolesRS, rolesSR);
		IndividualType expectedType2 = typeFactory.getIndividualType(conceptNamesBA, rolesRS, rolesRS);

		Assert.assertEquals(expectedType1, type1);
		Assert.assertEquals(expectedType1, expectedType1);
		Assert.assertEquals(type2, type2);
		Assert.assertEquals(type2, expectedType2);

	}

}

// /**
// * Test the ability to clean the cache
// */
// @Test
// public void test3() {
//
// IndividualType type1 = typeFactory.getType(conceptNamesBA, rolesR, rolesSR);
// IndividualType type2 = typeFactory.getType(conceptNamesA, rolesR, rolesSR);
// IndividualType type3 = typeFactory.getType(conceptNamesA, rolesRS, rolesSR);
//
// // System.out.println("Cache's size: " + typeFactory.getCacheSize());
// // type1 = null;
// // type2=null;
// // type3=null;
// // //Note: it is unclear when JVM perform GC, thus we just run 1000 times
// // //for (int i = 0; i < 1000; i++)
// // typeFactory.cleanCache();
// // System.out.println("Cache's size after cleaning the cache: "
// // + typeFactory.getCacheSize());
// //
// }
