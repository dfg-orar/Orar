package orar.dlreasoner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;

public abstract class DLReasonerTemplate implements DLReasoner {
	private Logger logger = Logger.getLogger(DLReasonerTemplate.class);
	protected final Set<OWLClassAssertionAxiom> conceptAssertions;
	protected final Set<OWLObjectPropertyAssertionAxiom> roleAssertions;
	protected final Map<OWLNamedIndividual, Set<OWLNamedIndividual>> sameasAssertionAsMap;
	protected OWLOntology owlOntology;
	protected long reasoningTime;
	protected OWLReasoner reasoner;
	protected OWLDataFactory dataFactory;
	boolean entailmentComputed;
	private final Configuration config = Configuration.getInstance();

	public DLReasonerTemplate(OWLOntology owlOntology) {
		this.conceptAssertions = new HashSet<OWLClassAssertionAxiom>();
		this.roleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		this.sameasAssertionAsMap = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();
		this.owlOntology = owlOntology;
		this.dataFactory = OWLManager.getOWLDataFactory();
		this.entailmentComputed = false;
	}

	@Override
	public Set<OWLClassAssertionAxiom> getEntailedConceptAssertions() {
		if (!this.entailmentComputed) {
			computeEntailments();
		}
		return this.conceptAssertions;
	}

	@Override
	public Set<OWLObjectPropertyAssertionAxiom> getEntailedRoleAssertions() {
		if (!this.entailmentComputed) {
			computeEntailments();
		}
		return this.roleAssertions;
	}

	@Override
	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getEntailedSameasAssertions() {
		if (!this.entailmentComputed) {
			computeEntailments();
		}
		return this.sameasAssertionAsMap;
	}

	@Override
	public long getReasoningTime() {

		return this.reasoningTime;
	}

	/**
	 * Initialize the reasoner with {@code ontology} in the root ontology
	 * 
	 * @param ontology
	 */
	protected abstract OWLReasoner getOWLReasoner(OWLOntology ontology);

	@Override
	public void computeConceptAssertions() {
		logger.info("computing concept assertions...");
		long startTime = System.currentTimeMillis();
		logger.info("getting the reasoner...");
		this.reasoner = getOWLReasoner(owlOntology);
		// logger.info("***DEBUG*** 1");
		logger.info("precomputing class assertions...");
		this.reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);

		// logger.info("***DEBUG*** 2");
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}
		// logger.info("***DEBUG*** 3");

		// computeEntailedConceptAssertions();

		this.entailmentComputed = true;
		long endTime = System.currentTimeMillis();
		dispose();

		this.reasoningTime = (endTime - startTime) / 1000; // get seconds
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTime);
		}
	}

	@Override
	public void computeEntailments() {

		this.reasoner = getOWLReasoner(owlOntology);
		// logger.info("***DEBUG*** 1");
		long startTime = System.currentTimeMillis();

		this.reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS,
				InferenceType.SAME_INDIVIDUAL);
		// logger.info("***DEBUG*** 2");
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}
		// logger.info("***DEBUG*** 3");
		logger.info("computing concept assertions...");
		computeEntailedConceptAssertions();
		logger.info("computing role assertions...");
		computeEntailedRoleAssertions();
		logger.info("computing sameas assertions...");
		computeEntailedSameasAssertions();
		this.entailmentComputed = true;
		dispose();
		long endTime = System.currentTimeMillis();
		logger.info("computing entailments...finished!");
		this.reasoningTime = (endTime - startTime) / 1000; // get seconds
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTime);
		}
	}

	/**
	 * compute entailed same individuals
	 */
	protected void computeEntailedSameasAssertions() {
		Set<OWLNamedIndividual> allIndividuals = this.owlOntology.getIndividualsInSignature(true);
		// logger.info("***DEBUG*** individuals in signatures:"+allIndividuals);
		for (OWLNamedIndividual indiv : allIndividuals) {
			Set<OWLNamedIndividual> equivalentIndividuals = reasoner.getSameIndividuals(indiv).getEntities();

			this.sameasAssertionAsMap.put(indiv, equivalentIndividuals);
		}

	}

	/**
	 * compute entailed role assertions
	 */
	protected void computeEntailedRoleAssertions() {
		Set<OWLNamedIndividual> allIndividuals = this.owlOntology.getIndividualsInSignature(true);
		Set<OWLObjectProperty> allRoles = this.owlOntology.getObjectPropertiesInSignature(true);
		allRoles.remove(this.dataFactory.getOWLTopObjectProperty());
		for (OWLNamedIndividual individual : allIndividuals) {
			for (OWLObjectProperty role : allRoles) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(individual, role).getFlattened();
				for (OWLNamedIndividual object : objects) {
					OWLObjectPropertyAssertionAxiom newRoleAssertion = this.dataFactory
							.getOWLObjectPropertyAssertionAxiom(role, individual, object);
					this.roleAssertions.add(newRoleAssertion);
				}
			}
		}

	}

	/**
	 * compute entailed concept assertions
	 */
	private void computeEntailedConceptAssertions() {
		Set<OWLClass> allConceptsNames = this.owlOntology.getClassesInSignature(true);
		allConceptsNames.remove(OWLManager.getOWLDataFactory().getOWLThing());
		for (OWLClass eachConceptName : allConceptsNames) {
			Set<OWLNamedIndividual> instances = this.reasoner.getInstances(eachConceptName, false).getFlattened();
			for (OWLNamedIndividual eachInstance : instances) {
				OWLClassAssertionAxiom newConceptAssertion = this.dataFactory.getOWLClassAssertionAxiom(eachConceptName,
						eachInstance);
				this.conceptAssertions.add(newConceptAssertion);

			}

		}
	}

	// private void owlapi(){
	// this.reasoner.getSuperObjectProperties(pe, direct)
	// }

	/**
	 * release resource from the reasoner. Stop Konclude server (in case of
	 * using Konclude via OWLLink)
	 */
	protected abstract void dispose();

	public void classifiesOntology() {
		logger.info("start classifying the ontology...");
		long startTime = System.currentTimeMillis();
		logger.info("getting the reasoner...");
		this.reasoner = getOWLReasoner(owlOntology);
		// logger.info("***DEBUG*** 2");
		if (!reasoner.isConsistent()) {
			logger.error("Ontology inconsistent!");
		}
		// logger.info("***DEBUG*** 1");
		logger.info("precomputing class hierarchy...");
		this.reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		logger.info("done!");
		// logger.info("***DEBUG*** 3");

		// computeEntailedConceptAssertions();

		this.entailmentComputed = true;
		long endTime = System.currentTimeMillis();
		dispose();

		this.reasoningTime = (endTime - startTime) / 1000; // get seconds
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTime);
		}
	}

	public boolean isOntologyConsistent() {
		long startTime = System.currentTimeMillis();
		logger.info("Getting the reasoner...");
		this.reasoner = getOWLReasoner(owlOntology);
		logger.info("Checking consistency...");
		if (!reasoner.isConsistent()) {
			return false;
		}
		logger.info("Done! Checking consistency");
		this.entailmentComputed = true;
		long endTime = System.currentTimeMillis();

		this.reasoningTime = (endTime - startTime) / 1000; // get seconds
		if (this.config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_DLREASONER + this.reasoningTime);
		}
		dispose();
		return true;
	}
}
