package orar.owlconverter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.formats.TurtleOntologyFormat;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import orar.io.ontologyreader.OntologyReader;
import orar.modeling.ontology.OrarOntology;

public abstract class OWLOntologyConverterTemplate implements OWLOntologyConverter {
	private static Logger logger = Logger.getLogger(OWLOntologyConverterTemplate.class);
	private final OntologyReader ontologyReader;

	public OWLOntologyConverterTemplate() {
		this.ontologyReader = getOntologyReader();
	}

	protected abstract OntologyReader getOntologyReader();

	@Override
	public void convertToAllInOneOWLFunctionalSynxtax(String tboxFile, String aboxListFile,
			String owlFunctionalSyntaxFile) {
		long startSavingTime = System.currentTimeMillis();
		OWLOntology owlOntology = this.ontologyReader.getOWLAPIOntology(tboxFile, aboxListFile);
		long endSavingTime = System.currentTimeMillis();
		long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;
		logger.info("Time for loading the ontology (in seconds): " + savingTimeInSeconds);
		saveOntologyInFunctionalSyntax(owlOntology, owlFunctionalSyntaxFile);
		logger.info("Done!");
	}

	private void saveOntologyInFunctionalSyntax(OWLOntology ontology, String owlFunctionalSyntaxFile) {
		OWLOntologyManager manager = ontology.getOWLOntologyManager();
		logger.info("Saving ontology to the file: " + owlFunctionalSyntaxFile + "...");
		OWLFunctionalSyntaxOntologyFormat functionalFormat = new OWLFunctionalSyntaxOntologyFormat();

//		 OWLXMLOntologyFormat functionalFormat = new OWLXMLOntologyFormat();
	
		File file = new File(owlFunctionalSyntaxFile);
		IRI iriDocument = IRI.create(file.toURI());
		try {
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, functionalFormat, iriDocument);
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	private void saveABoxAssertionsInTurtle(OWLOntology ontology, String owlFunctionalSyntaxFile) {
		OWLOntologyManager manager = ontology.getOWLOntologyManager();
		logger.info("Saving ontology to the file: " + owlFunctionalSyntaxFile + "...");
		OWLFunctionalSyntaxOntologyFormat functionalFormat = new OWLFunctionalSyntaxOntologyFormat();
		// OWLXMLOntologyFormat functionalFormat = new
		// OWLXMLOntologyFormat();
		File file = new File(owlFunctionalSyntaxFile);
		IRI iriDocument = IRI.create(file.toURI());
		try {
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, functionalFormat, iriDocument);
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}
	
	private void saveOntologyInRDFXML(OWLOntology ontology, String rdfxmlFile) {
		OWLOntologyManager manager = ontology.getOWLOntologyManager();
		logger.info("Saving ontology to the file: " + rdfxmlFile + "...");
		RDFXMLOntologyFormat rdfxmlFormat = new RDFXMLOntologyFormat();
		// OWLFunctionalSyntaxOntologyFormat functionalFormat = new
		// OWLFunctionalSyntaxOntologyFormat();
		// OWLXMLOntologyFormat functionalFormat = new
		// OWLXMLOntologyFormat();
		File file = new File(rdfxmlFile);
		IRI iriDocument = IRI.create(file.toURI());
		try {
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, rdfxmlFormat, iriDocument);
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology  (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	/**
	 * save axioms to an ontology in functional syntax
	 * 
	 * @param axioms
	 * @param owlFunctionalSyntaxFile
	 * @param iri
	 */
	private void saveOntology(Set<OWLAxiom> axioms, String owlFunctionalSyntaxFile, IRI iri) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		logger.info("Saving ontology to the file: " + owlFunctionalSyntaxFile + "...");
		OWLFunctionalSyntaxOntologyFormat functionalFormat = new OWLFunctionalSyntaxOntologyFormat();
		// OWLXMLOntologyFormat functionalFormat = new
		// OWLXMLOntologyFormat();

		try {
			File file = new File(owlFunctionalSyntaxFile);
			OWLOntology ontology = manager.createOntology(iri);
			manager.addAxioms(ontology, axioms);
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, functionalFormat, IRI.create(file));
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * save axioms to an ontology in functional syntax
	 * 
	 * @param axioms
	 * @param owlFunctionalSyntaxFile
	 * @param iri
	 */
	private void saveAxiomsToAnOntologyInRDFXML(Set<OWLAxiom> axioms, String rdfxmlFile, IRI iri) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		logger.info("Saving ontology to the file: " + rdfxmlFile + "...");
		RDFXMLOntologyFormat rdfxmlFormat = new RDFXMLOntologyFormat();

		try {
			File file = new File(rdfxmlFile);
			OWLOntology ontology = manager.createOntology(iri);
			manager.addAxioms(ontology, axioms);
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, rdfxmlFormat, IRI.create(file));
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * save axioms to an ontology in functional syntax
	 * 
	 * @param axioms
	 * @param owlFunctionalSyntaxFile
	 * @param iri
	 */
	private void saveAxiomsToAnOntologyInTurtle(Set<OWLAxiom> axioms, String rdfxmlFile, IRI iri) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		logger.info("Saving ontology to the file: " + rdfxmlFile + "...");
		TurtleOntologyFormat turtleFormat = new TurtleOntologyFormat();

		try {
			File file = new File(rdfxmlFile);
			OWLOntology ontology = manager.createOntology(iri);
			manager.addAxioms(ontology, axioms);
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, turtleFormat, IRI.create(file));
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

	public void convertToSeparatedFiles(String allinoneOntologyFile, String tboxFile, String aboxFileInRDFXML) {
		OWLOntologyManager ontManager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology allInOneOntology = ontManager.loadOntologyFromOntologyDocument(new File(allinoneOntologyFile));
			IRI iri = allInOneOntology.getOntologyID().getOntologyIRI();
			/*
			 * save TBox
			 */
			Set<OWLAxiom> tboxAxioms = new HashSet<OWLAxiom>();
			tboxAxioms.addAll(allInOneOntology.getTBoxAxioms(true));
			tboxAxioms.addAll(allInOneOntology.getRBoxAxioms(true));

			saveOntology(tboxAxioms, tboxFile, iri);

			/*
			 * save ABox
			 */
			Set<OWLAxiom> aboxAssertions = new HashSet<OWLAxiom>();
			aboxAssertions.addAll(allInOneOntology.getABoxAxioms(true));
			saveAxiomsToAnOntologyInRDFXML(aboxAssertions, aboxFileInRDFXML, iri);
			logger.info("TBox axioms have been saved to: " + tboxFile);
			logger.info("ABox axioms have been saved to: " + aboxFileInRDFXML);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

	public void convertToTurtleABox(String tboxFile, String aboxListFile, String aboxFileInTurtle) {

	}
}
