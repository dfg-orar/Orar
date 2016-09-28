package orar.modeling.sameas2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import orar.indexing.IndividualIndexer;
import orar.modeling.ontology2.AssertionDecoder;

public class MapbasedSameAsBox2 implements SameAsBox2 {
	private OWLDataFactory owlDataFactory;
	/*
	 * Store map between individual and its equal one. We only store such a
	 * mapping for one individual if it is really equal to individuals other
	 * than itself.
	 */
	private final Map<Integer, Set<Integer>> sameAsMap;
	// private final List<Set<Integer>> listOfSameasAssertions;

	private IndividualIndexer indexer;

	public MapbasedSameAsBox2() {
		this.sameAsMap = new HashMap<Integer, Set<Integer>>();
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		this.indexer = IndividualIndexer.getInstance();
		// this.listOfSameasAssertions = new ArrayList<>();
	}

	@Override
	public Set<Integer> getSameIndividuals(Integer individual) {
		Set<Integer> equalIndividuals = this.sameAsMap.get(individual);
		if (equalIndividuals != null) {
			return equalIndividuals;
		} else {
			return new HashSet<Integer>();
		}
	}

	@Override
	public boolean addSameAsAssertion(Integer individual, Integer equalIndividual) {
		Set<Integer> existingEqualIndividuals = this.sameAsMap.get(individual);
		if (existingEqualIndividuals == null) {
			existingEqualIndividuals = new HashSet<Integer>();
			this.sameAsMap.put(individual, existingEqualIndividuals);
		}
		boolean hasNewElement = existingEqualIndividuals.add(equalIndividual);
		return hasNewElement;
	}

	@Override
	public boolean addManySameAsAssertions(Integer individual, Set<Integer> manyEqualIndividuals) {
		Set<Integer> existingEqualIndividuals = this.sameAsMap.get(individual);
		if (existingEqualIndividuals == null) {
			existingEqualIndividuals = new HashSet<Integer>();
			this.sameAsMap.put(individual, existingEqualIndividuals);
		}
		boolean hasNewElement = existingEqualIndividuals.addAll(manyEqualIndividuals);
		return hasNewElement;
	}

	@Override
	public Set<Integer> getAllIndividuals() {
		Set<Integer> allIndividuals = new HashSet<Integer>();
		allIndividuals.addAll(this.sameAsMap.keySet());
		for (Set<Integer> value : this.sameAsMap.values()) {
			allIndividuals.addAll(value);
		}
		return allIndividuals;
	}

	@Override
	public boolean addSameasAssertions(Set<Integer> setOfSameasIndividuals) {
		boolean updated = false;
		for (Integer anIndividual : setOfSameasIndividuals) {
			Set<Integer> existsingInds = this.sameAsMap.get(anIndividual);
			if (existsingInds == null) {
				existsingInds = new HashSet<Integer>();
				this.sameAsMap.put(anIndividual, existsingInds);
			}
			if (existsingInds.addAll(setOfSameasIndividuals)) {
				updated = true;
			}
		}
		return updated;
	}

	@Override
	public Map<Integer, Set<Integer>> getSameasMap() {
		return new HashMap<Integer, Set<Integer>>(this.sameAsMap);
	}

	@Override
	public Set<OWLAxiom> getOWLAPISameasAssertions() {
		Set<OWLAxiom> sameasOWLAxioms = new HashSet<OWLAxiom>();
		Set<Integer> allKeyIndividualsInSameasMap = this.sameAsMap.keySet();

		Set<Integer> checkedIndividuals = new HashSet<>();

		for (Integer eachIndividual : allKeyIndividualsInSameasMap) {
			if (!checkedIndividuals.contains(eachIndividual)) {

				Set<Integer> sameasOfEachIdividual = new HashSet<>();
				sameasOfEachIdividual.add(eachIndividual);
				sameasOfEachIdividual.addAll(getSameIndividuals(eachIndividual));
				OWLSameIndividualAxiom sameasAxiom = AssertionDecoder.getOWLAPISameasAssertion(sameasOfEachIdividual);
				sameasOWLAxioms.add(sameasAxiom);
				checkedIndividuals.addAll(sameasOfEachIdividual);
			}

		}
		return sameasOWLAxioms;
	}

	@Override
	public Integer getNumberOfEntailedSameasAssertions() {
		Set<Set<Integer>> uniqueSetOfSameasAssertions = new HashSet<>();
		Set<Integer> allKeyIndividualsInSameasMap = this.sameAsMap.keySet();

		Set<Integer> checkedIndividuals = new HashSet<>();

		for (Integer eachIndividual : allKeyIndividualsInSameasMap) {
			if (!checkedIndividuals.contains(eachIndividual)) {

				Set<Integer> sameasOfEachIdividual = new HashSet<>();
				sameasOfEachIdividual.add(eachIndividual);
				sameasOfEachIdividual.addAll(getSameIndividuals(eachIndividual));

				checkedIndividuals.addAll(sameasOfEachIdividual);
				uniqueSetOfSameasAssertions.add(sameasOfEachIdividual);
			}

		}

		return uniqueSetOfSameasAssertions.size();
	}

	@Override
	public boolean addNewManySameAsAssertions(Set<Integer> equalIndividuals) {
		for (Integer eachIndiv : equalIndividuals) {

			this.sameAsMap.put(eachIndiv, equalIndividuals);
		}
		return true;
	}
}
