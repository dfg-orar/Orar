package orar.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class OntologyInfo {
	private static Logger logger = Logger.getLogger(OntologyInfo.class);

	public static void printSize(OWLOntology ont) {
		logger.info("Number of individuals: "
				+ ont.getIndividualsInSignature(true).size());

		logger.info("TBox size: " + ont.getTBoxAxioms(true).size());
		logger.info("RBox size: " + ont.getRBoxAxioms(true).size());

		int numberOfCA = ont.getAxiomCount(AxiomType.CLASS_ASSERTION, true);
		int numberOfPA = ont.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION,
				true);
		int totalAssertions = numberOfCA + numberOfPA;
		logger.info("Number of Class Assertions and Object Property Assertions: "
				+ totalAssertions);
		logger.info("Number of Class Assertions: " + numberOfCA);
		logger.info("Number of Object Property Assertions: " + numberOfPA);

		logger.info("SameIndividuals assertions: "
				+ ont.getAxiomCount(AxiomType.SAME_INDIVIDUAL, true));
		logger.info("DifferentIndividuals assertions: "
				+ ont.getAxiomCount(AxiomType.DIFFERENT_INDIVIDUALS, true));
	}

	public static void printABoxInfo(OWLOntology ont) {
		logger.info("Number of individuals: "
				+ ont.getIndividualsInSignature(true).size());

		int numberOfCA = ont.getAxiomCount(AxiomType.CLASS_ASSERTION, true);
		int numberOfPA = ont.getAxiomCount(AxiomType.OBJECT_PROPERTY_ASSERTION,
				true);
		logger.info("Number of Class Assertions: " + numberOfCA);
		logger.info("Number of Object Property Assertions: " + numberOfPA);
		int totalAssertions = numberOfCA + numberOfPA;
		logger.info("Total number of Class Assertions and Object Property Assertions: "
				+ totalAssertions);
	}

	public static void printABoxInfo(Set<OWLOntology> onts) {

		long totalIndividuals = 0;
		long totalClassAssertions = 0;
		long totalPropertyAssertions = 0;

		for (OWLOntology ont : onts) {
			totalIndividuals += ont.getIndividualsInSignature(true).size();
			totalClassAssertions += ont.getAxiomCount(
					AxiomType.CLASS_ASSERTION, true);
			totalPropertyAssertions += ont.getAxiomCount(
					AxiomType.OBJECT_PROPERTY_ASSERTION, true);
		}

		logger.info("Total number of individuals: " + totalIndividuals);

		logger.info("Total number of Class Assertions: " + totalClassAssertions);
		logger.info("Total number of Object Property Assertions: "
				+ totalPropertyAssertions);

		long totalAssertions = totalClassAssertions + totalPropertyAssertions;
		logger.info("Total number of both Class Assertions and Object Property Assertions: "
				+ totalAssertions);
	}

	public static void printAssertionsPerRole(OWLOntology ontology) {
		Set<OWLObjectProperty> roles = ontology
				.getObjectPropertiesInSignature(true);
		Map<OWLObjectProperty, Integer> roleFrequency = new HashMap<OWLObjectProperty, Integer>();
		int maxAssertions = 0;
		OWLObjectProperty roleWithMaxAssertions = null;
		Set<OWLObjectPropertyAssertionAxiom> roleAssertions = ontology
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, true);

		for (OWLObjectPropertyAssertionAxiom pa : roleAssertions) {
			OWLObjectProperty p = pa.getProperty().asOWLObjectProperty();
			Integer count = roleFrequency.get(p);
			if (count == null)
				count = 0;
			count++;
			roleFrequency.put(p, count);
		}

		Iterator<Entry<OWLObjectProperty, Integer>> iterator = roleFrequency
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<OWLObjectProperty, Integer> entry = iterator.next();
			logger.info(entry);
			if (maxAssertions < entry.getValue()) {
				maxAssertions = entry.getValue();
				roleWithMaxAssertions = entry.getKey();
			}
		}

		logger.info("Role with biggest number of assertions:"
				+ roleWithMaxAssertions);
		logger.info("The biggest number of assertions:" + maxAssertions);
		logger.info("Total number of roles assertions: "
				+ roleAssertions.size());
	}

	public static void printAssertionPerConcept(OWLOntology ontology) {
		// Set<OWLClass> concepts = ontology.getClassesInSignature(true);

		Map<OWLClass, Integer> conceptFrequency = new HashMap<OWLClass, Integer>();
		Set<OWLClassAssertionAxiom> classAssertons = ontology.getAxioms(
				AxiomType.CLASS_ASSERTION, true);
		for (OWLClassAssertionAxiom ca : classAssertons) {
			OWLClass concept = ca.getClassExpression().asOWLClass();
			Integer count = conceptFrequency.get(concept);
			if (count == null)
				count = 0;
			count++;
			conceptFrequency.put(concept, count);
		}

		Iterator<Entry<OWLClass, Integer>> iterator = conceptFrequency
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<OWLClass, Integer> entry = iterator.next();
			logger.info(entry);

		}
		logger.info("Total number of class assertions:" + classAssertons.size());
	}

	public static void printTBoxAxioms(OWLOntology ontology) {
		Set<OWLAxiom> axioms = ontology.getTBoxAxioms(true);
		axioms.addAll(ontology.getRBoxAxioms(true));
		logger.info("TBox axioms:");
		for (OWLAxiom ax : axioms) {
			logger.info(ax);
		}

	}

	public static void printABoxAxioms(OWLOntology ontology) {
		Set<OWLAxiom> axioms = ontology.getABoxAxioms(true);
		logger.info("ABox axioms:");
		for (OWLAxiom ax : axioms) {
			logger.info(ax);
		}

	}

	public static void printDisjointAxioms(OWLOntology ontology) {
		Set<OWLDisjointClassesAxiom> axioms = ontology.getAxioms(AxiomType.DISJOINT_CLASSES);
		logger.info("disjoint axioms:");
		for (OWLAxiom ax : axioms) {
			logger.info(ax);
		}

	}
	
}
