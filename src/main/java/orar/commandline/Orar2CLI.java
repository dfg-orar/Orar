package orar.commandline;

import java.io.File;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;
import orar.materializer.Materializer;
import orar.materializer.DLLiteExtensions.DLLiteExtension_Materializer_Konclude;
import orar.materializer.HornSHIF.HornSHIF_Materializer_KoncludeOptimized;
import orar.materializer.HornSHIF_Increment.HornSHIF_Materializer_KoncludeOptimized_Increment;
import orar.materializer.HornSHOIF.HornSHOIF_Materializer_KoncludeOptimized;
import orar.materializer.HornSHOIF_Increment.HornSHOIF_Materializer_KoncludeOptimized_Increment;
import orar.modeling.ontology2.OrarOntology2;
import orar.strategyindentifying.StrategyIdentifier;
import orar.strategyindentifying.StrategyIdentifierImpl;
import orar.strategyindentifying.StrategyName;

public class Orar2CLI {
	private static Configuration config = Configuration.getInstance();
	private static Logger logger = Logger.getLogger(Orar2CLI.class);
	private static long totalLoadingTime = 0;

	private static void initLogger() {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		rootLogger.removeAllAppenders();

		String PATTERN = "%d{MM-dd HH:mm} %-5p - %m%n";
//		String PATTERN = " %-5p - %m%n";
		PatternLayout patternLayout = new PatternLayout(PATTERN);
		ConsoleAppender console = new ConsoleAppender(patternLayout);
		rootLogger.addAppender(console);
	}

