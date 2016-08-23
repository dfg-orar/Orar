package orar.commandline;

import org.apache.log4j.Logger;

import orar.owlconverter.HornSHOIF_Orar2TurtleConverter;
import orar.owlconverter.Orar2TurtleConverter;

public class TurtleConverterCLI {
	// private static Configuration config = Configuration.getInstance();
	private static Logger logger = Logger.getLogger(TurtleConverterCLI.class);

	public static void main(String[] args) {
		if (args.length == 4) {
			String tboxFile = args[0];
			String aboxListFile = args[1];
			String outputTboxFile = args[2];
			String outputTurtleFile = args[3];

			Orar2TurtleConverter converter = new HornSHOIF_Orar2TurtleConverter();
			converter.convert(tboxFile, aboxListFile, outputTboxFile, outputTurtleFile);
		} else if (args.length == 3) {
			String tboxFile = args[0];
			String aboxListFile = args[1];

			String outputTurtleFile = args[2];

			Orar2TurtleConverter converter = new HornSHOIF_Orar2TurtleConverter();
			converter.convert(tboxFile, aboxListFile, outputTurtleFile);
		} else {

			logger.error("Wrong arguments."
					+ "\n Example 1: java -jar turtleConverter.jar inputTBoxFilename.owl aboxList.txt outputTBoxFileName.owl outputTurtleFile.ttl"
					+ "\n Example 2: java -jar turtleConverter.jar inputTBoxFilename.owl aboxList.txt outputTurtleFile.ttl");

		}

	}
}
