package orar.tuning;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.util.PrintingHelper;

public class LoadingIMDB {
	// static String tboxFileName =
	// "src/test/resources/uobm_origintbox_oxforddata/uobmtbox_origin_workwith_oxford_generator.owl";
	// static String aboxListFileName =
	// "src/test/resources/uobm_origintbox_oxforddata/aboxListOf2.txt";

	static String tboxFileName = "/Users/kien/benchmarks/imdb/imdb.owl";
	static String aboxListFileName = "/Users/kien/benchmarks/imdb/aboxListIMDB.txt";

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME, LogInfo.REASONING_TIME);
		OntologyReader ontoReader = new HornSHOIF_OntologyReader();

		OrarOntology2 orarOntology = ontoReader.getNormalizedOrarOntology(tboxFileName, aboxListFileName);
//		System.gc();
		PrintingHelper.printSet(orarOntology.getActualDLConstructors());
	}
}
