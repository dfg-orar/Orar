package orar.materializer.HornSHOIF;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.data.NormalizationDataFactory;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.modeling.ontology2.OrarOntology2;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class HornSHOIF_Materializer_Konclude_Optimized_TestOutPut {
	@Test
	public void testOutPutOWLAPIAssertions1() {

		/*
		 * FunctionalObjectProperty(:F) # Individual: :a (:a)
		 * 
		 * ClassAssertion(:A :a) ObjectPropertyAssertion(:F :a :b1)
		 * ObjectPropertyAssertion(:F :a :b2)
		 * 
		 * # Individual: :b1 (:b1)
		 * 
		 * ObjectPropertyAssertion(:F :b1 :c1)
		 * 
		 * # Individual: :b2 (:b2)
		 * 
		 * ObjectPropertyAssertion(:F :b2 :c2)
		 * 
		 * # Individual: :c1 (:c1)
		 * 
		 * ClassAssertion(:C :c1)
		 * 
		 */
		String ontologyPath = "src/test/resources/output_assertions/test1.owl";

		AbstractDataFactory.getInstance().clear();
		NormalizationDataFactory.getInstance().clear();
		MetaDataOfOntology.getInstance().clear();
		DataForTransferingEntailments.getInstance().clear();
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();

		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.REASONING_TIME, LogInfo.LOADING_TIME,
				LogInfo.DETAILED_STATISTIC);
		// Configuration.getInstance().addLoginfoLevels(LogInfo.TUNING_SAMEAS);
		// Configuration.getInstance().addDebugLevels(
		// DebugLevel.PRINT_MARKING_INDIVIDUALS,
		// DebugLevel.ADDING_MARKING_AXIOMS);
		System.out.println("Loading ontology for abstraction materializer....");
		OntologyReader ontoReader = new HornSHOIF_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontoReader.getNormalizedOrarOntology(ontologyPath);
		// System.out.println("Print indexing after reading ontology");
		// PrintingHelper.printMap(IndividualIndexer.getInstance().viewMapIndividuslString2Integer());
		Materializer materializer = new HornSHOIF_Materializer_KoncludeOptimized(normalizedOrarOntology);
		materializer.materialize();
		Set<OWLAxiom> actualEntailedAssertions = materializer.getOrarOntology().getOWLAPIMaterializedAssertions();
		PrintingHelper.printSet(actualEntailedAssertions);

		DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
		Set<OWLAxiom> expectedAssertions = new HashSet<>();
		OWLClassAssertionAxiom Aa = testData.getConceptAssertion("A", "a");
		expectedAssertions.add(Aa);
		OWLClassAssertionAxiom Ba = testData.getConceptAssertion("B", "a");
		expectedAssertions.add(Ba);
		OWLClassAssertionAxiom Cc1 = testData.getConceptAssertion("C", "c1");
		expectedAssertions.add(Cc1);
		OWLClassAssertionAxiom Cc2 = testData.getConceptAssertion("C", "c2");
		expectedAssertions.add(Cc2);
		OWLObjectPropertyAssertionAxiom Fab1 = testData.getRoleAssertion("a", "F", "b1");
		expectedAssertions.add(Fab1);
		OWLObjectPropertyAssertionAxiom Fab2 = testData.getRoleAssertion("a", "F", "b2");
		expectedAssertions.add(Fab2);
		OWLObjectPropertyAssertionAxiom Fb1c1 = testData.getRoleAssertion("b1", "F", "c1");
		expectedAssertions.add(Fb1c1);
		OWLObjectPropertyAssertionAxiom Fb1c2 = testData.getRoleAssertion("b1", "F", "c2");
		expectedAssertions.add(Fb1c2);
		OWLObjectPropertyAssertionAxiom Fb2c1 = testData.getRoleAssertion("b2", "F", "c1");
		expectedAssertions.add(Fb2c1);
		OWLObjectPropertyAssertionAxiom Fb2c2 = testData.getRoleAssertion("b2", "F", "c2");
		expectedAssertions.add(Fb2c2);
		OWLSameIndividualAxiom b1equalb2 = testData.getSameaAsAssertion("b1","b2");
		OWLSameIndividualAxiom c1equalc2 = testData.getSameaAsAssertion("c1","c2");
		expectedAssertions.add(b1equalb2);
		expectedAssertions.add(c1equalc2);		
		
		Assert.assertEquals(expectedAssertions,actualEntailedAssertions);

	}

}
