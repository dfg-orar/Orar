package orar.ruleengine;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.indexing.IndividualIndexer;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.modeling.ontology.MapbasedOrarOntology;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.MapbasedOrarOntology2;
import orar.modeling.ontology2.OrarOntology2;
import orar.rolereasoning.HermitRoleReasoner;
import orar.rolereasoning.RoleReasoner;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class SemiNaiveRuleEngineTest {
	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
	/*
	 * Signature
	 */
	OWLNamedIndividual a = testData.getIndividual("a");
	OWLNamedIndividual a1 = testData.getIndividual("a1");
	OWLNamedIndividual a2 = testData.getIndividual("a2");

	OWLNamedIndividual b = testData.getIndividual("b");
	OWLNamedIndividual b1 = testData.getIndividual("b1");
	OWLNamedIndividual b2 = testData.getIndividual("b2");

	OWLNamedIndividual c = testData.getIndividual("c");
	OWLNamedIndividual c1 = testData.getIndividual("c1");
	OWLNamedIndividual c2 = testData.getIndividual("c2");

	OWLNamedIndividual d = testData.getIndividual("d");

	OWLNamedIndividual o = testData.getIndividual("o");

	OWLClass A = testData.getConcept("A");
	OWLClass A1 = testData.getConcept("A1");
	OWLClass A2 = testData.getConcept("A2");

	OWLClass B = testData.getConcept("B");
	OWLClass B1 = testData.getConcept("B1");
	OWLClass B2 = testData.getConcept("B2");

	OWLClass C = testData.getConcept("C");

	OWLClass No = testData.getConcept("No");
	/*
	 * Nominal-Concept, e.g. concept generated for each nominal.
	 */
	OWLClass NoC = testData.getConcept("NoC");

	OWLObjectProperty R = testData.getRole("R");
	OWLObjectProperty R1 = testData.getRole("R1");
	OWLObjectProperty R2 = testData.getRole("R2");

	OWLObjectProperty S = testData.getRole("S");

	OWLObjectProperty T = testData.getRole("T");
	OWLObjectProperty invT = testData.getRole("InvT");
	OWLObjectProperty F = testData.getRole("F");
	OWLObjectProperty InvF = testData.getRole("InvF");

	/*
	 * Others
	 */

	OWLObjectProperty funcRole = testData.getRole("funcRole");
	OWLObjectProperty invFuncRole = testData.getRole("invFuncRole");
	DataForTransferingEntailments sharedMap = DataForTransferingEntailments.getInstance();
	MetaDataOfOntology metaDataOfOntology = MetaDataOfOntology.getInstance();

	IndividualIndexer indexer= IndividualIndexer.getInstance();
	@Test
	public void testSubRoleRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology2 orarOntology = new MapbasedOrarOntology2();
		metaDataOfOntology.getSubRoleMap().put(R, testData.getSetOfRoles(S));
		orarOntology.addRoleAssertion(indexer.getIndexOfOWLIndividual(a), R, indexer.getIndexOfOWLIndividual(b));
		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		ruleEngine.materialize();
		// after computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
	}

	@Test
	public void testFuncRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology2 orarOntology = new MapbasedOrarOntology2();
		orarOntology.addRoleAssertion(indexer.getIndexOfOWLIndividual(a), F, indexer.getIndexOfOWLIndividual(b1));
		orarOntology.addRoleAssertion(indexer.getIndexOfOWLIndividual(a), F, indexer.getIndexOfOWLIndividual(b2));
		orarOntology.addRoleAssertion(indexer.getIndexOfOWLIndividual(b1), F, indexer.getIndexOfOWLIndividual(c1));
		orarOntology.addRoleAssertion(indexer.getIndexOfOWLIndividual(b2), F, indexer.getIndexOfOWLIndividual(c2));
		this.metaDataOfOntology.getFunctionalRoles().add(F);

		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		PrintingHelper.printSet(orarOntology.getSameIndividuals(indexer.getIndexOfOWLIndividual(b1)));
		PrintingHelper.printSet(orarOntology.getSameIndividuals(indexer.getIndexOfOWLIndividual(c1)));
		ruleEngine.materialize();
		// after computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		PrintingHelper.printSet(orarOntology.getSameIndividuals(indexer.getIndexOfOWLIndividual(b1)));
		PrintingHelper.printSet(orarOntology.getSameIndividuals(indexer.getIndexOfOWLIndividual(c1)));
	}

	@Test
	public void testTranRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology orarOntology = new MapbasedOrarOntology();
		orarOntology.addRoleAssertion(a, T, b);
		orarOntology.addRoleAssertion(b, T, c);
		orarOntology.addRoleAssertion(c, T, d);

		this.metaDataOfOntology.getTransitiveRoles().add(T);

		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());

		ruleEngine.materialize();
		// after computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());

	}
	
	@Test
	public void testSameasRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology orarOntology = new MapbasedOrarOntology();
		orarOntology.addSameasAssertion(testData.getSetOfIndividuals(a, b));
		orarOntology.addSameasAssertion(testData.getSetOfIndividuals(c, b));
		orarOntology.addSameasAssertion(testData.getSetOfIndividuals(d, b));
		
		

		
		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());

		ruleEngine.materialize();
		// after computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(a));
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(b));
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(c));
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(d
				));

	}
	
	@Test
	public void testAllRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology orarOntology = new MapbasedOrarOntology();
		metaDataOfOntology.getTransitiveRoles().add(T);
		metaDataOfOntology.getSubRoleMap().put(T, testData.getSetOfRoles(R));
		orarOntology.addRoleAssertion(a, T, b);
		orarOntology.addRoleAssertion(b, T, c);
		orarOntology.addRoleAssertion(c, R, d);
		
		

		
		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		System.out.println("Before reasoning");
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());

		ruleEngine.materialize();
		// after computing deductive closure
		System.out.println("After reasoning");
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());

		

	}
	
	@Test
	public void seeHowFastWithCorbunLUBM(){
		/*
		 * Load ontology where TBox and ABox are in separated files.
		 */
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";

		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "/Users/kien/Cavender&Coburn_1992-D.owl.xml";
		OntologyReader ontoReader= new HornSHOIF_OntologyReader();
		OrarOntology orarOntology=ontoReader.getNormalizedOrarOntology(allInOneOntologyName);
		
		RoleReasoner hermitRoleReasoner= new HermitRoleReasoner(orarOntology.getTBoxAxioms());
		hermitRoleReasoner.doReasoning();
		metaDataOfOntology.getSubRoleMap().putAll(hermitRoleReasoner.getRoleHierarchyAsMap());
		
		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		long startReasoning = System.currentTimeMillis();
		System.out.println(orarOntology.getNumberOfInputRoleAssertions());
		ruleEngine.materialize();
		long endReasoning = System.currentTimeMillis();
		long reasoningTime = (endReasoning-startReasoning)/1000;
		System.out.println("Reasoning time: "+reasoningTime);
		System.out.println(orarOntology.getNumberOfRoleAssertions());
	}
}
