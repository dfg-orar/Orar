package orar.io.ontologyreader;

import java.io.File;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.modeling.ontology.OrarOntology;

public class StreamOntologyReaderTest {

	/**
	 * Check if parsing using Jena and using OWLAPI return the same results.
	 * 
	 * @throws OWLOntologyCreationException
	 */
	@Test
	public void shouldReturnTheSameConceptAssertions() throws OWLOntologyCreationException {
		/*
		 * Load ontology where TBox and ABox are in separated files.
		 */
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";

		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "src/test/resources/uobm-ox/u1/univ0.owl";

		/*
		 * Load ontology using a stream reader
		 */

		OrarOntology ontologyByStreamReader = loadOntologyUsingStreamReader(tboxFileName, aboxListFileName);

		/*
		 * Load ontology where TBox and ABox are mixed into one file.
		 */

		OrarOntology ontologyByOWLAPI = loadOntologyUsingOWLAPI(allInOneOntologyName);

		/*
		 * Comparing results
		 */

		Set<OWLClassAssertionAxiom> conceptAssertionByStreamReader = ontologyByStreamReader
				.getOWLAPIConceptAssertionsWithNormalizationSymbols();

		Set<OWLClassAssertionAxiom> conceptAssertionByOWLAPI = ontologyByOWLAPI
				.getOWLAPIConceptAssertionsWithNormalizationSymbols();

		System.out.println(
				"the number of Concept Assertions by the stream reader:" + conceptAssertionByStreamReader.size());
		System.out.println("the number of Concept Assertions by OWLAPI reader:" + conceptAssertionByOWLAPI.size());

		Assert.assertEquals(conceptAssertionByStreamReader, conceptAssertionByOWLAPI);

	}

	@Test
	public void shouldReturnTheSameRoleAssertions() throws OWLOntologyCreationException {
		/*
		 * Load ontology where TBox and ABox are in separated files.
		 */
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";

		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "src/test/resources/uobm-ox/u1/univ0.owl";

		/*
		 * Load ontology using a stream reader
		 */

		OrarOntology ontologyByStreamReader = loadOntologyUsingStreamReader(tboxFileName, aboxListFileName);

		/*
		 * Load ontology where TBox and ABox are mixed into one file.
		 */

		OrarOntology ontologyByOWLAPI = loadOntologyUsingOWLAPI(allInOneOntologyName);

		/*
		 * Comparing results
		 */
		Set<OWLObjectPropertyAssertionAxiom> roleAssertionsByStreamReader = ontologyByStreamReader
				.getOWLAPIRoleAssertionsWithNormalizationSymbols();
		System.out.println("Number of role assertons by the stream reader: " + roleAssertionsByStreamReader.size());

		Set<OWLObjectPropertyAssertionAxiom> roleAssertionByOWLAPI = ontologyByOWLAPI
				.getOWLAPIRoleAssertionsWithNormalizationSymbols();
		System.out.println("Number of role assertons by OWLAPI: " + roleAssertionByOWLAPI.size());
		Assert.assertEquals(roleAssertionsByStreamReader, roleAssertionByOWLAPI);

	}

	@Test
	public void shouldReturnTheSameTBoxAxioms() throws OWLOntologyCreationException {
		/*
		 * Load ontology where TBox and ABox are in separated files.
		 */
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";

		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "src/test/resources/uobm-ox/u1/univ0.owl";

		/*
		 * Load ontology using a stream reader
		 */

		OrarOntology ontologyByStreamReader = loadOntologyUsingStreamReader(tboxFileName, aboxListFileName);

		/*
		 * Load ontology where TBox and ABox are mixed into one file.
		 */

		OrarOntology ontologyByOWLAPI = loadOntologyUsingOWLAPI(allInOneOntologyName);

		/*
		 * Comparing results
		 */
		Assert.assertEquals(ontologyByStreamReader.getTBoxAxioms(), ontologyByOWLAPI.getTBoxAxioms());

	}

	@Test
	public void shouldReturnTheSameSignature() throws OWLOntologyCreationException {
		/*
		 * Load ontology where TBox and ABox are in separated files.
		 */
		String tboxFileName = "src/test/resources/uobm-ox/univ-bench-dl-ox.owl";

		String aboxListFileName = "src/test/resources/uobm-ox/u1/aboxU1.txt";

		String allInOneOntologyName = "src/test/resources/uobm-ox/u1/univ0.owl";

		/*
		 * Load ontology using a stream reader
		 */

		OrarOntology ontologyByStreamReader = loadOntologyUsingStreamReader(tboxFileName, aboxListFileName);

		/*
		 * Load ontology where TBox and ABox are mixed into one file.
		 */

		OrarOntology ontologyByOWLAPI = loadOntologyUsingOWLAPI(allInOneOntologyName);

		/*
		 * Comparing results
		 */

		Assert.assertEquals(ontologyByStreamReader.getIndividualsInSignature(),
				ontologyByOWLAPI.getIndividualsInSignature());

		Assert.assertEquals(ontologyByStreamReader.getConceptNamesInSignature(),
				ontologyByOWLAPI.getConceptNamesInSignature());

		Assert.assertEquals(ontologyByStreamReader.getRoleNamesInSignature(),
				ontologyByOWLAPI.getRoleNamesInSignature());

	}

	private OrarOntology loadOntologyUsingStreamReader(String tboxFileName, String aboxListFileName)
			throws OWLOntologyCreationException {
		long streamStart = System.currentTimeMillis();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owlOntology = manager.loadOntologyFromOntologyDocument(new File(tboxFileName));

		StreamOntologyReader2InternalModel streamReader = new StreamOntologyReader2InternalModel(owlOntology,
				aboxListFileName);

		OrarOntology ontologyByStreamReader = streamReader.getOntology();

		long streamEnd = System.currentTimeMillis();

		long streamTime = (streamEnd - streamStart) / 1000;
		System.out.println("Stream time:" + streamTime);
		return ontologyByStreamReader;
	}

	private OrarOntology loadOntologyUsingOWLAPI(String allInOneOntologyName) throws OWLOntologyCreationException {
		long owlapiStart = System.currentTimeMillis();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLOntology mixedOntology = manager.loadOntologyFromOntologyDocument(new File(allInOneOntologyName));
		OWLAPI2OrarConverter converter = new OWLAPI2OrarConverter(mixedOntology);
		OrarOntology ontologyByOWLAPI = converter.getInternalOntology();

		long owlapiEnd = System.currentTimeMillis();

		long owlapiTime = (owlapiEnd - owlapiStart) / 1000;

		System.out.println("owlapi time:" + owlapiTime);
		return ontologyByOWLAPI;
	}
}
