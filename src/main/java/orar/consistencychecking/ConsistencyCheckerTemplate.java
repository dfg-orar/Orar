package orar.consistencychecking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.BasicTypeComputor;
import orar.abstraction.TypeComputor;
import orar.abstraction.HornSHIF.HornSHIF_AbstractionGenerator;
import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.config.LogInfo;
import orar.config.StatisticVocabulary;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.MetaDataOfOntology;
import orar.innerconsistencychecking.InnerConsistencyChecker;
import orar.innerreasoner.InnerReasoner;
import orar.materializer.DLLite_MaterializerTemplate;
import orar.modeling.ontology.OrarOntology;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.AssertionTransporter;
import orar.refinement.assertiontransferring.HornSHIF.HornSHIF_AssertionTransporter;
import orar.ruleengine.RuleEngine;
import orar.ruleengine.SemiNaiveRuleEngine;
import orar.type.BasicIndividualTypeFactory_UsingWeakHashMap;
import orar.type.IndividualType;
import orar.util.PrintingHelper;

public abstract class ConsistencyCheckerTemplate implements ConsistencyChecker {

	// input & output
	protected final OrarOntology normalizedORAROntology;

	private long reasoningTimeInSeconds;
	protected final Configuration config;
	// logging
	private static final Logger logger = Logger.getLogger(DLLite_MaterializerTemplate.class);
	// shared data
	protected final DataForTransferingEntailments dataForTransferringEntailments;
	protected final MetaDataOfOntology metaDataOfOntology;
	protected final AbstractDataFactory abstractDataFactory = AbstractDataFactory.getInstance();
	// fields that vary in implementations
	protected AbstractionGenerator abstractionGenerator;
	// other fields for the algorithm
	protected final TypeComputor typeComputor;
	protected Set<OWLOntology> abstractOntologies;
	protected final RuleEngine ruleEngine;
	private boolean isConsistent;

	public ConsistencyCheckerTemplate(OrarOntology normalizedOrarOntology) {
		// input & output
		this.normalizedORAROntology = normalizedOrarOntology;

		this.reasoningTimeInSeconds = -1;
		this.config = Configuration.getInstance();
		// shared data
		this.dataForTransferringEntailments = DataForTransferingEntailments.getInstance();
		this.metaDataOfOntology = MetaDataOfOntology.getInstance();

		// other fields
		this.abstractOntologies = new HashSet<OWLOntology>();
		this.ruleEngine = new SemiNaiveRuleEngine(normalizedOrarOntology);
		this.typeComputor = new BasicTypeComputor();
		this.isConsistent = true;
	}

	private void checkConsistency() {
		long startTime = System.currentTimeMillis();

		this.dataForTransferringEntailments.clear();
		AbstractDataFactory.getInstance().clear();
		BasicIndividualTypeFactory_UsingWeakHashMap.getInstance().clear();

		/*
		 * (3). Compute types
		 */
		logger.info("Computing types...");
		Map<IndividualType, Set<OWLNamedIndividual>> typeMap2Individuals = this.typeComputor
				.computeTypes(this.normalizedORAROntology);

		/*
		 * logging
		 * 
		 */
		if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
			logger.info(StatisticVocabulary.NUMBER_OF_TYPES + typeMap2Individuals.size());
		}

		if (config.getDebuglevels().contains(DebugLevel.TYPE_COMPUTING)) {
			logger.info("***DEBUG*** Types:");
			PrintingHelper.printMap(typeMap2Individuals);
		}

		/*
		 * (4). Generate the abstractions
		 */
		logger.info("Generating abstractions ...");
		List<OWLOntology> abstractions = getAbstractions(typeMap2Individuals);
		logger.info("Info:Number of abstraction ontolog(ies):" + abstractions.size());
		// logging debug
		if (config.getDebuglevels().contains(DebugLevel.ABSTRACTION_CREATION)) {
			logger.info("*** DEBUG*** Number of abstraction ontologies: " + abstractions.size());
			for (OWLOntology abs : abstractions) {
				logger.info("=== Abstraction ontolog(ies):====");
				PrintingHelper.printSet(abs.getAxioms());
			}
		}

		// logging statistic
		if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
			// logging abstract individuals
			logger.info(StatisticVocabulary.NUMBER_OF_X + this.abstractDataFactory.getxCounter());
			logger.info(StatisticVocabulary.NUMBER_OF_U + this.abstractDataFactory.getuCounter());
			long yandz = this.abstractDataFactory.getyCounter() + this.abstractDataFactory.getzCounter();
			logger.info(StatisticVocabulary.NUMBER_OF_YZ + yandz);

			long numberOfAbstractIndividuals = this.abstractDataFactory.getxCounter()
					+ this.abstractDataFactory.getuCounter() + yandz;
			logger.info(StatisticVocabulary.NUMBER_OF_ABSTRACT_INDIVIDUALS + numberOfAbstractIndividuals);

