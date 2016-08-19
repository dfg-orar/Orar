package orar.io.ontologyreader;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import junit.framework.Assert;
import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.config.LogInfo;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.data.NormalizationDataFactory;
import orar.modeling.ontology.OrarOntology;
import orar.util.PrintingHelper;

public class DLLiteR_OntologyReaderTest {

	@Test
	public void shouldReturnTheSameSignature_uobmorigin() {
		String ontologyTbox = "src/test/resources/dlfragmentvalidator/tiny-uobmtbox.owl";
		String aboxList = "src/test/resources/uobm-origin/abox/aboxListOf2.txt";
		AbstractDataFactory.getInstance().clear();
		NormalizationDataFactory.getInstance().clear();
		MetaDataOfOntology.getInstance().clear();
		DataForTransferingEntailments.getInstance().clear();

		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.REASONING_TIME,
				LogInfo.COMPARED_RESULT_INFO);
		Configuration.getInstance().addDebugLevels(DebugLevel.DL_FRAGMENT_VALIDATING);
		System.out.println("Loading ontology for abstraction materializer....");
		OntologyReader ontoReader = new DLLiteH_OntologyReader();
		OrarOntology normalizedOrarOntology = ontoReader.getNormalizedOrarOntology(ontologyTbox, aboxList);
		int numberOfAssertions = normalizedOrarOntology.getNumberOfInputConceptAssertions()
				+ normalizedOrarOntology.getNumberOfInputRoleAssertions();
		System.out.println("DEBUG***Number of assertions in OrarOntology: " + numberOfAssertions);
		 HashSet<OWLClass> orarConceptNames = new HashSet<OWLClass>(normalizedOrarOntology.getConceptNamesInSignature());
		 HashSet<OWLObjectProperty> orarRoleNames = new HashSet<OWLObjectProperty>(normalizedOrarOntology.getRoleNamesInSignature());
		 System.out.println(
				"DEBUG*** Number of concept names:" + normalizedOrarOntology.getConceptNamesInSignature().size());
		System.out.println("Orar concept names:");
		PrintingHelper.printSet(orarConceptNames);
		System.out.println("Orar TBox:");
		PrintingHelper.printSet(normalizedOrarOntology.getTBoxAxioms());
		
		
		System.out.println("================================================================");
		System.out.println("Loading ontology for a DL Reasoner....");
		OWLOntology owlOntology = ontoReader.getOWLAPIOntology(ontologyTbox, aboxList);
		System.out.println("Number of assertions in OwlapiOntology: " + owlOntology.getABoxAxioms(true).size());
		System.out.println("DEBUG*** Number of concept names:"+owlOntology.getClassesInSignature(true).size());
		Set<OWLClass> owlapiOntoConceptNames = owlOntology.getClassesInSignature(true);
		 Set<OWLObjectProperty> owlapiOntoRoleNames = owlOntology.getObjectPropertiesInSignature(true);
		Assert.assertEquals(orarConceptNames, owlapiOntoConceptNames);
		Assert.assertEquals(orarRoleNames, owlapiOntoRoleNames);
		
	}
}
