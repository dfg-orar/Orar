package orar.tuning;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.data.NormalizationDataFactory;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_KoncludeOptimized;
import orar.modeling.ontology2.OrarOntology2;
import x.io.ontologyreader.HornSHOIF_OntologyReader;
import x.io.ontologyreader.OntologyReader;
import x.materializer.Materializer;

public class Merging {

	private static String ontologyPath = "src/test/resources/merigng/test3.owl";

	public static void main(String[] args) {
		AbstractDataFactory.getInstance().clear();
		NormalizationDataFactory.getInstance().clear();
		MetaDataOfOntology.getInstance().clear();
		DataForTransferingEntailments.getInstance().clear();
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();

		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.REASONING_TIME, LogInfo.LOADING_TIME,
				LogInfo.TIME_STAMP_FOR_EACH_STEP);
		// Configuration.getInstance().addDebugLevels(
		// DebugLevel.PRINT_MARKING_INDIVIDUALS,
		// DebugLevel.ADDING_MARKING_AXIOMS);
		System.out.println("Loading ontology for abstraction materializer....");
		OntologyReader ontoReader = new HornSHOIF_OntologyReader();
		OrarOntology2 normalizedOrarOntology = ontoReader.getNormalizedOrarOntology(ontologyPath);
		// System.out.println("Print indexing after reading ontology");
		// PrintingHelper.printMap(IndividualIndexer.getInstance().viewMapIndividuslString2Integer());
		Materializer materializer = new HornSHOIF_Materializer_KoncludeOptimized(normalizedOrarOntology);
		materializer.materialize();
	}
}
