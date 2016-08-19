package orar.example;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_Hermit;
import orar.modeling.ontology.OrarOntology;

/**
 * Examples to demostrate how to run Abstraction Refinement Materilzer with
 * Hermit (change Materializer to test with other reasoners). There are two
 * options of input:<br>
 * Option 1: Tbox is an OWLOntology file. ABoxes are stored in different RDF/XML
 * files, whose named is stored in an input text file.<br>
 * Option 2: Tbox and ABox are stored in a single OWL Syntax file.
 * 
 * @author kien
 *
 */
public class RunMaterializationUsingAbstractionRefinement {

	static String tboxFileName = "src/test/resources/uobm_origintbox_oxforddata/uobmtbox_origin_workwith_oxford_generator.owl";
	static String aboxListFileName = "src/test/resources/uobm_origintbox_oxforddata/aboxListOf2.txt";

	static String combinedAboxAndTBox = "src/test/resources/uobm-ox/u1AboxAndTbox/univ0-tiny.owl";

	public static void main(String[] args) throws OWLOntologyCreationException {
		Configuration.getInstance().clearDebugLevels();
		Configuration.getInstance().clearLogInfoLevels();
		Configuration.getInstance().addLoginfoLevels(LogInfo.STATISTIC, LogInfo.LOADING_TIME);
		runWithSeperatedTBoxAndABoxes();
//		runWithCombinedTBoxAndABoxes();

	}

	public static void runWithSeperatedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OrarOntology normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(tboxFileName, aboxListFileName);

		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new HornSHOIF_Materializer_Hermit(normalizedOrarOntology);
		materializer.materialize();

	}

	public static void runWithCombinedTBoxAndABoxes() {
		OntologyReader ontologyReader = new HornSHOIF_OntologyReader();
		OrarOntology normalizedOrarOntology = ontologyReader.getNormalizedOrarOntology(combinedAboxAndTBox);

		// long startAbstraction = System.currentTimeMillis();
		Materializer materializer = new HornSHOIF_Materializer_Hermit(normalizedOrarOntology);
		materializer.materialize();

	}

}
