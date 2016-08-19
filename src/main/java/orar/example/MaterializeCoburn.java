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
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_Konclude;
import orar.modeling.ontology.OrarOntology;

public class MaterializeCoburn {
	static String tboxabox="/Users/kien/benchmarks/coburn/coburnSHOIF.owl";
	
	static final Logger logger =Logger.getLogger(MaterializeCoburn.class);
	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME);
		runWithSeperatedTBoxAndABoxes();
		

	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OrarOntology normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tboxabox);

		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new HornSHOIF_Materializer_Konclude(normalizedOrarOntology);
		materializer.materialize();
		logger.info(StatisticVocabulary.TIME_REASONING_USING_ABSRTACTION+materializer.getReasoningTimeInSeconds());

	}

}