	public static void main(String[] args) {
		// PropertyConfigurator.configure("src/main/resources/log4j.properties");
		initLogger();

		Options options = new Options();
		/*
		 * Boolean options
		 */
		Option parsingTime = new Option(Argument.LOADING_TIME, false, "print loading time");
		Option runningTime = new Option(Argument.REASONING_TIME, false, "print time for materialization");

		Option statistic = new Option(Argument.STATISTIC, false,
				"print statistic information of the (materialized) ontology");

		Option detailStatistic = new Option(Argument.DETAILED_STATISTIC, false,
				"print detailed-statistic information of the (materialized) ontology");
		
		Option help = new Option(Argument.HELP, false, "print help");

		/*
		 * Argument options
		 */
		// Option tbox = new Option(Argument.TBOX, true, "input TBox OWL file in
		// any format supported by OWLAPI");
		Option tbox = Option.builder(Argument.TBOX).required().hasArg(true)
				.desc("input TBox OWL file in any format supported by OWLAPI").build();

		// Option aboxes = new Option(Argument.ABOX, true,
		// "an input text file containing the list of ABox files in RDF/XML, N3,
		// or Turtle format; one file per line");
		Option aboxes = Option.builder(Argument.ABOX).required().hasArg(true)
				.desc("an input text file containing the list of ABox files in RDF/XML, N3, or Turtle format; one file per line")
				.build();

		// Option konclude = new Option(Argument.KONCLUDEPATH, true,
		// "Path to Konclude reasoner binary file");

		Option konclude = Option.builder(Argument.KONCLUDEPATH).hasArg(true).required()
				.desc("Path to Konclude reasoner binary file").build();

		// Option port = new Option(Argument.PORT, true,
		// "port number of Konclude running with OWLLink");

		Option port = Option.builder(Argument.PORT).hasArg(true).required()
				.desc("port number of Konclude running with OWLLink").build();

		Option outputABox = new Option(Argument.OUTPUTABOX, true,
				"the output file containing all entailed atomic assertions in OWL/XML format");

		/*
		 * add options
		 */

		// options.addOption(totalTime);
		options.addOption(parsingTime);
		options.addOption(runningTime);
		options.addOption(statistic);
		options.addOption(detailStatistic);
		options.addOption(tbox);
		options.addOption(aboxes);
		options.addOption(konclude);
		// options.addOption(split);
		options.addOption(port);
		options.addOption(outputABox);
		options.addOption(help);

		// create the parser
		CommandLineParser parser = new DefaultParser();
		try {
			/*
			 * parse the command line arguments
			 */
			CommandLine commandLine = parser.parse(options, args);

			/*
			 * print help
			 */
			if (commandLine.hasOption(Argument.HELP)) {
				printHelp(options);
				return;
			}

			/*
			 * get arguments and config the system accordingly
			 */
			setConfigWithBooleanArguments(commandLine);

			OrarOntology2 orarOntology = getOrarOntology(commandLine);
			Materializer materializer = getMaterializer(commandLine, orarOntology);
			// logger.info("Run " + materializer.getClass());
			runMaterializer(materializer, commandLine);
			logger.info("************** Total times **************");
			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info(StatisticVocabulary.TIME_LOADING_IN_ALL_STEPS + totalLoadingTime);
			}

			if (config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
				logger.info(StatisticVocabulary.TOTAL_TIME_REASONING_ON_ABSTRACT_ONTOLOGIES
						+ materializer.getReasoningTimeOfInnerReasonerInSeconds());

				logger.info(StatisticVocabulary.TOTAL_REASONING_TIME + materializer.getReasoningTimeInSeconds());
			}
			logger.info("Finished.");
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed. " + exp.getMessage() + ". Or missing the argument of some option.");

			printHelp(options);

		}
	}

	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Orar", options);
		printExampleRun();
	}

	private static void printExampleRun() {
		System.out.println("");
		System.out.println("Example run:");
		System.out.println(
				"java -Xmx2G -jar x.jar -koncludepath ./Konclude -port 9090 -reasoningtime -tbox ./tbox.owl -abox ./aboxList.txt");
	}

	private static long runMaterializer(Materializer materializer, CommandLine commandLine) {

		materializer.materialize();

		if (commandLine.hasOption(Argument.OUTPUTABOX)) {
			String aboxFile = commandLine.getOptionValue(Argument.OUTPUTABOX);
			Set<OWLAxiom> entailedAssertions = materializer.getOrarOntology().getOWLAPIMaterializedAssertions();
			saveAssertionsToFile(entailedAssertions, aboxFile);
		}
		totalLoadingTime += materializer.getAbstractOntologyLoadingTime();
		return materializer.getReasoningTimeInSeconds();

	}

	private static void saveAssertionsToFile(Set<OWLAxiom> assertions, String fileName) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		logger.info("Saving entailed assertions to the file " + fileName + " ...");
		// OWLFunctionalSyntaxOntologyFormat format = new
		// OWLFunctionalSyntaxOntologyFormat();
		// OWLDocumentFormat format = new OWLXMLDocumentFormat();
		OWLXMLOntologyFormat format = new OWLXMLOntologyFormat();
		File file = new File(fileName);
		IRI iriDocument = IRI.create(file.toURI());
		try {
			OWLOntology ontology = manager.createOntology();
			manager.addAxioms(ontology, assertions);
			long startSavingTime = System.currentTimeMillis();
			manager.saveOntology(ontology, format, iriDocument);
			long endSavingTime = System.currentTimeMillis();
			long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;
			if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
				logger.info("Time for saving the entailed assertions (in seconds):" + savingTimeInSeconds);

			}
			// logger.info(" ...done!");
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}

		catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	static private Materializer getMaterializer(CommandLine commandLine, OrarOntology2 orarOntology) {
		Materializer materializer;
		String reasoner = commandLine.getOptionValue(Argument.REASONER);
		materializer = getHornReasoner(commandLine, reasoner, orarOntology);
		return materializer;
	}

	static private Materializer getHornReasoner(CommandLine commandLine, String reasonerName,
			OrarOntology2 orarOntology) {
		Materializer materializer = null;
		// logger.info("Info: Some DL Constructors in the validated ontology: "
		// + orarOntology.getActualDLConstructors());
		StrategyIdentifier strategyIdentifier = new StrategyIdentifierImpl(orarOntology.getTBoxAxioms());
		StrategyName strategyName = strategyIdentifier.getStrategyName();
		if (config.getLogInfos().contains(LogInfo.TIME_STAMP_FOR_EACH_STEP)) {
			logger.info("Strategy:" + strategyName);
		}
		String koncludePath = commandLine.getOptionValue(Argument.KONCLUDEPATH);
		config.setKONCLUDE_BINARY_PATH(koncludePath);
		String port = commandLine.getOptionValue(Argument.PORT);
		int intPort = Integer.parseInt(port);

		switch (strategyName) {
		case DLLITE_EXTENSION_STRATEGY:
			materializer = new DLLiteExtension_Materializer_Konclude(orarOntology, intPort);
			break;
		case HORN_SHIF_STRATEGY:
			materializer = new HornSHIF_Materializer_KoncludeOptimized_Increment(orarOntology, intPort);
			break;
		case HORN_SHOIF_STRATEGY:
			materializer = new HornSHOIF_Materializer_KoncludeOptimized_Increment(orarOntology, intPort);
			break;
		}
		return materializer;

	}

	static private void setConfigWithBooleanArguments(CommandLine commandLine) {
		if (commandLine.hasOption(Argument.LOADING_TIME)) {
			config.addLoginfoLevels(LogInfo.LOADING_TIME);
		}

		if (commandLine.hasOption(Argument.REASONING_TIME)) {
			config.addLoginfoLevels(LogInfo.REASONING_TIME);
		}

		if (commandLine.hasOption(Argument.STATISTIC)) {
			config.addLoginfoLevels(LogInfo.STATISTIC);
		}
		
		if (commandLine.hasOption(Argument.DETAILED_STATISTIC)) {
			config.addLoginfoLevels(LogInfo.DETAILED_STATISTIC);
		}


	}

	static private OrarOntology2 getOrarOntology(CommandLine commandLine) {
		OrarOntology2 orarOntology;

		OntologyReader ontReader = new HornSHOIF_OntologyReader();

		String tboxFile = commandLine.getOptionValue(Argument.TBOX);
		String aboxList = commandLine.getOptionValue(Argument.ABOX);
		orarOntology = ontReader.getNormalizedOrarOntology(tboxFile, aboxList);

		totalLoadingTime += ontReader.getLoadingTime();
		return orarOntology;
	}
}