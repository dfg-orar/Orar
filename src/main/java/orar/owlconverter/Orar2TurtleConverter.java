package orar.owlconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

import orar.indexing.IndividualIndexer;
import orar.io.ontologyreader.OntologyReader;
import orar.modeling.conceptassertion2.ConceptAssertionBox2;
import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.RoleAssertionBox2;
import orar.modeling.sameas2.SameAsBox2;

public abstract class Orar2TurtleConverter {
	private final Logger logger = Logger.getLogger(Orar2TurtleConverter.class);
	private final IndividualIndexer indexer;
	private Model model;

	public Orar2TurtleConverter() {
		indexer = IndividualIndexer.getInstance();

	}

	/**
	 * Read an ontology (tboxFileName + aboxListFile); validate that TBox and
	 * return its HornSHOIF fragment; and convert the ABox assertions into
	 * Turtle syntax.
	 * 
	 * @param tboxFileName
	 *            inputTBox
	 * @param aboxListFile
	 *            list of input aboxes file names
	 * @param validatedTBoxFileName
	 *            output TBox in HornSHOIF and in RDFXML format
	 * @param aboxInTurtle
	 *            output ABox in Turtle
	 */
	public void convertCombinedOntologyToSaparatedFiles(String tboxAndABoxFile, String validatedTBoxFileName,
			String aboxInTurtle) {
		indexer.clear();
		this.model = ModelFactory.createDefaultModel();

		OntologyReader ontologyReader = getOntologyReader();
		OrarOntology2 orarOntology = ontologyReader.getNOTNormalizedOrarOntology(tboxAndABoxFile);

		logger.info("obtaining the TBox in desired fragment...");
		saveTBoxInRDFXML(orarOntology, validatedTBoxFileName);
		logger.info("done! saved a validated TBox to: " + validatedTBoxFileName);

		logger.info("converting assertions into Turtle syntax...");
		saveABoxInTurtle(orarOntology, aboxInTurtle);
		logger.info("done! saved all assertions to: " + aboxInTurtle);
	}

	/**
	 * Read an ontology (tboxFileName + aboxListFile); validate that TBox and
	 * return its HornSHOIF fragment; and convert the ABox assertions into
	 * Turtle syntax.
	 * 
	 * @param tboxFileName
	 *            inputTBox
	 * @param aboxListFile
	 *            list of input aboxes file names
	 * @param validatedTBoxFileName
	 *            output TBox in HornSHOIF and in RDFXML format
	 * @param aboxInTurtle
	 *            output ABox in Turtle
	 */
	public void convert(String tboxFileName, String aboxListFile, String validatedTBoxFileName, String aboxInTurtle) {
		indexer.clear();
		this.model = ModelFactory.createDefaultModel();

		OntologyReader ontologyReader = getOntologyReader();
		OrarOntology2 orarOntology = ontologyReader.getNOTNormalizedOrarOntology(tboxFileName, aboxListFile);

		logger.info("obtaining the TBox in desired fragment...");
		saveTBoxInRDFXML(orarOntology, validatedTBoxFileName);
		logger.info("done! saved a validated TBox to: " + validatedTBoxFileName);

		logger.info("converting assertions into Turtle syntax...");
		saveABoxInTurtle(orarOntology, aboxInTurtle);
		logger.info("done! saved all assertions to: " + aboxInTurtle);
	}

	/**
	 * Read an ontology (tboxFileName + aboxListFile); validate that TBox and
	 * return its HornSHOIF fragment; and convert the ABox assertions into
	 * Turtle syntax.
	 * 
	 * @param tboxFileName
	 *            inputTBox
	 * @param aboxListFile
	 *            list of input aboxes file names
	 * 
	 * @param aboxInTurtle
	 *            output ABox in Turtle
	 */
	public void convert(String tboxFileName, String aboxListFile, String aboxInTurtle) {
		indexer.clear();
		this.model = ModelFactory.createDefaultModel();

		OntologyReader ontologyReader = getOntologyReader();
		OrarOntology2 orarOntology = ontologyReader.getNOTNormalizedOrarOntology(tboxFileName, aboxListFile);

		logger.info("converting assertions into Turtle syntax...");
		saveABoxInTurtle(orarOntology, aboxInTurtle);
		logger.info("done! saved all assertions to: " + aboxInTurtle);
	}

	private void saveABoxInTurtle(OrarOntology2 orarOntology, String aboxInTurtle) {
		putConceptAssertionsIntoJenaModel(orarOntology);
		putRoleAssertionsIntoJenaModel(orarOntology);
		putSameasAssertionIntoJenaModel(orarOntology);

		File file = new File(aboxInTurtle);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			model.write(outputStream, "TURTLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void putSameasAssertionIntoJenaModel(OrarOntology2 orarOntology) {
		SameAsBox2 sameasBox = orarOntology.getSameasBox();
		Map<Integer, Set<Integer>> sameasMap = sameasBox.getSameasMap();
		Iterator<Entry<Integer, Set<Integer>>> iterator = sameasMap.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Integer, Set<Integer>> entry = iterator.next();

			Integer indexedIndividual = entry.getKey();
			String stringOfEachIndexedIndividual = indexer.getIndividualString(indexedIndividual);
			Resource jenaEachIndividual = model.createResource(stringOfEachIndexedIndividual);

			Set<Integer> sameIndexedIndividuals = entry.getValue();
			for (Integer eachSameIndexedIndividual : sameIndexedIndividuals) {
				String stringOfSameIndividual = indexer.getIndividualString(eachSameIndexedIndividual);
				Resource jenaSameIndividual = model.createResource(stringOfSameIndividual);

				Statement sameasStatement = ResourceFactory.createStatement(jenaEachIndividual, OWL.sameAs,
						jenaSameIndividual);
				model.add(sameasStatement);
			}

		}

	}

