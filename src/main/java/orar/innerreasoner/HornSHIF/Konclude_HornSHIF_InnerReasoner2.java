package orar.innerreasoner.HornSHIF;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.innerreasoner.HornSHOIF.Konclude_HornSHOIF_InnerReasoner;

public class Konclude_HornSHIF_InnerReasoner2 extends HornSHIF_InnerReasonerTemplate2 {
	private int portNumber;
	private ExecuteWatchdog watchdog;
	private static Logger logger = Logger.getLogger(Konclude_HornSHIF_InnerReasoner2.class);
	private ByteArrayOutputStream stdout;
	private PumpStreamHandler pumpStreamHander;
	private final int POOLING_INTERVAL = 5; // miliseconds to check if Kondlude
											// is ready
	private final String KONCLUDE_READY = "Listening on port";
	DefaultExecuteResultHandler resultHandler;

	public Konclude_HornSHIF_InnerReasoner2(OWLOntology owlOntology) {
		super(owlOntology);
		this.portNumber = 8080;// default value
		/*
		 * for running Konclude via command line
		 */
		this.stdout = new ByteArrayOutputStream();
		this.pumpStreamHander = new PumpStreamHandler(stdout);
		this.resultHandler = new DefaultExecuteResultHandler();

	}

	public Konclude_HornSHIF_InnerReasoner2(OWLOntology ontology, int portNumber) {
		super(ontology);
		this.portNumber = portNumber;
		/*
		 * for running Konclude via command line
		 */
		this.stdout = new ByteArrayOutputStream();
		this.pumpStreamHander = new PumpStreamHandler(stdout);
		this.resultHandler = new DefaultExecuteResultHandler();

	}

	private void startKoncludeServer() {
		logger.info("Stop any existing Konclude/KoncludeMac/KoncludeLinux process");
		executeCommand("killall -9 Konclude");
		executeCommand("killall -9 KoncludeMac");
		executeCommand("killall -9 KoncludeLinux");
		
		CommandLine cmdLine = new CommandLine(Configuration.getInstance().getKONCLUDE_BINARY_PATH());

		cmdLine.addArgument("owllinkserver");
		cmdLine.addArgument("-p");
		cmdLine.addArgument(Integer.toString(portNumber));
//		cmdLine.addArgument("-w");
//		cmdLine.addArgument("AUTO");

		/*
		 * +=Konclude.Logging.MinLoggingLevel=100 for stopping log info printed
		 * on the screen
		 */
		// cmdLine.addArgument("+=Konclude.Logging.MinLoggingLevel=100");

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		/*
		 * set timeout for 24 hours
		 */
		watchdog = new ExecuteWatchdog(72 * 60 * 60 * 1000);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setStreamHandler(pumpStreamHander);

		executor.setWatchdog(watchdog);
		try {
			executor.execute(cmdLine, resultHandler);

		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * pooling Konclude to see if it is ready
		 */
		while (true) {
			try {
				Thread.sleep(POOLING_INTERVAL);
				String result = stdout.toString();
				if (result.toLowerCase().contains(KONCLUDE_READY.toLowerCase())) {
					if (this.config.getLogInfos().contains(LogInfo.TIME_STAMP_FOR_EACH_STEP)) {
						logger.info(result);
					}
					break;
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		logger.info("Konclude has been started.");
	}

	/**
	 * simple code for killing process/run simple command
	 * @param command
	 * @return
	 */
	private static String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	private void stopKoncludeServer() {

		watchdog.destroyProcess();
		// TODO: it works so far but it could be improved.
		try {
			Thread.sleep(1000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		if (reasoner != null)
			reasoner.dispose();
		logger.info("Konclude server has been stoped.");
	}

	@Override
	protected OWLReasoner getOWLReasoner(OWLOntology ontology) {
		// long startTimeSettingUpKonclude = System.currentTimeMillis();
		startKoncludeServer();
		try {
			URL url = new URL("http://localhost:" + portNumber);

			OWLlinkReasonerConfiguration reasonerConfiguration = new OWLlinkReasonerConfiguration(url);
			logger.info("Connected to Konclude server at: " + url.toString());
			OWLlinkHTTPXMLReasonerFactory factory = new OWLlinkHTTPXMLReasonerFactory();
			// long endTimeSettingUpKonclude = System.currentTimeMillis();
			// this.overheadTimeToSetupReasoner = (endTimeSettingUpKonclude -
			// startTimeSettingUpKonclude) / 1000;
			return factory.createNonBufferingReasoner(ontology, reasonerConfiguration);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	protected void dispose() {
		reasoner.dispose();
		stopKoncludeServer();
	}

	// @Override
	// public long getOverheadTimeToSetupReasoner() {
	//
	// return this.overheadTimeToSetupReasoner;
	// }
}