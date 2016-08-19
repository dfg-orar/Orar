package orar.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class OntologySaving {
	private static Logger logger = Logger.getLogger(OntologySaving.class);

	public static void saveOntologyToFile(OWLOntology ontology, String fileName) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		logger.info("Saving ontology to a file ..." + fileName);
		OWLFunctionalSyntaxOntologyFormat functionalFormat = new OWLFunctionalSyntaxOntologyFormat();
		// OWLXMLOntologyFormat functionalFormat = new
		// OWLXMLOntologyFormat();
		File file = new File(fileName);
		IRI iriDocument = IRI.create(file.toURI());
		try {
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, functionalFormat, iriDocument);
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;

			logger.info("Time for saving the ontology for Konclude (in seconds):" + savingTimeInSeconds);

		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

}