			// logging size related info
			int abstractConceptAssertions = 0;
			int abstractRoleAssertions = 0;
			int abstractAssertions = 0;

			for (OWLOntology abs : abstractions) {
				abstractConceptAssertions += abs.getAxiomCount(AxiomType.CLASS_ASSERTION, true);
				abstractRoleAssertions += abs.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION, true);
			}
			abstractAssertions = abstractConceptAssertions + abstractRoleAssertions;
			logger.info(StatisticVocabulary.NUMBER_OF_ABSTRACT_CONCEPTASSERTIONS + abstractConceptAssertions);
			logger.info(StatisticVocabulary.NUMBER_OF_ABSTRACT_ROLEASSERTIONS + abstractRoleAssertions);
			logger.info(StatisticVocabulary.NUMBER_OF_ABSTRACT_ASSERTIONS + abstractAssertions);
		}

		/*
		 * (5). Check consistency of the abstractions
		 */
		logger.info("Checking consistency of the abstractions ...");
		for (OWLOntology abstraction : abstractions) {
			if (config.getDebuglevels().contains(DebugLevel.REASONING_ABSTRACTONTOLOGY)) {
				logger.info("***DEBUG*** Abstraction ontology:");
				PrintingHelper.printSet(abstraction.getAxioms());
			}

			logger.info("Info:Size of the (splitted) abstract ontology: " + abstraction.getAxiomCount());
			InnerConsistencyChecker innerConsistencyChecker = getInnerConsistencyChecker(abstraction);
			boolean splitedAbstractionConsistent = innerConsistencyChecker.isConsistent();
			if (!splitedAbstractionConsistent) {
				this.isConsistent = false;
				break;
			}
		}

		/*
		 * get reasoning time
		 * 
		 */
		long endTime = System.currentTimeMillis();
		this.reasoningTimeInSeconds = (endTime - startTime) / 1000;
		/*
		 * logging
		 */
		if (config.getLogInfos().contains(LogInfo.REASONING_TIME)) {
			logger.info(StatisticVocabulary.TIME_REASONING_USING_ABSRTACTION + this.reasoningTimeInSeconds);
		}

		if (config.getLogInfos().contains(LogInfo.STATISTIC)) {
			int numberOfMaterializedConceptAssertions = this.normalizedORAROntology.getNumberOfConceptAssertions();
			int numberOfMaterializedRoleAssertions = this.normalizedORAROntology.getNumberOfRoleAssertions();
			int numberOfMaterializedAssertions = numberOfMaterializedConceptAssertions
					+ numberOfMaterializedRoleAssertions;
			logger.info(StatisticVocabulary.NUMBER_OF_MATERIALIZED_CONCEPTASSERTIONS
					+ numberOfMaterializedConceptAssertions);
			logger.info(StatisticVocabulary.NUMBER_OF_MATERIALIZED_ROLEASSERTIONS + numberOfMaterializedRoleAssertions);
			logger.info(StatisticVocabulary.NUMBER_OF_MATERIALIZED_ASSERTIONS + numberOfMaterializedAssertions);
		}

	}

	protected abstract InnerConsistencyChecker getInnerConsistencyChecker(OWLOntology abstraction);

	protected List<OWLOntology> getAbstractions(Map<IndividualType, Set<OWLNamedIndividual>> typeMap2Individuals) {
		AbstractionGenerator abstractionGenerator = new HornSHIF_AbstractionGenerator(normalizedORAROntology,
				typeMap2Individuals);
		// AbstractionGenerator abstractionGenerator = new
		// DLLiteR_AbstractionGenerator(normalizedORAROntology,
		// typeMap2Individuals);
		List<OWLOntology> abstractions = new ArrayList<OWLOntology>();
		abstractions.add(abstractionGenerator.getAbstractOntology());
		return abstractions;
	}

	protected AssertionTransporter getAssertionTransporter(
			Map<OWLNamedIndividual, Set<OWLClass>> entailedAbstractConceptAssertions,
			AbstractRoleAssertionBox entailedAbstractRoleAssertion,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> entailedSameasMap) {
		AssertionTransporter assertionTransporter = new HornSHIF_AssertionTransporter(normalizedORAROntology,
				entailedAbstractConceptAssertions, entailedAbstractRoleAssertion);
		return assertionTransporter;
	}

	protected abstract InnerReasoner getInnerReasoner(OWLOntology abstraction);

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConsistent() {
		checkConsistency();
		return this.isConsistent;
	}

	@Override
	public Set<Set<OWLAxiom>> getExplanations(int maxNumberOfExplanations) {
		return null;
	}

	@Override
	public long getReasoningTimeInSeconds() {
		return this.reasoningTimeInSeconds;
	}

}
