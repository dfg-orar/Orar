package orar.dlreasoner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasoner;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;

public class KoncludeDLReasonerFileBased implements DLReasoner {
	private OWLOntology ontology;
	protected final Set<OWLClassAssertionAxiom> conceptAssertions;
	protected final Set<OWLObjectPropertyAssertionAxiom> roleAssertions;
	protected final Map<OWLNamedIndividual, Set<OWLNamedIndividual>> sameasAssertionAsMap;
	private OWLlinkHTTPXMLReasoner reasoner;
	private final int portNumber;
	private static Logger logger = Logger.getLogger(KoncludeDLReasonerFileBased.class);
	private ExecuteWatchdog watchdog;

	private final Configuration config;

	protected String savedOntologyFileName;
	// = "ontologyForKonclude.funcionalsyntax.owl";
	private final String koncludeConfigFileName;
	// = "/data/kien/benchmark/software/konclude-load-config.xml";
	protected boolean savingDone = false;
	private ByteArrayOutputStream stdout;
	private PumpStreamHandler pumpStreamHander;
	private final int POOLING_INTERVAL = 5; // miliseconds to check if Kondlude
											// is ready
	private final String KONCLUDE_READY = "Listening on port";
	DefaultExecuteResultHandler resultHandler;

	private long reasoningTimeInSecond;
	private final OWLDataFactory owlDataFactory;

	public KoncludeDLReasonerFileBased(OWLOntology ontology) {

		this.ontology = ontology;
		this.portNumber = 8080;// default

		this.config = Configuration.getInstance();

		this.savedOntologyFileName = config.getSavedOntologyFileName();
		this.koncludeConfigFileName = config.getKoncludeConfigFileName();

		this.conceptAssertions = new HashSet<OWLClassAssertionAxiom>();
		this.roleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		this.sameasAssertionAsMap = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();

		this.owlDataFactory = OWLManager.getOWLDataFactory();
		/*
		 * for running Konclude via command line
		 */
		this.stdout = new ByteArrayOutputStream();
		this.pumpStreamHander = new PumpStreamHandler(stdout);
		this.resultHandler = new DefaultExecuteResultHandler();
	}

	public KoncludeDLReasonerFileBased(OWLOntology ontology, int portNumber) {
		this.ontology = ontology;
		this.portNumber = portNumber;

		this.config = Configuration.getInstance();

		this.savedOntologyFileName = config.getSavedOntologyFileName();
		this.koncludeConfigFileName = config.getKoncludeConfigFileName();

		this.conceptAssertions = new HashSet<OWLClassAssertionAxiom>();
		this.roleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		this.sameasAssertionAsMap = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();

		this.owlDataFactory = OWLManager.getOWLDataFactory();

		/*
		 * for running Konclude via command line
		 */
		this.stdout = new ByteArrayOutputStream();
		this.pumpStreamHander = new PumpStreamHandler(stdout);
		this.resultHandler = new DefaultExecuteResultHandler();
	}

