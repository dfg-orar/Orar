package orar.example;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;
import orar.materializer.DLLiteExtensions.DLLiteExtension_Materializer_Hermit;
import orar.modeling.ontology2.OrarOntology2;
import x.io.ontologyreader.DLLiteH_OntologyReader;
import x.io.ontologyreader.OntologyReader;
import x.materializer.Materializer;

public class MaterializeNPD {
	static String tboxFileName = "/Users/kien/benchmarks/npd-v2/npd.owl";
	static String aboxListFileName = "/Users/kien/benchmarks/npd-v2/aboxList.txt";
	static final Logger logger = Logger.getLogger(MaterializeNPD.class);

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME);
		runWithSeperatedTBoxAndABoxes();

	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new DLLiteH_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tboxFileName, aboxListFileName);

		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new DLLiteExtension_Materializer_Hermit(normalizedOrarOntology);
		materializer.materialize();
		logger.info(StatisticVocabulary.TOTAL_REASONING_TIME + materializer.getReasoningTimeInSeconds());

	}

}