	private void putRoleAssertionsIntoJenaModel(OrarOntology2 orarOntology) {
		/*
		 * get role assertions into the Jena model
		 */

		RoleAssertionBox2 roleAssertionBox = orarOntology.getRoleAssertionBox();

		Set<Integer> allIndexedIndividuals = roleAssertionBox.getAllIndividuals();
		for (Integer eachIndexedIndividual : allIndexedIndividuals) {
			String stringOfEachIndexedIndividual = indexer.getIndividualString(eachIndexedIndividual);
			Resource jenaEachIndividual = model.createResource(stringOfEachIndexedIndividual);

			/*
			 * get successor assertions for each Individual
			 */
			Map<OWLObjectProperty, Set<Integer>> succesorMap = roleAssertionBox
					.getSuccesorRoleAssertionsAsMap(eachIndexedIndividual);

			Iterator<Entry<OWLObjectProperty, Set<Integer>>> iterator = succesorMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<OWLObjectProperty, Set<Integer>> entry = iterator.next();
				OWLObjectProperty owlRole = entry.getKey();
				Property jenaPredicate = model.createProperty(owlRole.getIRI().toString());

				Set<Integer> indexedObjectes = entry.getValue();
				for (Integer eachIndexedObject : indexedObjectes) {
					String stringObject = indexer.getIndividualString(eachIndexedObject);
					Resource jenaObjectIndividual = model.createResource(stringObject);
					Statement succRoleAssertionStatement = ResourceFactory.createStatement(jenaEachIndividual,
							jenaPredicate, jenaObjectIndividual);
					this.model.add(succRoleAssertionStatement);
					// jenaSubjectIndividual.addProperty(jenaPredicate,
					// jenaObjectIndividual);
				}

			}
			/*
			 * get predecessor assertions for each individuals
			 */

			Map<OWLObjectProperty, Set<Integer>> preRoleAssertionMap = roleAssertionBox
					.getPredecessorRoleAssertionsAsMap(eachIndexedIndividual);

			Iterator<Entry<OWLObjectProperty, Set<Integer>>> iteratorOfPreMap = preRoleAssertionMap.entrySet()
					.iterator();
			while (iteratorOfPreMap.hasNext()) {
				Entry<OWLObjectProperty, Set<Integer>> entry = iteratorOfPreMap.next();
				OWLObjectProperty owlRole = entry.getKey();
				Property jenaPredicate = model.createProperty(owlRole.getIRI().toString());

				Set<Integer> indexedPredecessors = entry.getValue();
				for (Integer eachIndexedPredecessor : indexedPredecessors) {
					String stringPredecessor = indexer.getIndividualString(eachIndexedPredecessor);
					Resource jenaPredecessor = model.createResource(stringPredecessor);
					Statement preRoleAssertionStatement = ResourceFactory.createStatement(jenaPredecessor,
							jenaPredicate, jenaEachIndividual);
					this.model.add(preRoleAssertionStatement);
					// jenaSubjectIndividual.addProperty(jenaPredicate,
					// jenaObjectIndividual);
				}
			}
		}

	}

	private void putConceptAssertionsIntoJenaModel(OrarOntology2 orarOntology) {
		/*
		 * get concept assertions into the Jena model
		 */
		ConceptAssertionBox2 conceptAssertionBox = orarOntology.getConceptAssertionBox();

		Set<Integer> allIndexedIndividuals = conceptAssertionBox.getAllIndividuals();
		for (Integer eachIndex : allIndexedIndividuals) {
			Set<OWLClass> concepts = conceptAssertionBox.getAssertedConcepts(eachIndex);
			String stringOfEachIndex = indexer.getIndividualString(eachIndex);

			Resource jenaIndividual = model.createResource(stringOfEachIndex);

			for (OWLClass eachConcep : concepts) {
				Resource jenaConceptName = model.createResource(eachConcep.getIRI().toString());
				Statement conceptAssertionStatement = ResourceFactory.createStatement(jenaIndividual, RDF.type,
						jenaConceptName);
				this.model.add(conceptAssertionStatement);
				// jenaIndividual.addProperty(RDF.type, jenaConceptName);
			}
		}
	}

	private void saveTBoxInRDFXML(OrarOntology2 orarOntology, String validatedTBoxFileName) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology;
		try {
			ontology = manager.createOntology();
			manager.addAxioms(ontology, orarOntology.getTBoxAxioms());

			// OWLXMLDocumentFormat functionalFormat = new
			// OWLXMLDocumentFormat();
			// OWLFunctionalSyntaxOntologyFormat functionalFormat = new
			// OWLFunctionalSyntaxOntologyFormat();
			RDFXMLOntologyFormat functionalFormat = new RDFXMLOntologyFormat();
			File file = new File(validatedTBoxFileName);
			IRI iriDocument = IRI.create(file.toURI());

			// long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, functionalFormat, iriDocument);
			// long endSavingTime = System.currentTimeMillis();
			// long savingTimeInSeconds = (endSavingTime - startSavingTime) /
			// 1000;
			//
			// logger.info("Time for saving the ontology (in seconds):" +
			// savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (OWLOntologyCreationException e1) {
			e1.printStackTrace();
		}
	}

	protected abstract OntologyReader getOntologyReader();
}
