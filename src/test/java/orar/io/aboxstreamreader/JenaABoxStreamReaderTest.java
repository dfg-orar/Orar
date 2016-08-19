package orar.io.aboxstreamreader;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import junit.framework.Assert;
import orar.modeling.ontology.MapbasedOrarOntology;
import orar.modeling.ontology.OrarOntology;

/**
 * Check if parsing using Jena and using OWLAPI return the same results
 * (assertions). Note that there is a type by UOBM Oxford generator, i.e Article
 * is used in the TBox, whereas Artical is used in the ABox. We cannot change
 * the generator, thus, we change Article in TBox to Artical
 * 
 * 
 */
public class JenaABoxStreamReaderTest {

	@Test
	public void readByJenaAndByOWLAPI_ShouldReturnTheSameConceptAssertions() throws OWLOntologyCreationException {
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";
		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "src/test/resources/uobm-ox/u1/univ0.owl";

		/*
		 * Get ontologies
		 */
		OrarOntology orarOntology = getOrarOntologyWithJena(tboxFileName, aboxListFileName);
		OWLOntology tbox = getOWLAPIOntology(tboxFileName);
		OWLOntology owlOntology = getOWLAPIOntology(allInOneOntologyName);
		/*
		 * Compare assertions in two ontologies
		 */
		Set<OWLClassAssertionAxiom> conceptAssertoinOfOrarOntology = orarOntology
				.getOWLAPIConceptAssertionsWithNormalizationSymbols();
		/*
		 * Note that sometimes, TBox files does contain assertions. We must take
		 * that into account.
		 */
		HashSet<OWLClassAssertionAxiom> conceptAssertoinOfOrarOntologyPlusAssertionInTBox = new HashSet<OWLClassAssertionAxiom>(
				conceptAssertoinOfOrarOntology);
		conceptAssertoinOfOrarOntologyPlusAssertionInTBox.addAll(tbox.getAxioms(AxiomType.CLASS_ASSERTION, true));

		System.out.println(conceptAssertoinOfOrarOntologyPlusAssertionInTBox.size());
		Set<OWLClassAssertionAxiom> conceptAssertionOfOWLAPIOntology = owlOntology.getAxioms(AxiomType.CLASS_ASSERTION,
				true);
		System.out.println(conceptAssertionOfOWLAPIOntology.size());

		Assert.assertEquals(conceptAssertoinOfOrarOntologyPlusAssertionInTBox, conceptAssertionOfOWLAPIOntology);

	}

	@Test
	public void readByJenaAndByOWLAPI_ShouldReturnTheSameRoleAssertions() throws OWLOntologyCreationException {
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";
		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "src/test/resources/uobm-ox/u1/univ0.owl";

		/*
		 * Get ontologies
		 */
		OrarOntology orarOntology = getOrarOntologyWithJena(tboxFileName, aboxListFileName);
		OWLOntology tbox = getOWLAPIOntology(tboxFileName);
		OWLOntology owlOntology = getOWLAPIOntology(allInOneOntologyName);
		/*
		 * Compare assertions in two ontologies
		 */
		Set<OWLObjectPropertyAssertionAxiom> roleAssertionOfOrarOntology = orarOntology
				.getOWLAPIRoleAssertionsWithNormalizationSymbols();
		/*
		 * Note that sometimes, TBox files does contain assertions. We must take
		 * that into account.
		 */
		Set<OWLObjectPropertyAssertionAxiom> roleAssertionOfOrarOntologyPlusOnesInTBox = new HashSet<OWLObjectPropertyAssertionAxiom>(
				roleAssertionOfOrarOntology);
		roleAssertionOfOrarOntologyPlusOnesInTBox.addAll(tbox.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, true));

		System.out.println(roleAssertionOfOrarOntologyPlusOnesInTBox.size());
		Set<OWLObjectPropertyAssertionAxiom> roleAssertionOfOWLAPIOntology = owlOntology
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, true);
		System.out.println(roleAssertionOfOWLAPIOntology.size());

		Assert.assertEquals(roleAssertionOfOrarOntologyPlusOnesInTBox, roleAssertionOfOWLAPIOntology);

	}

	private OrarOntology getOrarOntologyWithJena(String tboxFile, String aboxListFile)
			throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology tbox = manager.loadOntologyFromOntologyDocument(new File(tboxFile));
		Set<OWLObjectProperty> definedRoles = tbox.getObjectPropertiesInSignature(true);
		Set<OWLClass> defiedConcepts = tbox.getClassesInSignature(true);

		OrarOntology orarOntology = new MapbasedOrarOntology();
		ABoxStreamReader aboxStreamReader = new JenaMultipleABoxesStreamReader(definedRoles, defiedConcepts, aboxListFile,
				orarOntology);
		aboxStreamReader.readABoxes();
		return orarOntology;
	}

	private OWLOntology getOWLAPIOntology(String owlapiOntologyFile) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owlOntology = manager.loadOntologyFromOntologyDocument(new File(owlapiOntologyFile));
		return owlOntology;
	}

}
