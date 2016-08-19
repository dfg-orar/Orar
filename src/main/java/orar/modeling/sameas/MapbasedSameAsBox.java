package orar.modeling.sameas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

public class MapbasedSameAsBox implements SameAsBox {
	private OWLDataFactory owlDataFactory;
	/*
	 * Store map between individual and its equal one. We only store such a
	 * mapping for one individual if it is really equal to individuals other
	 * than itself.
	 */
	private final Map<OWLNamedIndividual, Set<OWLNamedIndividual>> sameAsMap;
	// private final List<Set<OWLNamedIndividual>> listOfSameasAssertions;

	public MapbasedSameAsBox() {
		this.sameAsMap = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		// this.listOfSameasAssertions = new ArrayList<>();
	}

	@Override
	public Set<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual individual) {
		Set<OWLNamedIndividual> equalIndividuals = this.sameAsMap.get(individual);
		if (equalIndividuals != null) {
			return equalIndividuals;
		} else {
			return new HashSet<OWLNamedIndividual>();
		}
	}

	@Override
	public boolean addSameAsAssertion(OWLNamedIndividual individual, OWLNamedIndividual equalIndividual) {
		Set<OWLNamedIndividual> existingEqualIndividuals = this.sameAsMap.get(individual);
		if (existingEqualIndividuals == null) {
			existingEqualIndividuals = new HashSet<OWLNamedIndividual>();
		}
		boolean hasNewElement = existingEqualIndividuals.add(equalIndividual);
		this.sameAsMap.put(individual, existingEqualIndividuals);
		return hasNewElement;
	}

	@Override
	public boolean addManySameAsAssertions(OWLNamedIndividual individual,
			Set<OWLNamedIndividual> manyEqualIndividuals) {
		Set<OWLNamedIndividual> existingEqualIndividuals = this.sameAsMap.get(individual);
		if (existingEqualIndividuals == null) {
			existingEqualIndividuals = new HashSet<OWLNamedIndividual>();
		}
		boolean hasNewElement = existingEqualIndividuals.addAll(manyEqualIndividuals);
		this.sameAsMap.put(individual, existingEqualIndividuals);
		return hasNewElement;
	}

	@Override
	public Set<OWLNamedIndividual> getAllIndividuals() {
		Set<OWLNamedIndividual> allIndividuals = new HashSet<OWLNamedIndividual>();
		allIndividuals.addAll(this.sameAsMap.keySet());
		for (Set<OWLNamedIndividual> value : this.sameAsMap.values()) {
			allIndividuals.addAll(value);
		}
		return allIndividuals;
	}

	@Override
	public boolean addSameasAssertions(Set<OWLNamedIndividual> setOfSameasIndividuals) {
		boolean updated = false;
		for (OWLNamedIndividual anIndividual : setOfSameasIndividuals) {
			Set<OWLNamedIndividual> existsingInds = this.sameAsMap.get(anIndividual);
			if (existsingInds == null) {
				existsingInds = new HashSet<OWLNamedIndividual>();
			}
			if (existsingInds.addAll(setOfSameasIndividuals)) {
				updated = true;
			}
			this.sameAsMap.put(anIndividual, existsingInds);
		}
		return updated;
	}

	@Override
	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getSameasMap() {
		return new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>(this.sameAsMap);
	}

	@Override
	public Set<OWLAxiom> getEntailedSameasOWLAxioms() {
		Set<OWLAxiom> sameasOWLAxioms = new HashSet<>();

		Iterator<Entry<OWLNamedIndividual, Set<OWLNamedIndividual>>> iterator = this.sameAsMap.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLNamedIndividual>> entry = iterator.next();
			OWLNamedIndividual key = entry.getKey();
			Set<OWLNamedIndividual> value = entry.getValue();

			Set<OWLNamedIndividual> allIndividuals = new HashSet<>();
			allIndividuals.add(key);
			if (value != null) {
				allIndividuals.addAll(value);
			}

			OWLSameIndividualAxiom sameasAxiom = this.owlDataFactory.getOWLSameIndividualAxiom(allIndividuals);
			sameasOWLAxioms.add(sameasAxiom);
		}

		return sameasOWLAxioms;
	}
}
