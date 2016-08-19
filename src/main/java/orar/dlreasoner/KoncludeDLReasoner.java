package orar.dlreasoner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import orar.config.Configuration;

public class KoncludeDLReasoner extends DLReasonerTemplate {
	private int portNumber;
	private ExecuteWatchdog watchdog;
	private static Logger logger = Logger.getLogger(KoncludeDLReasoner.class);
	private ByteArrayOutputStream stdout;
	private PumpStreamHandler pumpStreamHander;
	private final int POOLING_INTERVAL = 5; // miliseconds to check if Kondlude
											// is ready
	private final String KONCLUDE_READY = "Listening on port";
	DefaultExecuteResultHandler resultHandler;

	public KoncludeDLReasoner(OWLOntology owlOntology) {
		super(owlOntology);
		this.portNumber = 8080;// default value
		/*
		 * for running Konclude via command line
		 */
		this.stdout = new ByteArrayOutputStream();
		this.pumpStreamHander = new PumpStreamHandler(stdout);
		this.resultHandler = new DefaultExecuteResultHandler();

	}

	public KoncludeDLReasoner(OWLOntology ontology, int portNumber) {
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
		CommandLine cmdLine = new CommandLine(Configuration.getInstance().getKONCLUDE_BINARY_PATH());

		cmdLine.addArgument("owllinkserver");
		cmdLine.addArgument("-p");
		cmdLine.addArgument(Integer.toString(portNumber));

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
					logger.info(result);
					break;
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		logger.info("Konclude has been started.");
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
		startKoncludeServer();
		try {
			URL url = new URL("http://localhost:" + portNumber);

			OWLlinkReasonerConfiguration reasonerConfiguration = new OWLlinkReasonerConfiguration(url);
			logger.info("Connected to Konclude server at: " + url.toString());
			OWLlinkHTTPXMLReasonerFactory factory = new OWLlinkHTTPXMLReasonerFactory();
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

	/**
	 * compute entailed role assertions
	 */
	@Override
	protected void computeEntailedRoleAssertions() {
		// askRoleAssertionForJustOneRole();
		// askRoleAssertionForJustOneIndividual();
		askRoleAssertionForJustSomeIndividuals();

	}

	private void askRoleAssertionForJustOneRole() {
		Set<OWLObjectProperty> allRoles = this.owlOntology.getObjectPropertiesInSignature(true);
		allRoles.remove(OWLManager.getOWLDataFactory().getOWLTopObjectProperty());
		Iterator<OWLObjectProperty> roleIterator = allRoles.iterator();
		OWLObjectProperty aRole = null;
		if (roleIterator.hasNext()) {
			aRole = roleIterator.next();
		}

		Set<OWLNamedIndividual> allIndividuals = this.owlOntology.getIndividualsInSignature(true);

		if (aRole != null) {

			for (OWLNamedIndividual eachInd : allIndividuals) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(eachInd, aRole).getFlattened();
				for (OWLNamedIndividual eachObject : objects) {
					OWLObjectPropertyAssertionAxiom entailedRoleAssertion = this.dataFactory
							.getOWLObjectPropertyAssertionAxiom(aRole, eachInd, eachObject);
					this.roleAssertions.add(entailedRoleAssertion);
				}
			}
		}

	}

	private void askRoleAssertionForJustOneIndividual() {
		Set<OWLObjectProperty> allRoles = this.owlOntology.getObjectPropertiesInSignature(true);
		allRoles.remove(OWLManager.getOWLDataFactory().getOWLTopObjectProperty());

		Set<OWLNamedIndividual> allIndividuals = this.owlOntology.getIndividualsInSignature(true);
		Iterator<OWLNamedIndividual> indIterator = allIndividuals.iterator();
		OWLNamedIndividual anIndividual = null;
		if (indIterator.hasNext()) {
			anIndividual = indIterator.next();
		}

		if (anIndividual != null) {
			for (OWLObjectProperty aRole : allRoles) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(anIndividual, aRole).getFlattened();
				for (OWLNamedIndividual eachObject : objects) {
					OWLObjectPropertyAssertionAxiom entailedRoleAssertion = this.dataFactory
							.getOWLObjectPropertyAssertionAxiom(aRole, anIndividual, eachObject);
					this.roleAssertions.add(entailedRoleAssertion);
				}
			}
		}
	}

	private void askRoleAssertionForJustSomeIndividuals() {
		Set<OWLObjectProperty> allRoles = this.owlOntology.getObjectPropertiesInSignature(true);
		allRoles.remove(OWLManager.getOWLDataFactory().getOWLTopObjectProperty());

		Set<OWLNamedIndividual> allIndividuals = this.owlOntology.getIndividualsInSignature(true);
		Iterator<OWLNamedIndividual> indIterator = allIndividuals.iterator();
		OWLNamedIndividual anIndividual = null;
		int count = 0;
		while (indIterator.hasNext()) {
			anIndividual = indIterator.next();
			count++;
			if (count > 300) {
				break;
			}
			for (OWLObjectProperty aRole : allRoles) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(anIndividual, aRole).getFlattened();
				for (OWLNamedIndividual eachObject : objects) {
					OWLObjectPropertyAssertionAxiom entailedRoleAssertion = this.dataFactory
							.getOWLObjectPropertyAssertionAxiom(aRole, anIndividual, eachObject);
					this.roleAssertions.add(entailedRoleAssertion);
				}
			}

		}

	}

	protected void computeEntailedSameasAssertions() {
		askOneQueryOfSameas();

	}

	private void askOneQueryOfSameas() {
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}
		Set<OWLNamedIndividual> allIndividuals = this.owlOntology.getIndividualsInSignature(true);
		Iterator<OWLNamedIndividual> iterator = allIndividuals.iterator();
		OWLNamedIndividual anIndividual = null;
		if (iterator.hasNext()) {
			anIndividual = iterator.next();
		}
		if (anIndividual != null) {
			this.reasoner.getSameIndividuals(anIndividual);
		}
	}
}
// private void getRoleAssertions(){
// Set<OWLObjectProperty> allRoles =
// this.owlOntology.getObjectPropertiesInSignature(true);
// allRoles.remove(OWLManager.getOWLDataFactory().getOWLTopObjectProperty());
// OWLlinkHTTPXMLReasoner owlLinkReasone = (OWLlinkHTTPXMLReasoner)
// this.reasoner;
// Set<OWLNamedIndividual> allIndividuals =
// this.owlOntology.getIndividualsInSignature(true);
//
// for( OWLObjectProperty role:allRoles){
// /*
// * create requests for all individuals
// */
// GetFlattenedObjectPropertyTargets[] manyRequests = new
// GetFlattenedObjectPropertyTargets[allObjects.size()];
// int index = 0;
// for (OWLNamedIndividual eachObject : allIndividuals) {
//
// GetFlattenedObjectPropertyTargets request = new
// GetFlattenedObjectPropertyTargets(iriKB, eachObject,
// invRole);
// manyRequests[index] = request;
// index++;
// }
// /*
// * get answers
// */
// ResponseMessage responseMessage = owllinkReasoner.answer(manyRequests);
// for (int i = 0; i < allObjects.size(); i++) {
// SetOfIndividuals responseForEachIndividual = (SetOfIndividuals)
// responseMessage.get(i);
//
// OWLNamedIndividual object =
// manyRequests[i].getOWLIndividual().asOWLNamedIndividual();
//
// for (OWLIndividual ind : responseForEachIndividual) {
// OWLNamedIndividual subject = ind.asOWLNamedIndividual();
// this.roleAssertionBox.addRoleAssertion(subject, role, object);
//
// }
// }
//
// }
//
// }
