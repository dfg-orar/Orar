package orar.example;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_Hermit;
import orar.modeling.ontology2.OrarOntology2;
import x.io.ontologyreader.HornSHOIF_OntologyReader;
import x.io.ontologyreader.OntologyReader;
import x.materializer.Materializer;

public class MaterializeCoburn {
//	static String tboxabox = "/Users/kien/benchmarks/coburn/coburnSHOIF.owl";
	static String tboxabox = "src/test/resources/optimization/example1.owl";
	static final Logger logger = Logger.getLogger(MaterializeCoburn.class);

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.NORMALIZATION_INFO,LogInfo.INPUTONTOLOGY_INFO);
		Configuration.getInstance().addDebugLevels(DebugLevel.PRINT_NORMALIZATION,DebugLevel.PRINT_TYPES);
		runWithSeperatedTBoxAndABoxes();

	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tboxabox);

		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new HornSHOIF_Materializer_Hermit(normalizedOrarOntology);
		materializer.materialize();
		
		logger.info(StatisticVocabulary.TOTAL_REASONING_TIME + materializer.getReasoningTimeInSeconds());

	}

}
