package orar.owlconverter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class OntologySplitter {
	private static Logger logger = Logger.getLogger(OntologySplitter.class);

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
	public static void main(String[] args){
		OntologySplitter splitter = new OntologySplitter();
		String allInOneOntologyFile="/Users/kien/benchmarks/coburn/coburnSHOIF.owl";
		String tbox="/Users/kien/benchmarks/coburn/coburnTbox.owl";
		String abox="/Users/kien/benchmarks/coburn/coburnABox.owl";
		splitter.convertToSeparatedFiles(allInOneOntologyFile, tbox, abox);
	}
}
