package orar.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.dlreasoner.DLReasoner;
import orar.dlreasoner.FactDLReasoner;
import orar.dlreasoner.HermitDLReasoner;
import orar.dlreasoner.KoncludeDLReasoner;
import orar.dlreasoner.KoncludeDLReasonerFileBased;
import orar.dlreasoner.PelletDLReasoner;
import orar.io.ontologyreader.HornSHOIF_OntologyReader;
import orar.io.ontologyreader.OntologyReader;

public class DLReasonerCLI {
	private static Configuration config = Configuration.getInstance();
	private static Logger logger = Logger.getLogger(DLReasonerCLI.class);

	public static void main(String[] args) {
		Options options = new Options();
		/*
		 * Boolean options
		 */
		Option parsingTime = new Option(Argument.LOADING_TIME, false, "print time for parsing ontology");
		Option runningTime = new Option(Argument.REASONING_TIME, false, "print time for ontology materialization");

		Option totalTime = new Option(Argument.TOTAL_TIME, false,
				"print total time of the system,e.g. parsing time + materialization time");

		Option statistic = new Option(

				Argument.STATISTIC, false, "print statistic information, i.e. for experiment result of the paper");

		/*
		 * Argument options
		 */
		Option tbox = new Option(Argument.TBOX, true, "TBox OWL file");
		Option aboxes = new Option(Argument.ABOX, true,
				"a text file containing the list of ABox files, each file in a separated line");
		Option ontology = new Option(Argument.ONTOLOGY, true, "OWL ontology file containing both TBox and ABox(es)");
		// Option reasoner = new Option(
		// REASONER,
		// true,
		// "set the reasoner used in the system. If you choose konclude, then
		// the path to Konclude reasoner must be provided");
		//
		StringBuilder reasonerDescription = new StringBuilder();
		reasonerDescription.append("set the reasoner used in the system:");
		reasonerDescription.append(
				Argument.KONCLUDE + " or " + Argument.HERMIT + " or " + Argument.FACT + " or " + Argument.PELLET);
		reasonerDescription.append(" If you choose konclude, then the path to Konclude reasoner must be provided");
		Option reasoner = Option.builder(Argument.REASONER).required().desc(reasonerDescription.toString()).hasArg(true)
				.build();

		Option konclude = new Option(Argument.KONCLUDEPATH, true, "Konclude reasoner file");
		Option port = new Option(Argument.PORT, true,
				"Port number of Konclude server in case the inner reasoner is Konclude");

		/*
		 * add options
		 */

		options.addOption(totalTime);
		options.addOption(parsingTime);
		options.addOption(runningTime);

		options.addOption(statistic);

		options.addOption(tbox);
		options.addOption(aboxes);
		options.addOption(ontology);
		options.addOption(reasoner);
		options.addOption(konclude);

		options.addOption(port);

		// create the parser
		CommandLineParser parser = new DefaultParser();
		try {
			/*
			 * parse the command line arguments
			 */
			CommandLine commandLine = parser.parse(options, args);

			/*
			 * Check the validity of the arguments
			 */
			if (!argumentsAreValid(commandLine)) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("orar", options);
				return;
			}
			/*
			 * get arguments and config the system accordingly
			 */
			setConfigWithBooleanArguments(commandLine);
			logger.info("Run DLReasoner:" + commandLine.getOptionValue(Argument.REASONER));

			OWLOntology owlOntology = getOWLOntology(commandLine);
			DLReasoner dlReasoner = getDLReasoner(commandLine, owlOntology);

			dlReasoner.computeEntailments();

			String reasonerName = commandLine.getOptionValue(Argument.REASONER);
			logger.info("DLReasoner: " + reasonerName);

		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("orar", options);
		}
	}

	static private DLReasoner getDLReasoner(CommandLine commandLine, OWLOntology owlOntology) {
		DLReasoner dlReasoner=null;
		String reasonerName = commandLine.getOptionValue(Argument.REASONER);

		if (reasonerName.equals(Argument.HERMIT)) {
			dlReasoner = new HermitDLReasoner(owlOntology);
		}

		if (reasonerName.equals(Argument.KONCLUDE)) {
			String koncludePath = commandLine.getOptionValue(Argument.KONCLUDEPATH);
			config.setKONCLUDE_BINARY_PATH(koncludePath);
			String port = commandLine.getOptionValue(Argument.PORT);
			int intPort = Integer.parseInt(port);
			dlReasoner = new KoncludeDLReasonerFileBased(owlOntology, intPort);
		}

		if (reasonerName.equals(Argument.FACT)) {
			dlReasoner = new FactDLReasoner(owlOntology);
		}

		if (reasonerName.equals(Argument.PELLET)) {
			dlReasoner = new PelletDLReasoner(owlOntology);
		}

		return dlReasoner;

	}

	static private boolean argumentsAreValid(CommandLine commandLine) {

		if (commandLine.hasOption(Argument.ONTOLOGY)) {
			if (commandLine.hasOption(Argument.TBOX) || commandLine.hasOption(Argument.ABOX)) {
				System.err.print("More than one ways to read the input ontology. Please choose either -"
						+ Argument.ONTOLOGY + " or -" + Argument.TBOX + " -" + Argument.ABOX);
				return false;
			}
		}

		if (!commandLine.hasOption(Argument.ONTOLOGY)
				&& !(commandLine.hasOption(Argument.TBOX) && commandLine.hasOption(Argument.ABOX))) {
			System.err.print("Input ontology arguments are missing. Please choose either -" + Argument.ONTOLOGY
					+ " or -" + Argument.TBOX + " -" + Argument.ABOX);
			return false;

		}

		if (!Argument.reasonerList.contains(commandLine.getOptionValue(Argument.REASONER))) {
			System.err.print("Please choose correct name of the owlreasoner, choose among " + Argument.reasonerList);
			return false;
		}

		if (commandLine.getOptionValue(Argument.REASONER).equals(Argument.KONCLUDE)) {
			if (!commandLine.hasOption(Argument.KONCLUDEPATH) || !commandLine.hasOption(Argument.PORT)) {
				System.err.print("Konclude needs to has Path and Port");
				return false;
			}
		}

		return true;
	}

	static private void setConfigWithBooleanArguments(CommandLine commandLine) {
		if (commandLine.hasOption(Argument.LOADING_TIME)) {
			config.addLoginfoLevels(LogInfo.LOADING_TIME);
		}

		if (commandLine.hasOption(Argument.REASONING_TIME)) {
			config.addLoginfoLevels(LogInfo.REASONING_TIME);
		}

		if (commandLine.hasOption(Argument.TOTAL_TIME)) {
			config.addLoginfoLevels(LogInfo.TOTAL_TIME);
		}

		if (commandLine.hasOption(Argument.STATISTIC)) {
			config.addLoginfoLevels(LogInfo.STATISTIC);
		}

	}

	static private OWLOntology getOWLOntology(CommandLine commandLine) {
		OWLOntology owlOntology;

		OntologyReader ontReader = new HornSHOIF_OntologyReader();

		if (commandLine.hasOption(Argument.ONTOLOGY)) {
			String owlFilePath = commandLine.getOptionValue(Argument.ONTOLOGY);

			owlOntology = ontReader.getOWLAPIOntology(owlFilePath);

		} else {
			String tboxFile = commandLine.getOptionValue(Argument.TBOX);
			String aboxList = commandLine.getOptionValue(Argument.ABOX);
			owlOntology = ontReader.getOWLAPIOntology(tboxFile, aboxList);
		}

		return owlOntology;
	}
}
