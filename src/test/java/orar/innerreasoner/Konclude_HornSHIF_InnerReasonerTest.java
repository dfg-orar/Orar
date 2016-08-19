package orar.innerreasoner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import junit.framework.Assert;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.HermitDLReasoner;
import orar.innerreasoner.HornSHIF.Konclude_HornSHIF_InnerReasoner;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class Konclude_HornSHIF_InnerReasonerTest {
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
	OWLObjectProperty F = testData.getRole("F");

	/*
	 * Others
	 */
	OWLOntologyManager ontManager = OWLManager.createOWLOntologyManager();
	OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
	MetaDataOfOntology metaDataOfOntology = MetaDataOfOntology.getInstance();
	AbstractDataFactory abstractDataFactory = AbstractDataFactory.getInstance();
	DataForTransferingEntailments dataForTransferingEntailments = DataForTransferingEntailments.getInstance();

	/**
	 * Should get assertion from R^2_t: N(a)-->T(a,a)
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldGetLoopAssertion() throws OWLOntologyCreationException {
		/**
		 * create ontology: <br>
		 * A SubClsasOf T.exists.Top <br>
		 * T SubRoleOf InverserOf(T) <br>
		 * A(a)<br>
		 * Tran(T)
		 */
		OWLOntology ontology = ontManager.createOntology();
		OWLClass top = dataFactory.getOWLThing();
		OWLObjectSomeValuesFrom T_Exists_Top = dataFactory.getOWLObjectSomeValuesFrom(T, top);
		OWLSubClassOfAxiom A_SubclassOf_TsomeTop = dataFactory.getOWLSubClassOfAxiom(A, T_Exists_Top);
		ontManager.addAxiom(ontology, A_SubclassOf_TsomeTop);
		OWLSubObjectPropertyOfAxiom T_SubroleOf_InvT = dataFactory.getOWLSubObjectPropertyOfAxiom(T,
				dataFactory.getOWLObjectInverseOf(T));
		ontManager.addAxiom(ontology, T_SubroleOf_InvT);
		OWLTransitiveObjectPropertyAxiom trans_T = dataFactory.getOWLTransitiveObjectPropertyAxiom(T);
		ontManager.addAxiom(ontology, trans_T);
		OWLClassAssertionAxiom A_a = dataFactory.getOWLClassAssertionAxiom(A, a);
		ontManager.addAxiom(ontology, A_a);

		/*
		 * prepare some information used in optimization, e.g. trans role,
		 * marked abstract individuals,...
		 */
		this.metaDataOfOntology.getTransitiveRoles().clear();
		this.abstractDataFactory.getXAbstractIndividuals().clear();
		this.abstractDataFactory.getUAbstractIndividuals().clear();
		this.metaDataOfOntology.getTransitiveRoles().add(T);
		this.abstractDataFactory.getXAbstractIndividuals().add(a);
		this.abstractDataFactory.getUAbstractIndividuals().add(a);
		/*
		 * Test with the reasoner
		 */
		DLReasoner hermitDLReasoner = new HermitDLReasoner(ontology);
		hermitDLReasoner.computeEntailments();
		Set<OWLObjectPropertyAssertionAxiom> expectedResult = hermitDLReasoner.getEntailedRoleAssertions();
		PrintingHelper.printString("===Result by a DL reasoner:===");
		PrintingHelper.printSet(expectedResult);

		InnerReasoner hermitInnerReasoner = computeEntailmentsByInnerReasoner(ontology);

		AbstractRoleAssertionBox entailedRoleAssertionBox = hermitInnerReasoner.getEntailedRoleAssertions();
		Set<OWLObjectPropertyAssertionAxiom> actualResult = entailedRoleAssertionBox.getSetOfRoleAssertions();
		PrintingHelper.printString("+++Result by an inner reasoner:+++");
		PrintingHelper.printSet(actualResult);

		Assert.assertEquals(expectedResult, actualResult);

	}

	/**
	 * Should get assertion from: a equivalent b
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldGetSameAsAssertions() throws OWLOntologyCreationException {
		/**
		 * create ontology: <br>
		 * axiom1: No = {o} <br>
		 * axiom2: A SubClsasOf exists.inv(F).No <br>
		 * axiom3: A(a) <br>
		 * axiom4: A(b)<br>
		 * axiom5: Func(F)
		 */
		OWLOntology ontology = ontManager.createOntology();

		/*
		 * axiom1: No = {o}
		 */
		OWLObjectOneOf nom_o = dataFactory.getOWLObjectOneOf(o);
		OWLEquivalentClassesAxiom axiom1 = dataFactory.getOWLEquivalentClassesAxiom(No, nom_o);
		ontManager.addAxiom(ontology, axiom1);

		/*
		 * axiom2: A SubClsasOf inv(F).exists.No <br>
		 */
		OWLObjectSomeValuesFrom superConcept2 = dataFactory
				.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectInverseOf(F), No);
		OWLSubClassOfAxiom axiom2 = dataFactory.getOWLSubClassOfAxiom(A, superConcept2);
		ontManager.addAxiom(ontology, axiom2);
		/*
		 * axiom3: A(a) <br> axiom4: A(b)<br>
		 */
		OWLClassAssertionAxiom A_a = dataFactory.getOWLClassAssertionAxiom(A, a);
		ontManager.addAxiom(ontology, A_a);
		OWLClassAssertionAxiom A_b = dataFactory.getOWLClassAssertionAxiom(A, b);
		ontManager.addAxiom(ontology, A_b);

		/*
		 * axiom5: Func(F)
		 * 
		 */
		OWLFunctionalObjectPropertyAxiom funcF = dataFactory.getOWLFunctionalObjectPropertyAxiom(F);
		ontManager.addAxiom(ontology, funcF);

		/*
		 * prepare some information used in optimization, e.g. trans role,
		 * marked abstract individuals,...
		 */
		// clear existing data
		this.metaDataOfOntology.getFunctionalRoles().clear();
		this.metaDataOfOntology.getNominalConcepts().clear();
		this.abstractDataFactory.getXAbstractIndividuals().clear();
		this.abstractDataFactory.getUAbstractIndividuals().clear();

		// add necessary data
		this.metaDataOfOntology.getFunctionalRoles().add(F);
		this.metaDataOfOntology.getNominalConcepts().add(No);
		this.abstractDataFactory.getXAbstractIndividuals().add(a);
		this.abstractDataFactory.getXAbstractIndividuals().add(b);
		this.abstractDataFactory.getXAbstractIndividuals().add(o);

		this.abstractDataFactory.getUAbstractIndividuals().add(a);
		this.abstractDataFactory.getUAbstractIndividuals().add(b);
		this.abstractDataFactory.getUAbstractIndividuals().add(o);

		/*
		 * Test with the reasoner
		 */
		Map<OWLNamedIndividual, Set<OWLNamedIndividual>> expectedResult = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();
		expectedResult.put(a, testData.getSetOfIndividuals(b));

		InnerReasoner hermitInnerReasoner = computeEntailmentsByInnerReasoner(ontology);
		Map<OWLNamedIndividual, Set<OWLNamedIndividual>> actualResult = hermitInnerReasoner.getSameAsMap();

		PrintingHelper.printString("+++Result by an inner reasoner:+++");
		PrintingHelper.printMap(actualResult);

		Assert.assertEquals(expectedResult, actualResult);

		DLReasoner hermitDLReasoner = new HermitDLReasoner(ontology);
		hermitDLReasoner.computeEntailments();
		Map<OWLNamedIndividual, Set<OWLNamedIndividual>> dlReasonerResult = hermitDLReasoner
				.getEntailedSameasAssertions();
		PrintingHelper.printString("===Some additional info---Result by a DL reasoner:===");
		PrintingHelper.printMap(dlReasonerResult);
	}

	/**
	 * Should get assertion by the rule R^3_t: N(a),M(b) --> T(a,b), and the
	 * 
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldGetRoleAssertionForRulet3AndRuleexist() throws OWLOntologyCreationException {
		/**
		 * create ontology: <br>
		 * axiom1: No = {o} <br>
		 * axiom2: A SubClsasOf exists.T.No <br>
		 * axiom3: B SubClsasOf exists.inv(T).No <br>
		 * axiom4: A(a) <br>
		 * axiom5: B(b)<br>
		 * axiom6: Tran(T)
		 */
		OWLOntology ontology = ontManager.createOntology();

		/*
		 * axiom1: No = {o}
		 */
		OWLObjectOneOf nom_o = dataFactory.getOWLObjectOneOf(o);
		OWLEquivalentClassesAxiom axiom1 = dataFactory.getOWLEquivalentClassesAxiom(No, nom_o);
		ontManager.addAxiom(ontology, axiom1);

		/*
		 * axiom2: A SubClsasOf exists.T.No <br>
		 */
		OWLObjectSomeValuesFrom superConcept2 = dataFactory.getOWLObjectSomeValuesFrom(T, No);
		OWLSubClassOfAxiom axiom2 = dataFactory.getOWLSubClassOfAxiom(A, superConcept2);
		ontManager.addAxiom(ontology, axiom2);
		// axiom3: B SubClsasOf exists.inv(T).No <br>
		OWLObjectSomeValuesFrom superConcept3 = dataFactory
				.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectInverseOf(T), No);
		OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(B, superConcept3);
		ontManager.addAxiom(ontology, axiom3);
		/*
		 * axiom4: A(a) <br> axiom5: B(b)<br>
		 */
		OWLClassAssertionAxiom A_a = dataFactory.getOWLClassAssertionAxiom(A, a);
		ontManager.addAxiom(ontology, A_a);
		OWLClassAssertionAxiom B_b = dataFactory.getOWLClassAssertionAxiom(B, b);
		ontManager.addAxiom(ontology, B_b);

		/*
		 * axiom5: Tran(T)
		 * 
		 */
		OWLTransitiveObjectPropertyAxiom tranT = dataFactory.getOWLTransitiveObjectPropertyAxiom(T);
		ontManager.addAxiom(ontology, tranT);

		/*
		 * prepare some information used in optimization, e.g. trans role,
		 * marked abstract individuals,...
		 */
		// clear old data
		this.metaDataOfOntology.getTransitiveRoles().clear();
		this.metaDataOfOntology.getNominalConcepts().clear();
		this.abstractDataFactory.getXAbstractIndividuals().clear();
		this.abstractDataFactory.getUAbstractIndividuals().clear();

		// add necessary data
		this.metaDataOfOntology.getTransitiveRoles().add(T);
		this.metaDataOfOntology.getNominalConcepts().add(No);
		this.abstractDataFactory.getXAbstractIndividuals().add(a);
		this.abstractDataFactory.getXAbstractIndividuals().add(b);
		this.abstractDataFactory.getXAbstractIndividuals().add(o);

		this.abstractDataFactory.getUAbstractIndividuals().add(a);
		this.abstractDataFactory.getUAbstractIndividuals().add(b);
		this.abstractDataFactory.getUAbstractIndividuals().add(o);

		/*
		 * Test with the reasoner
		 */
		Set<OWLObjectPropertyAssertionAxiom> expectedResult = new HashSet<OWLObjectPropertyAssertionAxiom>();
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(T, a, o));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(T, a, b));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(T, o, b));

		InnerReasoner hermitInnerReasoner = computeEntailmentsByInnerReasoner(ontology);
		Set<OWLObjectPropertyAssertionAxiom> actualResult = hermitInnerReasoner.getEntailedRoleAssertions()
				.getSetOfRoleAssertions();

		PrintingHelper.printString("+++Result by an inner reasoner:+++");
		PrintingHelper.printSet(actualResult);

		Assert.assertEquals(expectedResult, actualResult);

		DLReasoner hermitDLReasoner = new HermitDLReasoner(ontology);
		hermitDLReasoner.computeEntailments();
		Set<OWLObjectPropertyAssertionAxiom> dlReasonerResult = hermitDLReasoner.getEntailedRoleAssertions();
		PrintingHelper.printString("===Some additional info---Result by a DL reasoner:===");
		PrintingHelper.printSet(dlReasonerResult);
	}

	/**
	 * Should get assertion by the rule R^\exists: N(a), M(b) --> R(a,b) if
	 * |M|=1 and N subclassof \exist R.M
	 * 
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldGetRoleAssertionForRuleexist() throws OWLOntologyCreationException {
		/**
		 * create ontology: <br>
		 * axiom1: No = {o} <br>
		 * axiom2: A SubClsasOf exists.F.No <br>
		 * axiom3: B SubClsasOf exists.F.No <br>
		 * axiom4: A(a) <br>
		 * axiom5: B(b)<br>
		 * axiom6: invFunc(F) <br>
		 * axiom7: A SubClassOf exists. R. B
		 */
		OWLOntology ontology = ontManager.createOntology();

		/*
		 * axiom1
		 */
		OWLObjectOneOf nom_o = dataFactory.getOWLObjectOneOf(o);
		OWLEquivalentClassesAxiom axiom1 = dataFactory.getOWLEquivalentClassesAxiom(No, nom_o);
		ontManager.addAxiom(ontology, axiom1);

		/*
		 * axiom2,3: A SubClsasOf exists.F.No <br> axiom3: B SubClsasOf
		 * exists.F.No <br>
		 */
		OWLObjectSomeValuesFrom superConcept2 = dataFactory.getOWLObjectSomeValuesFrom(F, No);
		OWLSubClassOfAxiom axiom2 = dataFactory.getOWLSubClassOfAxiom(A, superConcept2);
		ontManager.addAxiom(ontology, axiom2);
		// axiom3: B SubClsasOf exists.inv(T).No <br>
		OWLObjectSomeValuesFrom superConcept3 = dataFactory.getOWLObjectSomeValuesFrom(F, No);
		OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(B, superConcept3);
		ontManager.addAxiom(ontology, axiom3);
		/*
		 * axiom4: A(a) <br> axiom5: B(b)<br>
		 */
		OWLClassAssertionAxiom A_a = dataFactory.getOWLClassAssertionAxiom(A, a);
		ontManager.addAxiom(ontology, A_a);
		OWLClassAssertionAxiom B_b = dataFactory.getOWLClassAssertionAxiom(B, b);
		ontManager.addAxiom(ontology, B_b);

		/*
		 * axiom6: invFunc(F)
		 * 
		 */
		OWLInverseFunctionalObjectPropertyAxiom invFunc_F = dataFactory.getOWLInverseFunctionalObjectPropertyAxiom(F);
		ontManager.addAxiom(ontology, invFunc_F);

		/*
		 * axiom 7:
		 * 
		 */
		OWLObjectSomeValuesFrom existsF_B = dataFactory.getOWLObjectSomeValuesFrom(F, B);
		OWLSubClassOfAxiom axiom6 = dataFactory.getOWLSubClassOfAxiom(A, existsF_B);
		ontManager.addAxiom(ontology, axiom6);

		/*
		 * prepare some information used in optimization, e.g. trans role,
		 * marked abstract individuals,...
		 */
		// clear old data
		this.metaDataOfOntology.getTransitiveRoles().clear();
		this.metaDataOfOntology.getNominalConcepts().clear();
		this.abstractDataFactory.getXAbstractIndividuals().clear();
		this.abstractDataFactory.getUAbstractIndividuals().clear();

		// add necessary data
		this.metaDataOfOntology.getInverseFunctionalRoles().add(F);
		this.metaDataOfOntology.getNominalConcepts().add(No);
		this.abstractDataFactory.getXAbstractIndividuals().add(a);
		this.abstractDataFactory.getXAbstractIndividuals().add(b);
		this.abstractDataFactory.getXAbstractIndividuals().add(o);

		this.abstractDataFactory.getUAbstractIndividuals().add(a);
		this.abstractDataFactory.getUAbstractIndividuals().add(b);
		this.abstractDataFactory.getUAbstractIndividuals().add(o);

		/*
		 * Test with the reasoner
		 */
		Set<OWLObjectPropertyAssertionAxiom> expectedResult = new HashSet<OWLObjectPropertyAssertionAxiom>();
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, a, a));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, a, b));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, a, o));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, b, b));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, b, o));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, b, a));

		InnerReasoner hermitInnerReasoner = computeEntailmentsByInnerReasoner(ontology);
		Set<OWLObjectPropertyAssertionAxiom> actualResult = hermitInnerReasoner.getEntailedRoleAssertions()
				.getSetOfRoleAssertions();

		PrintingHelper.printString("+++Result by an inner reasoner:+++");
		PrintingHelper.printSet(actualResult);

		Assert.assertEquals(expectedResult, actualResult);

		DLReasoner hermitDLReasoner = new HermitDLReasoner(ontology);
		hermitDLReasoner.computeEntailments();
		Set<OWLObjectPropertyAssertionAxiom> dlReasonerResult = hermitDLReasoner.getEntailedRoleAssertions();
		PrintingHelper.printString("===Some additional info---Result by a DL reasoner:===");
		PrintingHelper.printSet(dlReasonerResult);
	}

	/**
	 * Should get assertion by the rule R^2_<: M(a), F(a,b) --> H(a,b), N(b).
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldGetRoleAssertionForRuleLEQ2() throws OWLOntologyCreationException {
		/**
		 * create ontology: <br>
		 * axiom1: A(a) <br>
		 * axiom2: F(a,b) <br>
		 * axiom3: A SubClsasOf exists.R.B <br>
		 * axiom4: R SubClassOf F <br>
		 * axiom5: Func(F)
		 */
		OWLOntology ontology = ontManager.createOntology();
		/*
		 * axiom1: A(a) <br> axiom2: B(b)<br>
		 */
		OWLClassAssertionAxiom A_a = dataFactory.getOWLClassAssertionAxiom(A, a);
		ontManager.addAxiom(ontology, A_a);
		OWLObjectPropertyAssertionAxiom F_ab = dataFactory.getOWLObjectPropertyAssertionAxiom(F, a, b);
		ontManager.addAxiom(ontology, F_ab);

		/*
		 * axiom3: A SubClsasOf exists.R.B <br>
		 */
		OWLObjectSomeValuesFrom superConcept2 = dataFactory.getOWLObjectSomeValuesFrom(R, B);
		OWLSubClassOfAxiom axiom2 = dataFactory.getOWLSubClassOfAxiom(A, superConcept2);
		ontManager.addAxiom(ontology, axiom2);

		// axiom3: R SubRoleOf F

		OWLSubObjectPropertyOfAxiom axiom3 = dataFactory.getOWLSubObjectPropertyOfAxiom(R, F);
		ontManager.addAxiom(ontology, axiom3);

		/*
		 * axiom5: Func(F)
		 * 
		 */
		OWLFunctionalObjectPropertyAxiom funcF = dataFactory.getOWLFunctionalObjectPropertyAxiom(F);
		ontManager.addAxiom(ontology, funcF);

		/*
		 * prepare some information used in optimization, e.g. trans role,
		 * marked abstract individuals,...
		 */
		// clear old data
		this.metaDataOfOntology.getFunctionalRoles().clear();
		this.metaDataOfOntology.getFunctionalRoles().clear();
		this.abstractDataFactory.getXAbstractIndividuals().clear();
		this.abstractDataFactory.getYAbstractIndividuals().clear();
		this.dataForTransferingEntailments.getxAbstractHavingFunctionalRole().clear();
		this.dataForTransferingEntailments.getzAbstractHavingInverseFunctionalRole().clear();
		// add neccessary data
		this.metaDataOfOntology.getFunctionalRoles().add(F);
		this.metaDataOfOntology.getFunctionalRoles().add(R);
		this.abstractDataFactory.getXAbstractIndividuals().add(a);
		this.abstractDataFactory.getXAbstractIndividuals().add(b);
		this.abstractDataFactory.getYAbstractIndividuals().add(a);
		this.abstractDataFactory.getYAbstractIndividuals().add(b);

		this.dataForTransferingEntailments.getxAbstractHavingFunctionalRole().add(a);
		this.dataForTransferingEntailments.getzAbstractHavingInverseFunctionalRole().add(b);

		/*
		 * Test with the reasoner
		 */
		Set<OWLObjectPropertyAssertionAxiom> expectedResult = new HashSet<OWLObjectPropertyAssertionAxiom>();
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(F, a, b));
		expectedResult.add(dataFactory.getOWLObjectPropertyAssertionAxiom(R, a, b));

		InnerReasoner hermitInnerReasoner = computeEntailmentsByInnerReasoner(ontology);
		Set<OWLObjectPropertyAssertionAxiom> actualResult = hermitInnerReasoner.getEntailedRoleAssertions()
				.getSetOfRoleAssertions();

		PrintingHelper.printString("+++Result by an inner reasoner:+++");
		PrintingHelper.printSet(actualResult);

		Assert.assertEquals(expectedResult, actualResult);

		DLReasoner hermitDLReasoner = new HermitDLReasoner(ontology);
		hermitDLReasoner.computeEntailments();
		Set<OWLObjectPropertyAssertionAxiom> dlReasonerResult = hermitDLReasoner.getEntailedRoleAssertions();
		PrintingHelper.printString("===Some additional info---Result by a DL reasoner:===");
		PrintingHelper.printSet(dlReasonerResult);
	}

	private InnerReasoner computeEntailmentsByInnerReasoner(OWLOntology ontology) {
		/*
		 * Test with the reasoner
		 */
		InnerReasoner innerReasoner = new Konclude_HornSHIF_InnerReasoner(ontology);
		innerReasoner.computeEntailments();
		return innerReasoner;
	}
}
