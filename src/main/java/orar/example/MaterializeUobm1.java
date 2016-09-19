package orar.example;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_KoncludeOptimized;
import orar.modeling.ontology2.OrarOntology2;

public class MaterializeUobm1 {
	static Logger logger = Logger.getLogger(MaterializeUobm1.class);
	// static String
	// tbox="/Users/kien/benchmarks/uobm_ox/uobm_origin_minus.owl";
	// static String
	// tbox="/Users/kien/benchmarks/aaai17/uobm_original_tiny2.owl";
	static String tbox = "/Users/kien/benchmarks/uobm_ox/uobmtbox_origin_workwith_oxford_generator.owl";
	static String aboxList = "/Users/kien/benchmarks/uobm_ox/aboxListUOBM1.txt";

	// static String aboxList="/Users/kien/benchmarks/aaai17/aboxList.txt";
	public static void main(String[] args) throws OWLOntologyCreationException {
		// Configuration.getInstance().addDebugLevels(DebugLevel.UPDATING_CONCEPT_ASSERTION,
		// DebugLevel.TRANSFER_CONCEPTASSERTION);
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.REASONING_TIME);
		// Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC,
		// LogInfo.LOADING_TIME, LogInfo.REASONING_TIME,
		// LogInfo.TIME_IN_EACH_METHOD_OR_OPERATION);
		// Configuration.getInstance().addDebugLevels(DebugLevel.UPDATING_CONCEPT_ASSERTION,
		// DebugLevel.TRANSFER_ROLEASSERTION);
		// Configuration.getInstance().addDebugLevels(DebugLevel.TRANSFER_ROLEASSERTION,
		// DebugLevel.SAVE_NORMALIZED_ONTOLOGY);
		// Configuration.getInstance().addDebugLevels(DebugLevel.UPDATING_CONCEPT_ASSERTION,DebugLevel.TRANSFER_ROLEASSERTION,DebugLevel.TRANSFER_SAMEAS);
		runWithCombinedTBoxAndABoxes();

	}

	public static void runWithCombinedTBoxAndABoxes() {

		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tbox, aboxList);
		logger.info(
				"Info: Concstructors in the validated ontology:" + normalizedOrarOntology.getActualDLConstructors());
		// long startAbstraction = System.currentTimeMillis();
		// Materializer materializer = new
		// HornSHOIF_Materializer_Konclude(normalizedOrarOntology);
		Materializer materializer = new HornSHOIF_Materializer_KoncludeOptimized(normalizedOrarOntology);
		materializer.materialize();

	}

}
