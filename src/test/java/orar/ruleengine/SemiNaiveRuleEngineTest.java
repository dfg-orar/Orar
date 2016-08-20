package orar.ruleengine;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.indexing.IndividualIndexer;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.modeling.ontology2.MapbasedOrarOntology2;
import orar.modeling.ontology2.OrarOntology2;
import orar.rolereasoning.HermitRoleReasoner;
import orar.rolereasoning.RoleReasoner;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class SemiNaiveRuleEngineTest {
	DefaultTestDataFactory testData = DefaultTestDataFactory.getInsatnce();
	IndividualIndexer indexer = IndividualIndexer.getInstance();

	/*
	 * Signature
	 */
	Integer a = indexer.getIndexOfOWLIndividual(testData.getIndividual("a"));
	Integer a1 = indexer.getIndexOfOWLIndividual(testData.getIndividual("a1"));
	Integer a2 = indexer.getIndexOfOWLIndividual(testData.getIndividual("a2"));

	Integer b = indexer.getIndexOfOWLIndividual(testData.getIndividual("b"));
	Integer b1 = indexer.getIndexOfOWLIndividual(testData.getIndividual("b1"));
	Integer b2 = indexer.getIndexOfOWLIndividual(testData.getIndividual("b2"));

	Integer c = indexer.getIndexOfOWLIndividual(testData.getIndividual("c"));
	Set<Integer> a1a2 = new HashSet<>();
	Set<Integer> b1b2 = new HashSet<>();
	Set<Integer> a1a2b1b2 = new HashSet<>();
	Set<Integer> b1Set = new HashSet<>();
	Set<Integer> a1Set = new HashSet<>();

	Set<Integer> ab = new HashSet<>();
	Set<Integer> cb = new HashSet<>();
	Set<Integer> db = new HashSet<>();

	Integer c1 = indexer.getIndexOfOWLIndividual(testData.getIndividual("c1"));
	Integer c2 = indexer.getIndexOfOWLIndividual(testData.getIndividual("c2"));
	Integer d = indexer.getIndexOfOWLIndividual(testData.getIndividual("d"));
	Integer o = indexer.getIndexOfOWLIndividual(testData.getIndividual("o"));

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

	@Before
	public void init() {
		indexer.clear();
		a1a2.add(a1);
		a1a2.add(a2);

		b1b2.add(b1);
		b1b2.add(b2);

		a1a2b1b2.add(a1);
		a1a2b1b2.add(a2);
		a1a2b1b2.add(b1);
		a1a2b1b2.add(b2);

		b1Set.add(b1);
		a1Set.add(a1);

		ab.add(a);
		ab.add(b);

		cb.add(c);
		cb.add(b);

		db.add(d);
		db.add(b);
	}

	@Test
	public void testSubRoleRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology2 orarOntology = new MapbasedOrarOntology2();
		metaDataOfOntology.getSubRoleMap().put(R, testData.getSetOfRoles(S));
		orarOntology.addRoleAssertion(a, R, b);
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
		orarOntology.addRoleAssertion(a, F, b1);
		orarOntology.addRoleAssertion(a, F, b2);
		orarOntology.addRoleAssertion(b1, F, c1);
		orarOntology.addRoleAssertion(b2, F, c2);
		this.metaDataOfOntology.getFunctionalRoles().add(F);

		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		PrintingHelper.printSet(orarOntology.getSameIndividuals(b1));
		PrintingHelper.printSet(orarOntology.getSameIndividuals(c1));
		ruleEngine.materialize();
		// after computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		PrintingHelper.printSet(orarOntology.getSameIndividuals(b1));
		PrintingHelper.printSet(orarOntology.getSameIndividuals(c1));
	}

	@Test
	public void testTranRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology2 orarOntology = new MapbasedOrarOntology2();
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
		OrarOntology2 orarOntology = new MapbasedOrarOntology2();
		orarOntology.addSameasAssertion(ab);
		orarOntology.addSameasAssertion(cb);
		orarOntology.addSameasAssertion(db);

		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		// before computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());

		ruleEngine.materialize();
		// after computing deductive closure
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(a));
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(b));
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(c));
		PrintingHelper.printSet(orarOntology.getSameasBox().getSameIndividuals(d));

	}

	@Test
	public void testAllRule() {
		metaDataOfOntology.clear();
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology2 orarOntology = new MapbasedOrarOntology2();
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
	public void seeHowFastWithCorbunLUBM() {
		/*
		 * Load ontology where TBox and ABox are in separated files.
		 */
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";

		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "/Users/kien/Cavender&Coburn_1992-D.owl.xml";
		OntologyReader ontoReader = new HornSHOIF_OntologyReader();
		OrarOntology2 orarOntology = ontoReader.getNormalizedOrarOntology(allInOneOntologyName);

		RoleReasoner hermitRoleReasoner = new HermitRoleReasoner(orarOntology.getTBoxAxioms());
		hermitRoleReasoner.doReasoning();
		metaDataOfOntology.getSubRoleMap().putAll(hermitRoleReasoner.getRoleHierarchyAsMap());

		RuleEngine ruleEngine = new SemiNaiveRuleEngine(orarOntology);
		long startReasoning = System.currentTimeMillis();
		System.out.println(orarOntology.getNumberOfInputRoleAssertions());
		ruleEngine.materialize();
		long endReasoning = System.currentTimeMillis();
		long reasoningTime = (endReasoning - startReasoning) / 1000;
		System.out.println("Reasoning time: " + reasoningTime);
		System.out.println(orarOntology.getNumberOfRoleAssertions());
	}
}
