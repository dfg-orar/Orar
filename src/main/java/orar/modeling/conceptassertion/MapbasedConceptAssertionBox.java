package orar.modeling.conceptassertion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.data.NormalizationDataFactory;
import orar.dlfragmentvalidator.ValidatorDataFactory;

public class MapbasedConceptAssertionBox implements ConceptAssertionBox {
	private Logger logger = Logger.getLogger(MapbasedConceptAssertionBox.class);
	private final Map<OWLNamedIndividual, Set<OWLClass>> conceptAssertionMap;

	public MapbasedConceptAssertionBox() {
		this.conceptAssertionMap = new HashMap<OWLNamedIndividual, Set<OWLClass>>();

	}

	@Override
	public Set<OWLClass> getAssertedConcepts(OWLNamedIndividual individual) {

		Set<OWLClass> assertedConcepts = this.conceptAssertionMap.get(individual);
		if (assertedConcepts != null) {
			return assertedConcepts;
		}
		return new HashSet<OWLClass>();

	}

	@Override
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertions() {
		Set<OWLClassAssertionAxiom> classAssertionAxioms = new HashSet<OWLClassAssertionAxiom>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory owlDataFactory = manager.getOWLDataFactory();

		Iterator<Entry<OWLNamedIndividual, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLClass>> entry = iterator.next();
			OWLNamedIndividual a = entry.getKey();
			Set<OWLClass> assertedClasses = entry.getValue();
			for (OWLClass owlClass : assertedClasses) {
				OWLClassAssertionAxiom classAssertion = owlDataFactory.getOWLClassAssertionAxiom(owlClass, a);
				classAssertionAxioms.add(classAssertion);
			}
		}

		return classAssertionAxioms;
	}

	@Override
	public boolean addManyConceptAssertions(OWLNamedIndividual individual, Set<OWLClass> concepts) {

		Set<OWLClass> existingClasses = this.conceptAssertionMap.get(individual);
		if (existingClasses == null) {
			existingClasses = new HashSet<OWLClass>();
		}
		boolean hasNewElement = existingClasses.addAll(concepts);
		this.conceptAssertionMap.put(individual, existingClasses);
		return hasNewElement;
	}

	@Override
	public boolean addConceptAssertion(OWLNamedIndividual individual, OWLClass concept) {
		Set<OWLClass> existingClasses = this.conceptAssertionMap.get(individual);
		if (existingClasses == null) {
			existingClasses = new HashSet<OWLClass>();
		}
		boolean hasNewElement = existingClasses.add(concept);
		this.conceptAssertionMap.put(individual, existingClasses);
		return hasNewElement;
	}

	@Override
	public int getNumberOfConceptAssertions() {
		int numberOfCocneptAssertions = 0;
		Iterator<Entry<OWLNamedIndividual, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLClass>> entry = iterator.next();
			numberOfCocneptAssertions += entry.getValue().size();
		}
		return numberOfCocneptAssertions;
	}

	@Override
	public boolean addConceptAssertion(OWLClass concept, OWLNamedIndividual individual) {
		return addConceptAssertion(individual, concept);
	}

	@Override
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWithoutNormalizationSymbols() {

		Set<OWLClassAssertionAxiom> classAssertionAxioms = new HashSet<OWLClassAssertionAxiom>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
		OWLClass thingConcept = OWLManager.getOWLDataFactory().getOWLThing();

		Iterator<Entry<OWLNamedIndividual, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLClass>> entry = iterator.next();
			OWLNamedIndividual ind = entry.getKey();
			Set<OWLClass> assertedClasses = entry.getValue();
			for (OWLClass owlClass : assertedClasses) {
				boolean isNotIndividualByNormalization = !ValidatorDataFactory.getInstance()
						.getNamedIndividualGeneratedDuringValidation().contains(ind);
				boolean isNotConceptByNormalization = !NormalizationDataFactory.getInstance()
						.getConceptsByNormalization().contains(owlClass);
				boolean isNotThingConcept = !owlClass.equals(thingConcept);
				if (isNotConceptByNormalization && isNotIndividualByNormalization && isNotThingConcept) {

					OWLClassAssertionAxiom classAssertion = owlDataFactory.getOWLClassAssertionAxiom(owlClass, ind);
					classAssertionAxioms.add(classAssertion);
				}
			}
		}

		return classAssertionAxioms;
	}

	@Override
	public Set<OWLNamedIndividual> getAllIndividuals() {
		return new HashSet<OWLNamedIndividual>(this.conceptAssertionMap.keySet());
	}

}