	protected void saveOntologyToFile() {
		if (!savingDone) {
			OWLOntologyManager manager = this.ontology.getOWLOntologyManager();
			logger.info("Saving ontology to a file ...");
			OWLFunctionalSyntaxOntologyFormat functionalFormat = new OWLFunctionalSyntaxOntologyFormat();
			// OWLXMLOntologyFormat functionalFormat = new
			// OWLXMLOntologyFormat();
			File file = new File(this.savedOntologyFileName);
			IRI iriDocument = IRI.create(file.toURI());
			try {
				long startSavingTime = System.currentTimeMillis();
				manager.saveOntology(ontology, functionalFormat, iriDocument);
				long endSavingTime = System.currentTimeMillis();
				long savingTimeInSeconds = (endSavingTime - startSavingTime) / 1000;
				if (config.getLogInfos().contains(LogInfo.LOADING_TIME)) {
					logger.info("Time for saving the ontology for Konclude (in seconds):" + savingTimeInSeconds);
					this.savingDone = true;
				}
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
		}
	}

	private void startKoncludeServer() {
		// CommandLine ulimit= new CommandLine("ulimit");
		// ulimit.addArgument("-m");
		// ulimit.addArgument("3500000");
		//
		/*
		 * Save ontology to file
		 */
		saveOntologyToFile();

		CommandLine cmdLine = new CommandLine(Configuration.getInstance().getKONCLUDE_BINARY_PATH());
		// "/Users/kien/konclude/Konclude0.6/Binaries/Konclude");
		// "/Users/kien/koncludemac/Konclude-static");

		cmdLine.addArgument("owllinkserver");
		cmdLine.addArgument("-p");
		cmdLine.addArgument(Integer.toString(portNumber));

		/*
		 * +=Konclude.Logging.MinLoggingLevel=100 for stopping log info printed
		 * on the screen
		 */
		// cmdLine.addArgument("+=Konclude.Logging.MinLoggingLevel=100");

		/*
		 * Force Konclude to load the ontology from a file
		 */
		cmdLine.addArgument("-c");
		// cmdLine.addArgument("src/main/resources/konclude-load-config.xml");
		cmdLine.addArgument(koncludeConfigFileName);

		// cmdLine.addArgument("Konclude.Calculation.Memory.AllocationLimitation=true");
		// cmdLine.addArgument("Konclude.Calculation.Memory.MaximumAllocationSize=4000000000");//
		// in
		// // bytes

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
					logger.info(result);
					break;
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		logger.info("Ontology has been saved to file and Konclude has been started.");
	}

	private void stopKoncludeServer() {
		watchdog.destroyProcess();

		stdout.reset();
		// logger.info("Is the process was killed?"+watchdog.killedProcess());
		// TODO: it works so far but it could be improved.

		try {
			Thread.sleep(3000); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		if (reasoner != null)
			reasoner.dispose();
		logger.info("Konclude server has been stoped.");

	}

	private void initReasoner() {
		URL url;
		try {
			url = new URL("http://localhost:" + portNumber);

			OWLlinkReasonerConfiguration reasonerConfiguration = new OWLlinkReasonerConfiguration(url);
			logger.info("Connected to Konclude server at: " + url.toString());
			OWLOntology emptyOntology;

			emptyOntology = OWLManager.createOWLOntologyManager().createOntology();

			OWLlinkHTTPXMLReasonerFactory factory = new OWLlinkHTTPXMLReasonerFactory();
			this.reasoner = (OWLlinkHTTPXMLReasoner) factory.createNonBufferingReasoner(emptyOntology,
					reasonerConfiguration);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void computeEntailments() {

		startKoncludeServer();
		logger.info("Computing entailments...");
		long startTime = System.currentTimeMillis();
		initReasoner();
		/*
		 * Compute concept assertions
		 */
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}

		/*
		 * Computing concept assertions. To limit number of call via OWLLink,
		 * instead of asking entailed concepts for each individual, we ask for
		 * instances of each concept name.
		 */
		Set<OWLClass> conceptNames = ontology.getClassesInSignature(true);
		conceptNames.remove(OWLManager.getOWLDataFactory().getOWLThing());

		/*
		 * 
		 */
		logger.info("computing all concept assertions");
		for (OWLClass concept : conceptNames) {
			Set<OWLNamedIndividual> instances = reasoner.getInstances(concept, false).getFlattened();
			for (OWLNamedIndividual ind : instances) {
				OWLClassAssertionAxiom entailedAssertion = this.owlDataFactory.getOWLClassAssertionAxiom(concept, ind);
				this.conceptAssertions.add(entailedAssertion);
			}
			stdout.reset();
		}
		logger.info("computing role assertions just for just some individuals....");
		// askRoleAssertionForJustOneIndividual();
		askRoleAssertionForJustSomeIndividuals();
		logger.info("computing samease assertions just for one individuals....");
		askOneQueryOfSameas();
		long endTime = System.currentTimeMillis();
		this.reasoningTimeInSecond = (endTime - startTime) / 1000;
		reasoner.dispose();
		stopKoncludeServer();
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTimeInSecond);
		}

	}

	public void dispose(OWLReasoner reasoner) {
		reasoner.dispose();
		this.ontology = null;

	}

	private void askOneQueryOfSameas() {
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}
		Set<OWLNamedIndividual> allIndividuals = ontology.getIndividualsInSignature(true);
		Iterator<OWLNamedIndividual> iterator = allIndividuals.iterator();
		OWLNamedIndividual anIndividual = null;
		if (iterator.hasNext()) {
			anIndividual = iterator.next();
		}
		if (anIndividual != null) {
			this.reasoner.getSameIndividuals(anIndividual);
		}
	}

