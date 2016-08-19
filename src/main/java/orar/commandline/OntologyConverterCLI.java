package orar.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import orar.owlconverter.DLLiteHOD_OWLOntologyConverter;
import orar.owlconverter.OWLOntologyConverter;

public class OntologyConverterCLI {
	// private static Configuration config = Configuration.getInstance();
	private static Logger logger = Logger.getLogger(OntologyConverterCLI.class);

	public static void main(String[] args) {
		Options options = new Options();

		/*
		 * Argument options
		 */
		Option tbox = new Option(Argument.TBOX, true, "TBox OWL file");
		Option aboxes = new Option(Argument.ABOX, true,
				"a text file containing the list of ABox files, each file in a separated line");
		Option outputFile = new Option(Argument.OUTPUTONTOLOGY, true,
				"a text file containing the list of ABox files, each file in a separated line");

		/*
		 * add options
		 */

		options.addOption(tbox);
		options.addOption(aboxes);
		options.addOption(outputFile);

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
				formatter.printHelp("OntologyConverter", options);
				return;
			}
			/*
			 * get arguments and config the system accordingly
			 */
			OWLOntologyConverter ontologyConverter = getOWLOntologyConverter();
			String tboxFilePath = commandLine.getOptionValue(Argument.TBOX);
			String aboxListPath = commandLine.getOptionValue(Argument.ABOX);
			String outputOntologyPath = commandLine.getOptionValue(Argument.OUTPUTONTOLOGY);
			ontologyConverter.convertToAllInOneOWLFunctionalSynxtax(tboxFilePath, aboxListPath, outputOntologyPath);

		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("orar", options);
		}
	}

	private static OWLOntologyConverter getOWLOntologyConverter() {

		return new DLLiteHOD_OWLOntologyConverter();
	}

	static private boolean argumentsAreValid(CommandLine commandLine) {

		if (!commandLine.hasOption(Argument.TBOX)) {
			return false;
		}
		if (!commandLine.hasOption(Argument.ABOX)) {
			return false;
		}
		if (!commandLine.hasOption(Argument.OUTPUTONTOLOGY)) {
			return false;
		}

		return true;
	}

}
