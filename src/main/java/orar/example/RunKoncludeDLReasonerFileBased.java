package orar.example;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.KoncludeDLReasonerFileBased;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class RunKoncludeDLReasonerFileBased {

	// static String combinedAboxAndTBox =
	// "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-tiny.owl";
//	static String combinedAboxAndTBox = "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-small6.owl";
	static String combinedAboxAndTBox = "/Users/kien/benchmarks/coburn/coburnSHOIF.owl";
	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.REASONING_TIME);
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OWLOntology owlOntology = ontologyReader.getOWLAPIOntology(combinedAboxAndTBox);

		DLReasoner dlReasoner = new KoncludeDLReasonerFileBased(owlOntology);
		dlReasoner.computeEntailments();

	}

}
