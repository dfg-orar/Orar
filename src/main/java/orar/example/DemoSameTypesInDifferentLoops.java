package orar.example;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_Hermit;
import orar.modeling.ontology.OrarOntology;

/**
 * This example is to demonstrate that the types could remain the same in
 * different loops, but the original ABox got updated.
 * 
 * @author kien
 *
 */
public class DemoSameTypesInDifferentLoops {
	static Logger logger = Logger.getLogger(DemoSameTypesInDifferentLoops.class);

	// static String combinedAboxAndTBox =
	// "src/test/resources/main/sameTypesInDifferentLoop.owl";
	static String combinedAboxAndTBox = "src/test/resources/main/sameTypesInDifferentLoop2.owl";

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.REASONING_TIME);
		runWithCombinedTBoxAndABoxes();

	}

	public static void runWithCombinedTBoxAndABoxes() {

		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OrarOntology normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(combinedAboxAndTBox);
		logger.info(
				"Info: Concstructors in the validated ontology:" + normalizedOrarOntology.getActualDLConstructors());
		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new HornSHOIF_Materializer_Hermit(normalizedOrarOntology);
		materializer.materialize();

	}

}
