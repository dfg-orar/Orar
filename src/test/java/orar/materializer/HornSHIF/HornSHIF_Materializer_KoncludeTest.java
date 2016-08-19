package orar.materializer.HornSHIF;

import org.junit.Test;
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
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.HermitDLReasoner;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.modeling.ontology.OrarOntology;

public class HornSHIF_Materializer_KoncludeTest {

	@Test
	public void testLUBM() {

		String ontologyPath = "src/test/resources/lubm/full-lubm.owl";
		haveTheSameResults(ontologyPath);
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

		Configuration.getInstance().addLoginfoLevels(LogInfo.ABSTRACTION_INFO, LogInfo.INPUTONTOLOGY_INFO,
				LogInfo.COMPARED_RESULT_INFO);
//		Configuration.getInstance().addDebugLevels(DebugLevel.REASONING_ABSTRACTONTOLOGY,
//				DebugLevel.ADDING_MARKING_AXIOMS);
		System.out.println("Loading ontology for abstraction materializer....");
		OntologyReader ontoReader = new HornSHOIF_OntologyReader();
		OrarOntology normalizedOrarOntology = ontoReader.getNormalizedOrarOntology(ontologyPath);

		Materializer materializer = new HornSHIF_Materializer_Konclude(normalizedOrarOntology);

		/*
		 * get result directly from Konclude reasoning over the input ontology
		 */
		System.out.println("Loading ontology for a DL Reasoner....");
		OWLOntology owlOntology = ontoReader.getOWLAPIOntology(ontologyPath);

		DLReasoner koncludeRealizer = new HermitDLReasoner(owlOntology);

		CompletenessChecker checker = new CompletenessCheckerHorn(materializer, koncludeRealizer);
		checker.computeEntailments();

		// Assert.assertTrue(checker.isConceptAssertionComplete());
		Assert.assertTrue(checker.isSameasComplete());
		// Assert.assertTrue(checker.isRoleAssertionComplete());

	}

}
