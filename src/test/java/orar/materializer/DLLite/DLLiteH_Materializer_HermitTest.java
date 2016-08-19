package orar.materializer.DLLite;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import junit.framework.Assert;
import orar.completenesschecker.CompletenessChecker;
import orar.completenesschecker.CompletenessCheckerHorn;
import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.config.LogInfo;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.data.NormalizationDataFactory;
import orar.dlfragmentvalidator.ValidatorDataFactory;
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.HermitDLReasoner;
import orar.io.ontologyreader.DLLiteH_OntologyReader;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.materializer.DLLite.DLLite_Materializer_Hermit;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_Hermit;
import orar.modeling.ontology.OrarOntology;
import orar.util.PrintingHelper;

public class DLLiteH_Materializer_HermitTest {
	@Test
	public void testHasValue1() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/testHasValue1.owl";

		haveTheSameResults(ontologyPath);
	}

	/**
	 * No new entailments
	 */
	@Test
	public void test1() {

		String ontologyPath = "src/test/resources/main/test1.owl";
		haveTheSameResults(ontologyPath);
	}

	@Test
	public void test2() {
		String ontologyPath = "src/test/resources/main/test2.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void test3() {
		String ontologyPath = "src/test/resources/main/test3.owl";
		haveTheSameResults(ontologyPath);
	}

	@Test
	public void test4() {
		String ontologyPath = "src/test/resources/main/test4.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void test5() {
		String ontologyPath = "src/test/resources/main/test5.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void test6() {
		String ontologyPath = "src/test/resources/main/test6.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional() {
		String ontologyPath = "src/test/resources/main/testFunctional.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional2() {
		String ontologyPath = "src/test/resources/main/testFunctional2.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional3() {
		String ontologyPath = "src/test/resources/main/testFunctional3.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional4() {
		String ontologyPath = "src/test/resources/main/testFunctional4.owl";
		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional5() {
		String ontologyPath = "src/test/resources/main/testFunctional5.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional6() {
		String ontologyPath = "src/test/resources/main/testFunctional6.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctional7() {
		String ontologyPath = "src/test/resources/main/testFunctional7.owl";
		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testNominal() {
		String ontologyPath = "src/test/resources/main/testNominal.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testNominal1() {
		String ontologyPath = "src/test/resources/main/testNominal1.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testFunctionalAndNominal() {

		String ontologyPath = "src/test/resources/main/testFunctionalAndNominal.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testNonTrivialIndMerging() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testNontrivialIndMerging.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testNonTrivialIndMerging1() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testNontrivialIndMerging1.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testNominalConcept() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testNominalConcept.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testNonTrivialRoleAssertion() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testNonTrivialRoleAssertion.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testRoleAssertionWithNominal() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionWithNominal.owl";

		haveTheSameResults(ontologyPath);
	}

	@SuppressWarnings("Bugs are from Hermit. It failed to do reasoning over the abstraction: java.util.ConcurrentModificationException")
	@Test
	public void testRoleAssertionByNominalConcept() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionByNominalConcept.owl";

		haveTheSameResults(ontologyPath);
	}

	@SuppressWarnings("Bugs are from Hermit. It failed to do reasoning over the abstraction: java.util.ConcurrentModificationException")
	@Test
	public void testRoleAssertionByNominalConcept2() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionByNominalConcept2.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testRoleAssertionByTran1() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionByTran1.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testRoleAssertionByTran2() {
		Configuration.getInstance().addAllDebugInfos();
		String ontologyPath = "src/test/resources/main/HornALCHOIF/testRoleAssertionByTran2.owl";

		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testLUBM() {
		String ontologyPath = "src/test/resources/lubm/full-lubm.owl";
		haveTheSameResults(ontologyPath);
	}

	@Test
	public void testUOBM_OXSmall4() {
		String ontologyPath = "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-small4.owl";
		haveTheSameResults(ontologyPath);
	}
	// @Test
	// public void testUOBM_OXSmall1() {
	// String ontologyPath =
	// "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-small1.owl";
	// haveTheSameResults(ontologyPath);
	// }

	@Test
	public void testUOBM_OriginSmall() {
		String ontologyTbox = "src/test/resources/uobm-origin/tbox/uobmtbox_origin.owl";
		String aboxList = "src/test/resources/uobm-origin/abox/aboxListOf2.txt";
		haveTheSameResults(ontologyTbox, aboxList);
	}

	/**
	 * Compare result by Abstraction and by OWLReasoner; assert that they have
	 * the same result.
	 * 
	 * @param ontologyPath
	 */
	private void haveTheSameResults(String tbox, String aboxList) {
		AbstractDataFactory.getInstance().clear();
		NormalizationDataFactory.getInstance().clear();
		MetaDataOfOntology.getInstance().clear();
		DataForTransferingEntailments.getInstance().clear();

		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.REASONING_TIME,
				LogInfo.COMPARED_RESULT_INFO);
		Configuration.getInstance().addDebugLevels(DebugLevel.ADDING_MARKING_AXIOMS);
		System.out.println("Loading ontology for abstraction materializer....");
		OntologyReader ontoReader = new DLLiteH_OntologyReader();
		OrarOntology normalizedOrarOntology = ontoReader.getNormalizedOrarOntology(tbox, aboxList);
		int numberOfAssertions = normalizedOrarOntology.getNumberOfInputConceptAssertions()
				+ normalizedOrarOntology.getNumberOfInputRoleAssertions();
		System.out.println(
				"DEBUG***Number of assertions in OrarOntology: " + numberOfAssertions);
		HashSet<OWLClass> orarConceptNames = new HashSet<OWLClass>(normalizedOrarOntology.getConceptNamesInSignature());
		System.out.println("DEBUG*** Number of concept names:"+normalizedOrarOntology.getConceptNamesInSignature().size());
		
		Materializer materializer = new DLLite_Materializer_Hermit(normalizedOrarOntology);

		/*
		 * get result directly from Konclude reasoning over the input ontology
		 */
		System.out.println("Loading ontology for a DL Reasoner....");
		OWLOntology owlOntology = ontoReader.getOWLAPIOntology(tbox, aboxList);
		System.out.println("Number of assertions in OwlapiOntology: " + owlOntology.getABoxAxioms(true).size());
		System.out.println("DEBUG*** Number of concept names:"+owlOntology.getClassesInSignature(true).size());
		Set<OWLClass> owlapiOntoConceptNames = owlOntology.getClassesInSignature(true);
		owlapiOntoConceptNames.removeAll(orarConceptNames);
		System.out.println("DEBUG*** concept names in owlapiontology but not in orarontology:");
		PrintingHelper.printSet(owlapiOntoConceptNames);
		DLReasoner koncludeRealizer = new HermitDLReasoner(owlOntology);

		CompletenessChecker checker = new CompletenessCheckerHorn(materializer, koncludeRealizer);
		checker.computeEntailments();

		Assert.assertTrue(checker.isConceptAssertionComplete());
		// Assert.assertTrue(checker.isSameasComplete());
//		 Assert.assertTrue(checker.isRoleAssertionComplete());

	}

	/**
	 * Compare result by Abstraction and by OWLReasoner; assert that they have
	 * the same result.
	 * 
	 * @param ontologyPath
	 */
	private void haveTheSameResults(String ontologyPath) {
		AbstractDataFactory.getInstance().clear();
		NormalizationDataFactory.getInstance().clear();
		MetaDataOfOntology.getInstance().clear();
		DataForTransferingEntailments.getInstance().clear();
		ValidatorDataFactory.getInstance().clear();

		Configuration.getInstance().getDebuglevels().clear();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC);
		Configuration.getInstance().addLoginfoLevels(LogInfo.COMPARED_RESULT_INFO);
		// Configuration.getInstance().addDebugLevels(DebugLevel.PRINT_MARKING_INDIVIDUALS,
		// DebugLevel.ADDING_MARKING_AXIOMS,
		// DebugLevel.REASONING_ABSTRACTONTOLOGY);

		System.out.println("Loading ontology for abstraction materializer....");
		OntologyReader ontoReader = new DLLiteH_OntologyReader();
		OrarOntology normalizedOrarOntology = ontoReader.getNormalizedOrarOntology(ontologyPath);

		Materializer materializer = new DLLite_Materializer_Hermit(normalizedOrarOntology);

		/*
		 * get result directly from Konclude reasoning over the input ontology
		 */
		System.out.println("Loading ontology for a DL Reasoner....");
		OWLOntology owlOntology = ontoReader.getOWLAPIOntology(ontologyPath);

		DLReasoner dlReasoner = new HermitDLReasoner(owlOntology);
		// koncludeRealizer.computeEntailments();
		// PrintingHelper.printSet(koncludeRealizer.getEntailedRoleAssertions());

		CompletenessChecker checker = new CompletenessCheckerHorn(materializer, dlReasoner);
		checker.computeEntailments();

		Assert.assertTrue(checker.isConceptAssertionComplete());
//		 Assert.assertTrue(checker.isRoleAssertionComplete());
		// Assert.assertTrue(checker.isSameasComplete());

	}
}
