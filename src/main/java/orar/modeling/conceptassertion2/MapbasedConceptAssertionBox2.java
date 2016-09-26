package orar.modeling.conceptassertion2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import orar.data.NormalizationDataFactory;
import orar.dlfragmentvalidator.ValidatorDataFactory;
import orar.indexing.IndividualIndexer;
import orar.modeling.ontology2.AssertionDecoder;

public class MapbasedConceptAssertionBox2 implements ConceptAssertionBox2 {
	private Logger logger = Logger.getLogger(MapbasedConceptAssertionBox2.class);
	private final Map<Integer, Set<OWLClass>> conceptAssertionMap;
	private IndividualIndexer indexer;
	private OWLDataFactory owlDataFactory;
	private final NormalizationDataFactory normalizationDataFactory;

	public MapbasedConceptAssertionBox2() {
		this.conceptAssertionMap = new HashMap<Integer, Set<OWLClass>>();
		this.indexer = IndividualIndexer.getInstance();
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		this.normalizationDataFactory = NormalizationDataFactory.getInstance();
	}

	@Override
	public Set<OWLClass> getAssertedConcepts(Integer individual) {

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

		Iterator<Entry<Integer, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Set<OWLClass>> entry = iterator.next();
			Integer a = entry.getKey();
			Set<OWLClass> assertedClasses = entry.getValue();
			for (OWLClass owlClass : assertedClasses) {
				// OWLClassAssertionAxiom classAssertion =
				// getOWLAPIConceptAssertoin(owlClass, a);
				// owlDataFactory.getOWLClassAssertionAxiom(owlClass, a);
				OWLClassAssertionAxiom classAssertion = AssertionDecoder.getOWLAPIConceptAssertoin(owlClass, a);
				classAssertionAxioms.add(classAssertion);
			}
		}

		return classAssertionAxioms;
	}

	@Override
	public boolean addManyConceptAssertions(Integer individual, Set<OWLClass> concepts) {
		Set<OWLClass> existingClasses = this.conceptAssertionMap.get(individual);
		if (existingClasses == null) {
			existingClasses = new HashSet<OWLClass>();
		}
		boolean hasNewElement = existingClasses.addAll(concepts);
		if (hasNewElement) {
			this.conceptAssertionMap.put(individual, existingClasses);
		}
		return hasNewElement;
	}

	@Override
	public boolean addConceptAssertion(Integer individual, OWLClass concept) {
		Set<OWLClass> existingClasses = this.conceptAssertionMap.get(individual);
		if (existingClasses == null) {
			existingClasses = new HashSet<OWLClass>();
		}
		boolean hasNewElement = existingClasses.add(concept);
		if (hasNewElement) {
			this.conceptAssertionMap.put(individual, existingClasses);
		}
		return hasNewElement;

	}

	@Override
	public int getNumberOfConceptAssertions() {
		int numberOfCocneptAssertions = 0;
		Iterator<Entry<Integer, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Set<OWLClass>> entry = iterator.next();
			numberOfCocneptAssertions += entry.getValue().size();
		}
		return numberOfCocneptAssertions;
	}

	@Override
	public int getNumberOfConceptAssertionsWithoutNormalizationSymbols() {
		int numberOfCocneptAssertions = 0;
//		Set<Integer> processedIndividual= new HashSet<>();
		
		Iterator<Entry<Integer, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Set<OWLClass>> entry = iterator.next();
			Set<OWLClass> assertedConcept = entry.getValue();
			SetView<OWLClass> intersection = Sets.intersection(assertedConcept,
					this.normalizationDataFactory.getConceptsByNormalization());
			numberOfCocneptAssertions = numberOfCocneptAssertions + assertedConcept.size() - intersection.size();
		}
		return numberOfCocneptAssertions;
	}

	@Override
	public boolean addConceptAssertion(OWLClass concept, Integer individual) {
		return addConceptAssertion(individual, concept);
	}

	@Override
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWithoutNormalizationSymbols() {

		Set<OWLClassAssertionAxiom> classAssertionAxioms = new HashSet<OWLClassAssertionAxiom>();

		OWLClass thingConcept = OWLManager.getOWLDataFactory().getOWLThing();

		Iterator<Entry<Integer, Set<OWLClass>>> iterator = conceptAssertionMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Set<OWLClass>> entry = iterator.next();
			Integer ind = entry.getKey();
			Set<OWLClass> assertedClasses = entry.getValue();
			for (OWLClass owlClass : assertedClasses) {
				boolean isNotIndividualByNormalization = !ValidatorDataFactory.getInstance()
						.getNamedIndividualGeneratedDuringValidation().contains(ind);
				boolean isNotConceptByNormalization = !NormalizationDataFactory.getInstance()
						.getConceptsByNormalization().contains(owlClass);
				boolean isNotThingConcept = !owlClass.equals(thingConcept);
				if (isNotConceptByNormalization && isNotIndividualByNormalization && isNotThingConcept) {

					OWLClassAssertionAxiom classAssertion = AssertionDecoder.getOWLAPIConceptAssertoin(owlClass, ind);
					classAssertionAxioms.add(classAssertion);
				}
			}
		}

		return classAssertionAxioms;
	}

	// private OWLClassAssertionAxiom getOWLAPIConceptAssertoin(OWLClass
	// owlClass, Integer individualLong) {
	// String individualString =
	// this.indexer.getIndividualString(individualLong);
	// OWLNamedIndividual owlapiIndividual =
	// this.owlDataFactory.getOWLNamedIndividual(IRI.create(individualString));
	// OWLClassAssertionAxiom classAssertion =
	// this.owlDataFactory.getOWLClassAssertionAxiom(owlClass,
	// owlapiIndividual);
	// return classAssertion;
	// }

	@Override
	public Set<Integer> getAllIndividuals() {
		return new HashSet<Integer>(this.conceptAssertionMap.keySet());
	}

}
