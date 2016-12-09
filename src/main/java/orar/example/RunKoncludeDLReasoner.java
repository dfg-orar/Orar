package orar.example;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.KoncludeDLReasoner;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class RunKoncludeDLReasoner {

	static String tboxFileName = "src/test/resources/uobm-ox/tbox/uobm-tbox-oxford.owl";
	static String aboxListFileName = "src/test/resources/uobm-ox/abox/aboxListOf2.txt";

	static String combinedAboxAndTBox = "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-small6.owl";

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.REASONING_TIME);
		runWithSeperatedTBoxAndABoxes();

	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		// OWLOntology owlOntology =
		// ontologyReader.getOWLAPIOntology(tboxFileName, aboxListFileName);
		OWLOntology owlOntology = ontologyReader.getOWLAPIOntology(combinedAboxAndTBox);
		DLReasoner dlReasoner = new KoncludeDLReasoner(owlOntology);
		dlReasoner.computeEntailments();

	}
}
