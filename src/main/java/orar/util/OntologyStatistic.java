package orar.util;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.config.StatisticVocabulary;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

public class OntologyStatistic {
	private static final Logger logger = Logger.getLogger(OntologyStatistic.class);

	public static void printInputOrarOntologyInfo(OrarOntology internalOntology) {
		/*
		 * TBox
		 * 
		 */
		logger.info("===Ontology===");
		logger.info(StatisticVocabulary.NUMBER_OF_TBOX_AXIOMS + internalOntology.getTBoxAxioms().size());
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_CONCEPTNAMES
				+ internalOntology.getConceptNamesInSignature().size());
		logger.info(
				StatisticVocabulary.NUMBER_OF_INPUT_ROLEASSERTIONS + internalOntology.getRoleNamesInSignature().size());

		/*
		 * ABox
		 * 
		 */
		logger.info(
				StatisticVocabulary.NUMBER_OF_INPUT_INDIVIDUALS + internalOntology.getIndividualsInSignature().size());
		/*
		 * Note: Some test cases demonstrated that OWLAPI might count it
		 * wrongly. An alternative way is to get all class/role assertions and
		 * get size
		 */
		int numberOfConceptAsesrtions = internalOntology.getNumberOfInputConceptAssertions();
		int numberOfRoleAsesrtions = internalOntology.getNumberOfInputRoleAssertions();
		int numberOfAssertions = numberOfConceptAsesrtions + numberOfRoleAsesrtions;
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_CONCEPTASSERTIONS + numberOfConceptAsesrtions);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ROLEASSERTIONS + numberOfRoleAsesrtions);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ASSERTIONS + numberOfAssertions);

	}

	public static void printInputABoxOrarOntologyInfo(OrarOntology2 internalOntology) {
		/*
		 * ABox
		 * 
		 */
		logger.info(
				StatisticVocabulary.NUMBER_OF_INPUT_INDIVIDUALS + internalOntology.getIndividualsInSignature().size());
		/*
		 * Note: Some test cases demonstrated that OWLAPI might count it
		 * wrongly. An alternative way is to get all class/role assertions and
		 * get size
		 */
		int numberOfConceptAsesrtions = internalOntology.getNumberOfInputConceptAssertions();
		int numberOfRoleAsesrtions = internalOntology.getNumberOfInputRoleAssertions();
		int numberOfAssertions = numberOfConceptAsesrtions + numberOfRoleAsesrtions;
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_CONCEPTASSERTIONS + numberOfConceptAsesrtions);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ROLEASSERTIONS + numberOfRoleAsesrtions);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ASSERTIONS + numberOfAssertions);

	}

	public static void printOWLOntologyInfo(OWLOntology owlOntology) {
		// logger.info("===Ontology===");
		int tboxsize = owlOntology.getTBoxAxioms(true).size();
		int rboxSize = owlOntology.getRBoxAxioms(true).size();
		int tboxAndRboxSize = tboxsize + rboxSize;
		logger.info(StatisticVocabulary.NUMBER_OF_TBOX_AXIOMS + tboxAndRboxSize);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_CONCEPTNAMES + owlOntology.getClassesInSignature(true).size());
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ROLENAMES
				+ owlOntology.getObjectPropertiesInSignature(true).size());

		logger.info(
				StatisticVocabulary.NUMBER_OF_INPUT_INDIVIDUALS + owlOntology.getIndividualsInSignature(true).size());
		/*
		 * Note: Some test cases demonstrated that OWLAPI might count it
		 * wrongly. An alternative way is to get all class/role assertions and
		 * get size
		 */
		int numberOfConceptAsesrtions = owlOntology.getAxiomCount(AxiomType.CLASS_ASSERTION, true);
		int numberOfRoleAsesrtions = owlOntology.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION, true);
		int numberOfAssertions = numberOfConceptAsesrtions + numberOfRoleAsesrtions;
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_CONCEPTASSERTIONS + numberOfConceptAsesrtions);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ROLEASSERTIONS + numberOfRoleAsesrtions);
		logger.info(StatisticVocabulary.NUMBER_OF_INPUT_ASSERTIONS + numberOfAssertions);

		//
		// long numberOfCA = owlOntology.getAxioms(AxiomType.CLASS_ASSERTION,
		// true).size();
		// logger.info("Number of concept assertions:" + numberOfCA);
		// long numberOfRA =
		// owlOntology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION,
		// true).size();
		// logger.info("Number of role assertions:" + numberOfRA);
		// long totalOfAssertions = numberOfCA + numberOfRA;
		// logger.info("Number of concept assertions + role asesrtions:" +
		// totalOfAssertions);
	}
}
