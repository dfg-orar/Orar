package orar.ruleengine;

import static org.junit.Assert.*;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.modeling.ontology.MapbasedOrarOntology;
import orar.modeling.ontology.OrarOntology;
import orar.util.DefaultTestDataFactory;
import orar.util.PrintingHelper;

public class InverseRoleRuleExecutorTest {
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
	OWLDataFactory owlDataFactory= OWLManager.getOWLDataFactory();
	@Test
	public void test() {
		sharedMap.clear();

		/*
		 * create ontology
		 */
		OrarOntology orarOntology = new MapbasedOrarOntology();
		
		orarOntology.addRoleAssertion(a, R, b);
//		orarOntology.addRoleAssertion(c, S, d);
		
		OWLInverseObjectPropertiesAxiom inverse_R_S = owlDataFactory.getOWLInverseObjectPropertiesAxiom(R, S);
		orarOntology.getTBoxAxioms().add(inverse_R_S);
		
		RuleExecutor ruleExecutor= new InverseRoleRuleExecutor(orarOntology);
		OWLObjectPropertyAssertionAxiom roleAssertion = owlDataFactory.getOWLObjectPropertyAssertionAxiom(R, a, b);
		ruleExecutor.incrementalMaterialize(roleAssertion);
		// after computing deductive closure
		
		PrintingHelper.printSet(orarOntology.getOWLAPIRoleAssertionsWithNormalizationSymbols());
	}

}
