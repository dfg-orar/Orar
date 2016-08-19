package orar.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class NormalizationDataFactory {

	private static NormalizationDataFactory instance;
	private OWLDataFactory owlDataFactory;
	private final String CONCEPT_PREFIX = "http://www.af#NormTBoxC";
	private final String NOMINAL_CONCEPT_PREFIX = "http://www.af#NormNominalC";
	private final String TRANSITIVITY_CONCEPT_PREFIX = "http://www.af#NormTransC";

	private long count;
	private final Set<OWLClass> conceptsByNormalization;

	private final Map<OWLNamedIndividual, OWLClass> nominal2ConceptMap;

	private NormalizationDataFactory() {
		owlDataFactory = OWLManager.getOWLDataFactory();
		conceptsByNormalization = new HashSet<OWLClass>();
		nominal2ConceptMap = new HashMap<OWLNamedIndividual, OWLClass>();

		count = 0;
	}

	public static NormalizationDataFactory getInstance() {
		if (instance == null) {
			instance = new NormalizationDataFactory();
		}
		return instance;
	}

	public OWLClass getFreshConcept() {
		count++;
		String name = CONCEPT_PREFIX + count;
		OWLClass freshOWLClass = owlDataFactory.getOWLClass(IRI.create(name));
		conceptsByNormalization.add(freshOWLClass);
		return freshOWLClass;
	}

	public OWLClass getFreshConceptForTransitivity() {
		count++;
		String name = TRANSITIVITY_CONCEPT_PREFIX + count;
		OWLClass freshOWLClass = owlDataFactory.getOWLClass(IRI.create(name));
		conceptsByNormalization.add(freshOWLClass);
		return freshOWLClass;
	}

	public OWLClass getFreshOWLClassForNominal(OWLNamedIndividual ind) {
		OWLClass owlClass = nominal2ConceptMap.get(ind);
		if (owlClass != null) {
			return owlClass;
		} else {
			count++;
			String name = NOMINAL_CONCEPT_PREFIX + count;
			OWLClass freshOWLClass = owlDataFactory.getOWLClass(IRI.create(name));
			nominal2ConceptMap.put(ind, freshOWLClass);
			conceptsByNormalization.add(freshOWLClass);
			return freshOWLClass;
		}
	}

	/**
	 * Get one fresh Concept name for a set of same individuals.
	 * 
	 * @param inds
	 * @return
	 */
	public OWLClass getFreshOWLClassForNominal(Set<OWLNamedIndividual> inds) {

		count++;
		String name = NOMINAL_CONCEPT_PREFIX + count;
		OWLClass freshOWLClass = owlDataFactory.getOWLClass(IRI.create(name));
		conceptsByNormalization.add(freshOWLClass);
		for (OWLNamedIndividual ind : inds) {
			nominal2ConceptMap.put(ind, freshOWLClass);
		}
		return freshOWLClass;

	}

	/**
	 * @return Number of Concepts generated during normalization.
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @return A set of new OWLClasses generated during normalization.
	 */
	public Set<OWLClass> getConceptsByNormalization() {
		return conceptsByNormalization;
	}

	public void clear() {
		conceptsByNormalization.clear();
		nominal2ConceptMap.clear();
		count = 0;

	}

	public String getConceptPrefix() {
		return CONCEPT_PREFIX;
	}

	public String getNominalConceptPrefix() {
		return NOMINAL_CONCEPT_PREFIX;
	}

	public String getTransivityConceptPrefix() {
		return TRANSITIVITY_CONCEPT_PREFIX;
	}

}
