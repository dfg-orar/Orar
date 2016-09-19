package x.io.ontologyreader;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;
import orar.dlfragmentvalidator.OWLOntologyValidator;
import orar.modeling.ontology2.OrarOntology2;
import orar.normalization.Normalizer;
import orar.normalization.transitivity.TransitivityNormalizer;
import orar.normalization.transitivity.TransitivityNormalizerWithHermit;
import x.util.OntologyInfo;
import x.util.OntologySaving;
import x.util.OntologyStatistic;
import x.util.PrintingHelper;

public abstract class OntologyReaderTemplate implements OntologyReader {
	protected Normalizer normalizer;
	protected OWLOntologyValidator profileValidator;
	private Logger logger = Logger.getLogger(OntologyReaderTemplate.class);
	private Configuration config = Configuration.getInstance();
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private long loadingTime = 0;

	protected abstract OWLOntologyValidator getOntologyValidator(OWLOntology owlOntology);

	protected abstract Normalizer getNormalizer(OWLOntology owlOntology);

	@Override
	public OrarOntology2 getNormalizedOrarOntology(String ontologyFileName) {

		long startParsing = System.currentTimeMillis();
		// /*
		// * clear old indexing if there is
		// */
		// IndividualIndexer.getInstance().clear();
		/*
		 * Get a normalized OWLAPI ontology
		 */
		OWLOntology normalizedOWLAPIOntology = getNormalizedOWLAPIOntology(ontologyFileName);

		/*
		 * Convert to AromaOntology
		 */
		OWLAPI2OrarConverter converter = new OWLAPI2OrarConverter(normalizedOWLAPIOntology);

		OrarOntology2 internalOntology = converter.getInternalOntology();
		internalOntology.setActualDLConstructors(profileValidator.getDLConstructorsInInputOntology());

		// if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
		// logger.info("Information of the input ontology.");
		// logger.info("Ontology file:" + ontologyFileName);
		//
		// OntologyStatistic.printInputOntologyInfo(internalOntology);
		//
		// }

		long endParsing = System.currentTimeMillis();
		long parsingTimeInSecond = (endParsing - startParsing) / 1000;

		if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
			logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
		}
		return internalOntology;

	}

	/**
	 * Read a file containing an OWLAPI ontology (could be both TBox and ABox);
	 * normalize it and return the normalized one.
	 * 
	 * @param fileNameToOWLAPIOntology
	 * @return the normalized OWLAPI ontology
	 */
	private OWLOntology getNormalizedOWLAPIOntology(String fileNameToOWLAPIOntology) {
		try {
			long startParsing = System.currentTimeMillis();
			/*
			 * Read the file
			 */
			OWLOntology inputOntology = getInputOWLAPIOntology(fileNameToOWLAPIOntology);

			/*
			 * Get ontology in target DL fragment
			 */

			OWLOntology ontologyInDesiredDLFragment = getOntologyInTargetDLFragment(inputOntology);
			if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
				logger.info(
						"Information of the validated ontology TBox, e.g. after removing axioms not in the considered language:");
				OntologyStatistic.printOWLOntologyInfo(ontologyInDesiredDLFragment);
			}
			/*
			 * Normalize the ontology into normal form
			 */
			long startNormalization = System.currentTimeMillis();
			OWLOntology ontologyInNormalForm = getOntologyInTheNormalForm(ontologyInDesiredDLFragment);

			/*
			 * adding auxiliary axioms w.r.t transitivity
			 */

			OWLOntology ontologyInNormalFormWithAddedAuxiliaryAxiomsForTransitivity = getOntologyWithAuxiliaryAxiomsForTransitivity(
					ontologyInNormalForm);
			long endParsing = System.currentTimeMillis();
			long normalizationTime = (endParsing - startNormalization) / 1000;
			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info(StatisticVocabulary.TIME_NORMALIZATION_OF_TBOX + normalizationTime);
			}
			long parsingTimeInSecond = (endParsing - startParsing) / 1000 - normalizationTime;
			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX + parsingTimeInSecond);
			}

			return ontologyInNormalFormWithAddedAuxiliaryAxiomsForTransitivity;
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}
		return null;
	}

	private OWLOntology getValidatedOWLAPIOntology(String fileNameToOWLAPIOntology) {
		try {
			long startParsing = System.currentTimeMillis();
			/*
			 * Read the file
			 */
			OWLOntology inputOntology = getInputOWLAPIOntology(fileNameToOWLAPIOntology);

			/*
			 * Get ontology in target DL fragment
			 */

			OWLOntology ontologyInDesiredDLFragment = getOntologyInTargetDLFragment(inputOntology);

			long endParsing = System.currentTimeMillis();
			long parsingTimeInSecond = (endParsing - startParsing) / 1000;
			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
			}

			return ontologyInDesiredDLFragment;
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param owlOntologyFileName
	 * @return an OWLAPI Ontology
	 * @throws OWLOntologyCreationException
	 */
	private OWLOntology getInputOWLAPIOntology(String owlOntologyFileName) throws OWLOntologyCreationException {
		logger.info("Using OWLAPI to read the ontology:" + owlOntologyFileName + " ...");
		OWLOntology inputOntology;
		inputOntology = manager.loadOntologyFromOntologyDocument(new File(owlOntologyFileName));

		// if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
		// logger.info("Information of the input ontology.");
		// logger.info("Ontology file:" + owlOntologyFileName);
		//
		// OntologyStatistic.printOWLOntologyInfo(inputOntology);
		//
		// }

		return inputOntology;
	}

	/**
	 * @param inputOntology
	 * @return an ontology in the target DL Fragment
	 */
	private OWLOntology getOntologyInTargetDLFragment(OWLOntology inputOntology) {
		profileValidator = getOntologyValidator(inputOntology);
		profileValidator.validateOWLOntology();
		OWLOntology ontologyInTargetDLFragment = profileValidator.getOWLOntologyInTheTargetedDLFragment();
		if (config.getDebuglevels().contains(DebugLevel.DL_FRAGMENT_VALIDATING)) {
			logger.info("***DEBUG*** validated orar ontology:");
			logger.info("***DEBUG***TBox axioms:");
			PrintingHelper.printSet(ontologyInTargetDLFragment.getTBoxAxioms(true));
			logger.info("***DEBUG***concept names in signature:");
			PrintingHelper.printSet(ontologyInTargetDLFragment.getClassesInSignature(true));

		}
		manager.removeOntology(inputOntology);
		return ontologyInTargetDLFragment;
	}

	/**
	 * @param ontologyInTargetDLFragment
	 * @return the normalized ontology
	 */
	private OWLOntology getOntologyInTheNormalForm(OWLOntology ontologyInTargetDLFragment) {
		normalizer = getNormalizer(ontologyInTargetDLFragment);
		OWLOntology ontologyInNormalForm = normalizer.getNormalizedOntology();

		if (config.getDebuglevels().contains(DebugLevel.DL_FRAGMENT_VALIDATING)) {
			logger.info("***DEBUG*** normalized validated orar ontology:");
			logger.info("***DEBUG***TBox axioms:");
			PrintingHelper.printSet(ontologyInNormalForm.getTBoxAxioms(true));
			logger.info("***DEBUG***concept names in signature:");
			PrintingHelper.printSet(ontologyInNormalForm.getClassesInSignature(true));

		}
		manager.removeOntology(ontologyInTargetDLFragment);
		return ontologyInNormalForm;
	}

	/**
	 * @param ontologyInNormalForm
	 * @return ontology with added auxiliary axioms wrt transitivity
	 */
	private OWLOntology getOntologyWithAuxiliaryAxiomsForTransitivity(OWLOntology ontologyInNormalForm) {
		TransitivityNormalizer transNormalizer = new TransitivityNormalizerWithHermit(ontologyInNormalForm);
		transNormalizer.normalizeTransitivity();
		OWLOntology ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity = transNormalizer.getResultingOntology();
		/*
		 * Debug
		 */
		if (config.getDebuglevels().contains(DebugLevel.PRINT_NORMALIZATION)) {
			logger.info("");
			logger.info("***DEBUG: Normalized Ontology");

			OntologyInfo.printTBoxAxioms(ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity);

			OntologyInfo.printABoxAxioms(ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity);
			logger.info("***DEBUG: End");
			logger.info("");
		}
		/*
		 * Debug:End
		 */
		manager.removeOntology(ontologyInNormalForm);
		return ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity;

	}

	@Override
	public OrarOntology2 getNormalizedOrarOntology(String tboxFileName, String aboxListFileName) {

		long startParsing = System.currentTimeMillis();
		// /*
		// * clear old indexing if there is
		// */
		// IndividualIndexer.getInstance().clear();
		/*
		 * get a normalized owlapi ontology
		 */
		OWLOntology ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity = getNormalizedOWLAPIOntology(
				tboxFileName);
		if (config.getLogInfos().contains(LogInfo.DETAILED_STATISTIC)) {
			logger.info("Information of the validated normalized ontology.");
			OntologyStatistic.printOWLOntologyInfo(ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity);

		}
		if (config.getDebuglevels().contains(DebugLevel.DL_FRAGMENT_VALIDATING)) {
			logger.info("***DEBUG***normalized validated orar ontology:");
			logger.info("***DEBUG***TBox axioms:");
			PrintingHelper.printSet(ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity.getTBoxAxioms(true));
			logger.info("***DEBUG***concept names in signature:");
			PrintingHelper
					.printSet(ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity.getClassesInSignature(true));

		}
		if (config.getDebuglevels().contains(DebugLevel.SAVE_NORMALIZED_ONTOLOGY)) {
			DateFormat dateFormat = new SimpleDateFormat("dd_MMM@HH_mm");
			// get current date time with Date()
			Date date = new Date();

			String savedFileName = "normalizedTBox" + dateFormat.format(date) + ".owl";
			OntologySaving.saveOntologyToFile(ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity,
					savedFileName);
		}

		/*
		 * Read aboxes in stream mannner
		 */
		StreamOntologyReader2InternalModel streamReader = new StreamOntologyReader2InternalModel(
				ontologyInNormalFormAndAddedAuxiliaryAxiomsForTransitivity, aboxListFileName);

		OrarOntology2 internalOntology = streamReader.getOntology();
		internalOntology.setActualDLConstructors(profileValidator.getDLConstructorsInValidatedOntology());
		// if
		// (config.getDebuglevels().contains(DebugLevel.DL_FRAGMENT_VALIDATING)){
		// logger.info("***DEBUG*** actual DL constructors:"+
		// internalOntology.getActualDLConstructors());
		// }
		if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
			logger.info("ABoxList file:" + aboxListFileName);
			logger.info("ABox statistic:");
			OntologyStatistic.printInputABoxOrarOntologyInfo(internalOntology);

		}

		long endParsing = System.currentTimeMillis();
		long parsingTimeInSecond = (endParsing - startParsing) / 1000;
		this.loadingTime = parsingTimeInSecond;
		if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
			logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
		}
		return internalOntology;

	}

	@Override
	public OWLOntology getOWLAPIOntology(String ontologyFileName) {
		try {
			long startParsing = System.currentTimeMillis();
			// /*
			// * clear old indexing if there is
			// */
			// IndividualIndexer.getInstance().clear();
			/*
			 * Read the ontologyFile
			 */

			OWLOntology inputOntology = getInputOWLAPIOntology(ontologyFileName);

			/*
			 * Get the ontology in the target DL fragment
			 */

			OWLOntology profiledOntology = getOntologyInTargetDLFragment(inputOntology);
			/*
			 * Remove unused ontololgies
			 */

			long endParsing = System.currentTimeMillis();
			long parsingTimeInSecond = (endParsing - startParsing) / 1000;

			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
			}

			return profiledOntology;
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public OWLOntology getOWLAPIOntology(String tboxFile, String aboxListFile) {
		try {
			long startParsing = System.currentTimeMillis();
			// /*
			// * clear old indexing if there is
			// */
			// IndividualIndexer.getInstance().clear();
			/*
			 * Read the ontologyFile
			 */

			OWLOntology inputOntology = getInputOWLAPIOntology(tboxFile);

			/*
			 * Get the ontology in the target DL fragment
			 */

			OWLOntology profiledOntology = getOntologyInTargetDLFragment(inputOntology);

			/*
			 * Read assertions in stream manner
			 */
			StreamOntologyReader2OWLAPI streamReader = new StreamOntologyReader2OWLAPI(profiledOntology, aboxListFile);

			OWLOntology owlOntology = streamReader.getOWLAPIOntology();
			if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
				logger.info("===Information of the validated ontology===");
				logger.info("TBox file: " + tboxFile);
				logger.info("ABoxList file: " + aboxListFile);
				OntologyStatistic.printOWLOntologyInfo(owlOntology);
				logger.info("===========================================");
			}

			long endParsing = System.currentTimeMillis();
			long parsingTimeInSecond = (endParsing - startParsing) / 1000;

			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
			}

			return owlOntology;
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}
		return null;
	}

	public OrarOntology2 getNOTNormalizedOrarOntology(String tboxFileName, String aboxListFileName) {
		long startParsing = System.currentTimeMillis();
		// /*
		// * clear old indexing if there is
		// */
		// IndividualIndexer.getInstance().clear();
		/*
		 * get a normalized owlapi ontology
		 */
		OWLOntology validatedOWLAPIOntology = getValidatedOWLAPIOntology(tboxFileName);
		if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
			logger.info("Information of the validated normalized ontology.");
			logger.info("Extracted from ontology input file:" + tboxFileName);

			OntologyStatistic.printOWLOntologyInfo(validatedOWLAPIOntology);

		}
		if (config.getDebuglevels().contains(DebugLevel.DL_FRAGMENT_VALIDATING)) {
			logger.info("***DEBUG***normalized validated orar ontology:");
			logger.info("***DEBUG***TBox axioms:");
			PrintingHelper.printSet(validatedOWLAPIOntology.getTBoxAxioms(true));
			logger.info("***DEBUG***concept names in signature:");
			PrintingHelper.printSet(validatedOWLAPIOntology.getClassesInSignature(true));

		}
		/*
		 * Read aboxes in stream mannner
		 */
		StreamOntologyReader2InternalModel streamReader = new StreamOntologyReader2InternalModel(
				validatedOWLAPIOntology, aboxListFileName);

		OrarOntology2 internalOntology = streamReader.getOntology();
		internalOntology.setActualDLConstructors(profileValidator.getDLConstructorsInValidatedOntology());
		// if
		// (config.getDebuglevels().contains(DebugLevel.DL_FRAGMENT_VALIDATING)){
		// logger.info("***DEBUG*** actual DL constructors:"+
		// internalOntology.getActualDLConstructors());
		// }
		if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
			logger.info("ABoxList file:" + aboxListFileName);
			logger.info("ABox statistic:");
			OntologyStatistic.printInputABoxOrarOntologyInfo(internalOntology);

		}

		long endParsing = System.currentTimeMillis();
		long parsingTimeInSecond = (endParsing - startParsing) / 1000;

		if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
			logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
		}
		return internalOntology;

	}

	public OrarOntology2 getNOTNormalizedOrarOntology(String ontologyFileName) {
		long startParsing = System.currentTimeMillis();
		// /*
		// * clear old indexing if there is
		// */
		// IndividualIndexer.getInstance().clear();
		/*
		 * Get a normalized OWLAPI ontology
		 */
		OWLOntology validatedOWLAPIOntology = getValidatedOWLAPIOntology(ontologyFileName);

		/*
		 * Convert to AromaOntology
		 */
		OWLAPI2OrarConverter converter = new OWLAPI2OrarConverter(validatedOWLAPIOntology);

		OrarOntology2 internalOntology = converter.getInternalOntology();
		internalOntology.setActualDLConstructors(profileValidator.getDLConstructorsInInputOntology());

		// if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
		// logger.info("Information of the input ontology.");
		// logger.info("Ontology file:" + ontologyFileName);
		//
		// OntologyStatistic.printInputOntologyInfo(internalOntology);
		//
		// }

		long endParsing = System.currentTimeMillis();
		long parsingTimeInSecond = (endParsing - startParsing) / 1000;

		if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
			logger.info(StatisticVocabulary.TIME_LOADING_INPUT_TBOX_AND_ABOX + parsingTimeInSecond);
		}
		return internalOntology;
	}
	@Override
	public long getLoadingTime() {
		return this.loadingTime;
	}
}
