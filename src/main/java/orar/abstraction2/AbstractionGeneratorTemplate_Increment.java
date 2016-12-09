package orar.abstraction2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.AbstractionGeneratorTemplate;
import orar.abstraction.PairOfSubjectAndObject;
import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.DataForTransferingEntailments_Increment;
import orar.data.MetaDataOfOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;
import orar.util.PrintingHelper;

/**
 * Template for generating abstraction from individuals types.
 * 
 * @author kien
 *
 */
public abstract class AbstractionGeneratorTemplate_Increment extends AbstractionGeneratorTemplate {
	private Logger logger;

	public AbstractionGeneratorTemplate_Increment(OrarOntology2 orarOntology,
			Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		super(orarOntology, typeMap2Individuals);
		this.sharedMap = DataForTransferingEntailments_Increment.getInstance();
		this.logger = Logger.getLogger(AbstractionGeneratorTemplate_Increment.class);
	}

	@Override
	protected Set<OWLAxiom> generateAssertions(IndividualType type) {
		Set<OWLAxiom> abstractAssertions = new HashSet<OWLAxiom>();
		/*
		 * create x
		 */
		OWLNamedIndividual x = abstractDataFactory.createAbstractIndividualX();
		/*
		 * map X to type;
		 */
		Set<Integer> originalIndsForThisType = this.typeMap2Individuals.get(type);
		sharedMap.getMap_XAbstractIndiv_2_OriginalIndivs().put(x, originalIndsForThisType);
		sharedMap.getMap_XAbstractIndiv_2_Type().put(x, type);

		/*
		 * Mark x if x has functional succ-role
		 */
		markXHavingFunctionalRole(x, type);
		/*
		 * create abstract class assertions for x
		 */
		abstractAssertions.addAll(getConceptAssertions(x, type));

		/*
		 * create prerole assertions for x
		 */
		abstractAssertions.addAll(getPredecessorRoleAssertions(x, type));
		/*
		 * create succRole assertions for x
		 */
		abstractAssertions.addAll(getSuccessorRoleAssertions(x, type));

		/*
		 * create concept assertion for conept-type
		 */
		abstractAssertions.addAll(getConceptAssertionsForConceptType(type));

		return abstractAssertions;
	}

	protected Set<Integer> getOriginalIndivsForX(OWLNamedIndividual x) {
		IndividualType type = sharedMap.getMap_XAbstractIndiv_2_Type().get(x);
		Set<Integer> originalIndivs = sharedMap.getMapType_2_Individuals().get(type);
		if (Configuration.getInstance().getDebuglevels().contains(DebugLevel.ABSTRACTION_CREATION)) {
			logger.info("***Debug on getting original from XAbstract***");
			logger.info("Map X-->Type:");
			PrintingHelper.printMap(this.sharedMap.getMap_XAbstractIndiv_2_Type());
			logger.info("Map Type--->OriginalIndivs:");
			PrintingHelper.printMap(this.sharedMap.getMapType_2_Individuals());
			logger.info("original individuals for " + x);
			logger.info(originalIndivs);
		}
		return originalIndivs;
	}

}