	private void askRoleAssertionForJustSomeIndividuals() {
		Set<OWLObjectProperty> allRoles = this.ontology.getObjectPropertiesInSignature(true);
		allRoles.remove(OWLManager.getOWLDataFactory().getOWLTopObjectProperty());

		Set<OWLNamedIndividual> allIndividuals = this.ontology.getIndividualsInSignature(true);
		Iterator<OWLNamedIndividual> indIterator = allIndividuals.iterator();
		OWLNamedIndividual anIndividual = null;
		int count = 0;
		while (indIterator.hasNext()) {
			anIndividual = indIterator.next();
			count++;
			if (count > 2) {
				break;
			}
			for (OWLObjectProperty aRole : allRoles) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(anIndividual, aRole).getFlattened();
				for (OWLNamedIndividual eachObject : objects) {
					OWLObjectPropertyAssertionAxiom entailedRoleAssertion = this.owlDataFactory
							.getOWLObjectPropertyAssertionAxiom(aRole, anIndividual, eachObject);
					this.roleAssertions.add(entailedRoleAssertion);
				}
			}

		}

	}

	private void askRoleAssertionForJustOneIndividual() {
		Set<OWLObjectProperty> allRoles = ontology.getObjectPropertiesInSignature(true);
		allRoles.remove(OWLManager.getOWLDataFactory().getOWLTopObjectProperty());

		Set<OWLNamedIndividual> allIndividuals = ontology.getIndividualsInSignature(true);
		Iterator<OWLNamedIndividual> indIterator = allIndividuals.iterator();
		OWLNamedIndividual anIndividual = null;
		if (indIterator.hasNext()) {
			anIndividual = indIterator.next();
		}

		if (anIndividual != null) {

			for (OWLObjectProperty aRole : allRoles) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(anIndividual, aRole).getFlattened();
				for (OWLNamedIndividual eachObject : objects) {
					OWLObjectPropertyAssertionAxiom entailedRoleAssertion = this.owlDataFactory
							.getOWLObjectPropertyAssertionAxiom(aRole, anIndividual, eachObject);
					this.roleAssertions.add(entailedRoleAssertion);
				}
			}
		}

	}

	public long getReasoningTime() {

		return this.reasoningTimeInSecond;
	}

	@Override
	public Set<OWLClassAssertionAxiom> getEntailedConceptAssertions() {

		return this.conceptAssertions;
	}

	@Override
	public Set<OWLObjectPropertyAssertionAxiom> getEntailedRoleAssertions() {

		return this.roleAssertions;
	}

	@Override
	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getEntailedSameasAssertions() {

		return this.sameasAssertionAsMap;
	}

	@Override
	public void computeConceptAssertions() {
		startKoncludeServer();
		logger.info("Computing concept assertions...");
		long startTime = System.currentTimeMillis();
		initReasoner();
		/*
		 * Compute concept assertions
		 */
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}

		/*
		 * Computing concept assertions. To limit number of call via OWLLink,
		 * instead of asking entailed concepts for each individual, we ask for
		 * instances of each concept name.
		 */
		Set<OWLClass> conceptNames = ontology.getClassesInSignature(true);
		conceptNames.remove(OWLManager.getOWLDataFactory().getOWLThing());

		/*
		 * 
		 */
		logger.info("realizing...");
		reasoner.realise();
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
		// logger.info("getting instances for each concept ...");
		//
		// for (OWLClass concept : conceptNames) {
		// reasoner.getInstances(concept, false).getFlattened();
		//// Set<OWLNamedIndividual> instances = reasoner.getInstances(concept,
		// false).getFlattened();
		//// for (OWLNamedIndividual ind : instances) {
		//// OWLClassAssertionAxiom entailedAssertion =
		// this.owlDataFactory.getOWLClassAssertionAxiom(concept, ind);
		//// this.conceptAssertions.add(entailedAssertion);
		//// }
		//// stdout.reset();
		// }

		long endTime = System.currentTimeMillis();
		this.reasoningTimeInSecond = (endTime - startTime) / 1000;
		reasoner.dispose();
		stopKoncludeServer();
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTimeInSecond);
		}

	}

	@Override
	public void classifiesOntology() {
		logger.info("staring Konclude server...");
		startKoncludeServer();
		logger.info("initializing the reasoner...");
		long startTime = System.currentTimeMillis();
		initReasoner();
		/*
		 * Compute concept assertions
		 */
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}

		/*
		 * 
		 */
		logger.info("classifying the ontology...");

		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		long endTime = System.currentTimeMillis();
		this.reasoningTimeInSecond = (endTime - startTime) / 1000;
		reasoner.dispose();
		stopKoncludeServer();
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTimeInSecond);
		}

	}

	@Override
	public boolean isOntologyConsistent() {
		logger.info("staring Konclude server...");
		startKoncludeServer();
		logger.info("initializing the reasoner...");
		long startTime = System.currentTimeMillis();
		initReasoner();
		logger.info("checking consistency of the ontology...");
		if (!reasoner.isConsistent()) {
			return false;
		}
		logger.info("done!");

		long endTime = System.currentTimeMillis();
		this.reasoningTimeInSecond = (endTime - startTime) / 1000;
		reasoner.dispose();
		stopKoncludeServer();
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTimeInSecond);
		}
		return true;

	}

}
