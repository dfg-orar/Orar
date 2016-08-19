package orar.example;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;
import orar.io.ontologyreader.DLLiteH_OntologyReader;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.materializer.DLLite.DLLite_Materializer_Hermit;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_Hermit;
import orar.modeling.ontology.OrarOntology;

public class MaterializeNPD {
	static String tboxFileName = "/Users/kien/benchmarks/npd-v2/npd.owl";
	static String aboxListFileName = "/Users/kien/benchmarks/npd-v2/aboxList.txt";
	static final Logger logger =Logger.getLogger(MaterializeNPD.class);
	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME);
		runWithSeperatedTBoxAndABoxes();
		

	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new DLLiteH_OntologyReader();
		OrarOntology normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tboxFileName, aboxListFileName);

		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new DLLite_Materializer_Hermit(normalizedOrarOntology);
		materializer.materialize();
		logger.info(StatisticVocabulary.TIME_REASONING_USING_ABSRTACTION+materializer.getReasoningTimeInSeconds());

	}

}
