package orar.util;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Pause {
	private static final Logger logger = Logger.getLogger(Pause.class);
	/**
	 * pause the program, press a key to continue
	 */
	public static void pause() {
		logger.info("Press any key to continue");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
