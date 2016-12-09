package orar.rolereasoning;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import junit.framework.Assert;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class FunctionalRoleIdentifierHermitTest {
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
	OWLObjectProperty invT = testData.getRole("InvT");
	OWLObjectProperty F = testData.getRole("F");
	OWLObjectProperty InvF = testData.getRole("InvF");
	/*
	 * Others
	 */
	OWLDataFactory dataFactory= OWLManager.getOWLDataFactory();
	
	
	
	@Test
	public void shouldComputeFuncandInvFuncRolesProperly() throws OWLOntologyCreationException {
		String file = "src/test/resources/functionalrole/test1.owl";
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(file));

		Set<OWLAxiom> tboxRboxAxioms = new HashSet<OWLAxiom>();
		tboxRboxAxioms.addAll(ontology.getTBoxAxioms(true));
		tboxRboxAxioms.addAll(ontology.getRBoxAxioms(true));

		RoleReasoner funcRoleIdentifier = new HermitRoleReasoner(tboxRboxAxioms);
		funcRoleIdentifier.doReasoning();
		PrintingHelper.printSet(funcRoleIdentifier.getFunctionalRoles());
		PrintingHelper.printSet(funcRoleIdentifier.getInverseFunctionalRoles());

		DefaultTestDataFactory defautDataFact = DefaultTestDataFactory.getInsatnce();
		// functional:f1
		OWLObjectProperty f1 = defautDataFact.getRole("F1");
		// inverse functional : f2
		OWLObjectProperty f2 = defautDataFact.getRole("F2");
		// functional + symetric : f3
		OWLObjectProperty f3 = defautDataFact.getRole("F3");

		OWLObjectProperty subF1 = defautDataFact.getRole("SubF1");
		OWLObjectProperty subF2 = defautDataFact.getRole("SubF2");
		OWLObjectProperty invSubF1 = defautDataFact.getRole("InvSubF1");
		OWLObjectProperty invSubF2 = defautDataFact.getRole("InvSubF2");

		Set<OWLObjectProperty> expectedFunctionalRoles = new HashSet<OWLObjectProperty>();
		expectedFunctionalRoles.add(f1);
		expectedFunctionalRoles.add(subF1);
		expectedFunctionalRoles.add(invSubF2);
		expectedFunctionalRoles.add(f3);

		Set<OWLObjectProperty> expectedInverseFunctionalRoles = new HashSet<OWLObjectProperty>();
		expectedInverseFunctionalRoles.add(f2);
		expectedInverseFunctionalRoles.add(subF2);
		expectedInverseFunctionalRoles.add(invSubF1);
		expectedInverseFunctionalRoles.add(f3);

		Assert.assertEquals(expectedFunctionalRoles, funcRoleIdentifier.getFunctionalRoles());

		Assert.assertEquals(expectedInverseFunctionalRoles, funcRoleIdentifier.getInverseFunctionalRoles());

	}

	/**
	 * nested subrole
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldComputeRoleHierarcyProperly() throws OWLOntologyCreationException {
		String file = "src/test/resources/functionalrole/test2.owl";
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(file));

		Set<OWLAxiom> tboxRboxAxioms = new HashSet<OWLAxiom>();
		tboxRboxAxioms.addAll(ontology.getTBoxAxioms(true));
		tboxRboxAxioms.addAll(ontology.getRBoxAxioms(true));

		RoleReasoner funcRoleIdentifier = new HermitRoleReasoner(tboxRboxAxioms);

		funcRoleIdentifier.doReasoning();
		PrintingHelper.printSet(funcRoleIdentifier.getFunctionalRoles());
		PrintingHelper.printSet(funcRoleIdentifier.getInverseFunctionalRoles());

		DefaultTestDataFactory defautDataFact = DefaultTestDataFactory.getInsatnce();
		// functional:f
		OWLObjectProperty f = defautDataFact.getRole("F");

		OWLObjectProperty subF = defautDataFact.getRole("SubF");

		OWLObjectProperty subsubF = defautDataFact.getRole("SubSubF");

		Set<OWLObjectProperty> expectedFunctionalRoles = new HashSet<OWLObjectProperty>();
		expectedFunctionalRoles.add(f);
		expectedFunctionalRoles.add(subF);
		expectedFunctionalRoles.add(subsubF);

		Assert.assertEquals(expectedFunctionalRoles, funcRoleIdentifier.getFunctionalRoles());

	}
	/**
	 * nested subrole
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldComputeTranRolesProperly(){
		
		
		Set<OWLAxiom> tboxRboxAxioms = new HashSet<OWLAxiom>();
		OWLTransitiveObjectPropertyAxiom transR = dataFactory.getOWLTransitiveObjectPropertyAxiom(R);
		OWLTransitiveObjectPropertyAxiom tranInvS = dataFactory.getOWLTransitiveObjectPropertyAxiom(dataFactory.getOWLObjectInverseOf(S));
		tboxRboxAxioms.add(transR);
		tboxRboxAxioms.add(tranInvS);

		RoleReasoner roleReasoner = new HermitRoleReasoner(tboxRboxAxioms);
		roleReasoner.doReasoning();
		
		
	
		
		Set<OWLObjectProperty> expectedFunctionalRoles = new HashSet<OWLObjectProperty>();
		expectedFunctionalRoles.add(R);
		expectedFunctionalRoles.add(S);
		

		Assert.assertEquals(expectedFunctionalRoles, roleReasoner.getTransitiveRoles());
	}

	
}
