package orar.example;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.HermitDLReasoner;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class RunDLReasoner {

	static String tboxFileName = "src/test/resources/uobm-ox/tbox/univ-bench-dl-ox.owl";
	static String aboxListFileName = "src/test/resources/uobm-ox/abox/aboxListOf2.txt";

	static String combinedAboxAndTBox = "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-tiny.owl";

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.REASONING_TIME);
		runWithSeperatedTBoxAndABoxes();

		runWithCombinedTBoxAndABoxes();
	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OWLOntology owlOntology = ontologyReader.getOWLAPIOntology(tboxFileName, aboxListFileName);

		DLReasoner dlReasoner = new HermitDLReasoner(owlOntology);
		dlReasoner.computeEntailments();

	}

	public static void runWithCombinedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OWLOntology owlOntology = ontologyReader.getOWLAPIOntology(combinedAboxAndTBox);

		DLReasoner dlReasoner = new HermitDLReasoner(owlOntology);
		dlReasoner.computeEntailments();

	}

}
