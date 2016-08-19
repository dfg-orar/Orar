package orar.normalization.transitivity;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import junit.framework.Assert;
import orar.data.NormalizationDataFactory;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class TransitivityNormalizerWithHermitTest {

	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
	OWLClass A = testData.getConcept("A");
	OWLClass B = testData.getConcept("B");
	OWLObjectProperty R = testData.getRole("R");
	OWLObjectProperty T = testData.getRole("T");
	OWLObjectProperty T1 = testData.getRole("T1");
	OWLObjectProperty T2 = testData.getRole("T2");

	OWLOntology ontology1;
	OWLOntology ontology2;
	OWLOntology ontology3;
	OWLOntology ontology4;

	/**
	 * Create ontology1:<br>
	 * Subclass(A, forall R. B) <br>
	 * SubRole (T,R) <br>
	 * Trans(T)
	 */
	@Before
	public void initOntology1() {
		ontology1 = testData.getOntology();
		testData.addAxiomsTo(ontology1, testData.getSubClassAxiom(A, testData.getForAllAxiom(R, B)));
		testData.addAxiomsTo(ontology1, testData.getTransitivityAxiom(T));
		testData.addAxiomsTo(ontology1, testData.getSubRoleAxiom(T, R));

	}

	@Test
	public void testWithOntology1() {
		/*
		 * Note to reset counter of normalization data factory, to predict
		 * correct names
		 */
		NormalizationDataFactory.getInstance().clear();
		TransitivityNormalizer transNormalizer = new TransitivityNormalizerWithHermit(ontology1);
		transNormalizer.normalizeTransitivity();
		OWLOntology outPutOntology = transNormalizer.getResultingOntology();
		System.out.println("Resulting ontology:");
		PrintingHelper.printSet(outPutOntology.getAxioms());
		/*
		 * compare results
		 */
		Set<OWLAxiom> expectedAxioms = new HashSet<OWLAxiom>();
		expectedAxioms.addAll(ontology1.getAxioms());

		OWLClass C1 = testData
				.getConceptWithFullname(NormalizationDataFactory.getInstance().getTransivityConceptPrefix() + 1);
		expectedAxioms.add(testData.getSubClassAxiom(A, testData.getForAllAxiom(T, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, testData.getForAllAxiom(T, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, B));

		Assert.assertEquals(expectedAxioms, outPutOntology.getAxioms());
	}

	/**
	 * Create ontology2:<br>
	 * Subclass(A, forall T. B) <br>
	 * Trans(T)
	 */
	@Before
	public void initOntology2() {
		ontology2 = testData.getOntology();
		testData.addAxiomsTo(ontology2, testData.getSubClassAxiom(A, testData.getForAllAxiom(T, B)));
		testData.addAxiomsTo(ontology2, testData.getTransitivityAxiom(T));
	}

	@Test
	public void testWithOntology2() {
		/*
		 * Note to reset counter of normalization data factory, to predict
		 * correct names
		 */
		NormalizationDataFactory.getInstance().clear();
		TransitivityNormalizer transNormalizer = new TransitivityNormalizerWithHermit(ontology2);
		transNormalizer.normalizeTransitivity();
		OWLOntology outPutOntology = transNormalizer.getResultingOntology();
		System.out.println("Resulting ontology:");
		PrintingHelper.printSet(outPutOntology.getAxioms());
		/*
		 * compare results
		 */
		Set<OWLAxiom> expectedAxioms = new HashSet<OWLAxiom>();
		expectedAxioms.addAll(ontology2.getAxioms());

		OWLClass C1 = testData
				.getConceptWithFullname(NormalizationDataFactory.getInstance().getTransivityConceptPrefix() + 1);
		expectedAxioms.add(testData.getSubClassAxiom(A, testData.getForAllAxiom(T, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, testData.getForAllAxiom(T, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, B));

		Assert.assertEquals(expectedAxioms, outPutOntology.getAxioms());
	}

	/**
	 * Create ontology3:<br>
	 * Subclass(A, forall R. B) <br>
	 * Trans(T) <br>
	 * Subrole(T, T1)<br>
	 * Subrole(T1, T2)<br>
	 * Subrole(T2, R)<br>
	 * 
	 */
	@Before
	public void initOntology3() {
		ontology3 = testData.getOntology();
		testData.addAxiomsTo(ontology3, testData.getSubClassAxiom(A, testData.getForAllAxiom(R, B)));
		testData.addAxiomsTo(ontology3, testData.getTransitivityAxiom(T));
		testData.addAxiomsTo(ontology3, testData.getSubRoleAxiom(T, T1));
		testData.addAxiomsTo(ontology3, testData.getSubRoleAxiom(T1, T2));
		testData.addAxiomsTo(ontology3, testData.getSubRoleAxiom(T2, R));
	}

	@Test
	public void testWithOntology3() {
		/*
		 * Note to reset counter of normalization data factory, to predict
		 * correct names
		 */
		NormalizationDataFactory.getInstance().clear();
		TransitivityNormalizer transNormalizer = new TransitivityNormalizerWithHermit(ontology3);
		transNormalizer.normalizeTransitivity();
		OWLOntology outPutOntology = transNormalizer.getResultingOntology();
		System.out.println("Resulting ontology:");
		PrintingHelper.printSet(outPutOntology.getAxioms());
		/*
		 * compare results
		 */
		Set<OWLAxiom> expectedAxioms = new HashSet<OWLAxiom>();
		expectedAxioms.addAll(ontology3.getAxioms());

		OWLClass C1 = testData
				.getConceptWithFullname(NormalizationDataFactory.getInstance().getTransivityConceptPrefix() + 1);
		expectedAxioms.add(testData.getSubClassAxiom(A, testData.getForAllAxiom(T, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, testData.getForAllAxiom(T, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, B));

		Assert.assertEquals(expectedAxioms, outPutOntology.getAxioms());
	}

	/**
	 * Create ontology4:<br>
	 * Subclass(A, forall R. B) <br>
	 * Trans(T1) <br>
	 * Trans(T2) <br>
	 * Subrole(T1, R)<br>
	 * Subrole(T2, R)<br>
	 * 
	 */
	@Before
	public void initOntology4() {
		ontology4 = testData.getOntology();
		testData.addAxiomsTo(ontology4, testData.getSubClassAxiom(A, testData.getForAllAxiom(R, B)));
		testData.addAxiomsTo(ontology4, testData.getTransitivityAxiom(T1));
		testData.addAxiomsTo(ontology4, testData.getTransitivityAxiom(T2));
		testData.addAxiomsTo(ontology4, testData.getSubRoleAxiom(T1, R));
		testData.addAxiomsTo(ontology4, testData.getSubRoleAxiom(T2, R));
	}

	@Test
	public void testWithOntology4() {
		/*
		 * Note to reset counter of normalization data factory, to predict
		 * correct names
		 */
		NormalizationDataFactory.getInstance().clear();
		TransitivityNormalizer transNormalizer = new TransitivityNormalizerWithHermit(ontology4);
		transNormalizer.normalizeTransitivity();
		OWLOntology outPutOntology = transNormalizer.getResultingOntology();
		System.out.println("Resulting ontology:");
		PrintingHelper.printSet(outPutOntology.getAxioms());
		/*
		 * compare results
		 */
		Set<OWLAxiom> expectedAxioms = new HashSet<OWLAxiom>();
		expectedAxioms.addAll(ontology4.getAxioms());

		OWLClass C1 = testData
				.getConceptWithFullname(NormalizationDataFactory.getInstance().getTransivityConceptPrefix() + 1);
		expectedAxioms.add(testData.getSubClassAxiom(A, testData.getForAllAxiom(T1, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, testData.getForAllAxiom(T1, C1)));
		expectedAxioms.add(testData.getSubClassAxiom(C1, B));

		OWLClass C2 = testData
				.getConceptWithFullname(NormalizationDataFactory.getInstance().getTransivityConceptPrefix() + 1);
		expectedAxioms.add(testData.getSubClassAxiom(A, testData.getForAllAxiom(T2, C2)));
		expectedAxioms.add(testData.getSubClassAxiom(C2, testData.getForAllAxiom(T2, C2)));
		expectedAxioms.add(testData.getSubClassAxiom(C2, B));

		/*
		 * Cannot control the order of normalization, so we cannot predict exact
		 * names in the resulting ontology.
		 */
	}

}
