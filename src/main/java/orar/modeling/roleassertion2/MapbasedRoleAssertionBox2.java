package orar.modeling.roleassertion2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.indexing.IndividualIndexer;
import orar.modeling.ontology2.AssertionDecoder;

/**
 * Store role assertion using maps. For example R(a,b) and R(a,c) will be stored
 * in two map like: 1. a ---> < R --->{b,c} > 2. first entry: b ---->< R---->{a}
 * >, second entry: c ---> <R-->{c}>
 * 
 * @author kien
 *
 */
public class MapbasedRoleAssertionBox2 implements RoleAssertionBox2 {

	/**
	 * Store role assertions grouping by subjects and roles. For example R(a,b)
	 * and R(a,c) will be stored in the map like: a ---> < R --->{b,c} >
	 */
	private final Map<Integer, Map<OWLObjectProperty, Set<Integer>>> roleAssertionMapWithSubjectAsKey;

	/**
	 * Store role assertions grouping by objects and roles. For example R(a,b)
	 * and R(a,c) will be stored in the map like:<br>
	 * first entry: b ---> <R --->{a}> <br>
	 * second entry: c ---><R --->{a}>
	 */
	private final Map<Integer, Map<OWLObjectProperty, Set<Integer>>> roleAssertionMapWithObjectAsKey;

	/**
	 * Partial store of role assertions grouping by roles. E.g. R(a,b), R(a,c)
	 * will be stored in the map as: R ---> <a>
	 */
	private final Map<OWLObjectProperty, Set<Integer>> roleAssertionMapWithRoleAsKeyAndSubjectAsValue;

	/**
	 * Partial store of role assertions grouping by roles. E.g. R(a,b), R(c,d)
	 * will be stored in the map as: R ---> <b,d>
	 *
	 */
	private final Map<OWLObjectProperty, Set<Integer>> roleAssertionMapWithRoleAsKeyAndObjectAsValue;
	// TODO: maps from role to individuals should be restricted for using in
	// role-hierarchy rule, trans rule, and functionality rule.

	private IndividualIndexer indexer;
	private OWLDataFactory owlDataFactory;

	public MapbasedRoleAssertionBox2() {
		this.roleAssertionMapWithSubjectAsKey = new HashMap<Integer, Map<OWLObjectProperty, Set<Integer>>>();
		this.roleAssertionMapWithObjectAsKey = new HashMap<Integer, Map<OWLObjectProperty, Set<Integer>>>();
		this.roleAssertionMapWithRoleAsKeyAndObjectAsValue = new HashMap<OWLObjectProperty, Set<Integer>>();
		this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue = new HashMap<OWLObjectProperty, Set<Integer>>();
		this.indexer = IndividualIndexer.getInstance();
		this.owlDataFactory = OWLManager.getOWLDataFactory();
	}

	@Override
	public boolean addRoleAssertion(Integer subject, OWLObjectProperty role, Integer object) {
		boolean hasNewElement = addRoleAssertionToMapWithIndividualAsKey(subject, role, object,
				this.roleAssertionMapWithSubjectAsKey);
		if (hasNewElement) {
			addRoleAssertionToMapWithIndividualAsKey(object, role, subject, this.roleAssertionMapWithObjectAsKey);
			addRoleAssertionToMapWithRoleAsKey(role, subject, this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue);
			addRoleAssertionToMapWithRoleAsKey(role, object, this.roleAssertionMapWithRoleAsKeyAndObjectAsValue);
		}
		return hasNewElement;

	}

//	@Override
//	public boolean addManyRoleAssertions(int subject, OWLObjectProperty role, Set<Integer> objects) {
//	
//		boolean hasNewElement = addManyRoleAssertionsToMapWithIndividualAsKey(subject, role, objects,
//				this.roleAssertionMapWithSubjectAsKey);
//		if (hasNewElement) {
//			addRoleAssertionToMapWithIndividualAsKey(object, role, subject, this.roleAssertionMapWithObjectAsKey);
//			addRoleAssertionToMapWithRoleAsKey(role, subject, this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue);
//			addRoleAssertionToMapWithRoleAsKey(role, object, this.roleAssertionMapWithRoleAsKeyAndObjectAsValue);
//		}
//		return hasNewElement;
//
//	}
//	
	private void addRoleAssertionToMapWithRoleAsKey(OWLObjectProperty role, Integer individual,
			Map<OWLObjectProperty, Set<Integer>> roleAssertionMapWithRoleAsKey) {

		Set<Integer> existingSet = roleAssertionMapWithRoleAsKey.get(role);
		if (existingSet == null) {
			existingSet = new HashSet<Integer>();
		}
		boolean hasNewElement = existingSet.add(individual);
		if (hasNewElement) {
			roleAssertionMapWithRoleAsKey.put(role, existingSet);
		}
	}

	/**
	 * @param ind1
	 * @param property
	 * @param ind2
	 * @param map
	 *            add the entry {@code ind1 --> (property--->{ind2})} to the
	 *            {@code map}
	 */
	boolean addRoleAssertionToMapWithIndividualAsKey(Integer ind1, OWLObjectProperty property, Integer ind2,
			Map<Integer, Map<OWLObjectProperty, Set<Integer>>> map) {

		Map<OWLObjectProperty, Set<Integer>> subMap = map.get(ind1);
		if (subMap == null) {
			subMap = new HashMap<OWLObjectProperty, Set<Integer>>();
		}
		Set<Integer> neighbours = subMap.get(property);
		if (neighbours == null) {
			neighbours = new HashSet<Integer>();
		}
		boolean addingSuccess = neighbours.add(ind2);
		if (addingSuccess) {
			subMap.put(property, neighbours);
			map.put(ind1, subMap);
		}
		return addingSuccess;
	}
	boolean addManyRoleAssertionsToMapWithIndividualAsKey(Integer ind1, OWLObjectProperty property, Set<Integer> manyInds2,
			Map<Integer, Map<OWLObjectProperty, Set<Integer>>> map) {

		Map<OWLObjectProperty, Set<Integer>> subMap = map.get(ind1);
		if (subMap == null) {
			subMap = new HashMap<OWLObjectProperty, Set<Integer>>();
		}
		Set<Integer> neighbours = subMap.get(property);
		if (neighbours == null) {
			neighbours = new HashSet<Integer>();
		}
		boolean addingSuccess = neighbours.addAll(manyInds2);
		if (addingSuccess) {
			subMap.put(property, neighbours);
			map.put(ind1, subMap);
		}
		return addingSuccess;
	}
	// /**
	// * @param ind1
	// * @param property
	// * @param ind2
	// * @param map
	// * add the entry {@code ind1 --> (property--->{ind2})} to the
	// * {@code map}
	// */
	// boolean addRoleAssertionToMapWithIndividualAsKeyList(Integer ind1,
	// OWLObjectProperty property, Integer ind2,
	// Map<Integer, Map<OWLObjectProperty, List<Integer>>> map) {
	//
	// Map<OWLObjectProperty, List<Integer>> subMap = map.get(ind1);
	// if (subMap == null) {
	// subMap = new HashMap<OWLObjectProperty, List<Integer>>();
	// }
	// List<Integer> neighbours = subMap.get(property);
	// if (neighbours == null) {
	// neighbours = new ArrayList<Integer>();
	// }
	// boolean addingSuccess = neighbours.add(ind2);
	// subMap.put(property, neighbours);
	// map.put(ind1, subMap);
	// return addingSuccess;
	// }

	@Override
	public int getNumberOfRoleAssertions() {
		int numberOfRoleAssertions = 0;

		Iterator<Entry<Integer, Map<OWLObjectProperty, Set<Integer>>>> iteratorOfRoleAssertionMap = this.roleAssertionMapWithSubjectAsKey
				.entrySet().iterator();
		while (iteratorOfRoleAssertionMap.hasNext()) {
			Entry<Integer, Map<OWLObjectProperty, Set<Integer>>> entry = iteratorOfRoleAssertionMap.next(); // e.g.
			// a
			// ---><R
			// -->{b1,b2},
			// S
			// -->{c1,c2}

			Map<OWLObjectProperty, Set<Integer>> successorMap = entry.getValue();
			Iterator<Entry<OWLObjectProperty, Set<Integer>>> iteratorForSuccessorMap = successorMap.entrySet()
					.iterator();
			while (iteratorForSuccessorMap.hasNext()) {
				Entry<OWLObjectProperty, Set<Integer>> successorMapEntry = iteratorForSuccessorMap.next();
				// e.g R -->{b1,b2}
				numberOfRoleAssertions += successorMapEntry.getValue().size();
			}
		}
		return numberOfRoleAssertions;
	}

	@Override
	public Set<OWLObjectPropertyAssertionAxiom> getOWLAPIRoleAssertions() {
		// OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();
		Set<OWLObjectPropertyAssertionAxiom> assertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		Iterator<Entry<Integer, Map<OWLObjectProperty, Set<Integer>>>> iterator = this.roleAssertionMapWithSubjectAsKey
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Map<OWLObjectProperty, Set<Integer>>> entry = iterator.next();
			Integer subject = entry.getKey();
			Map<OWLObjectProperty, Set<Integer>> successorMap = entry.getValue();
			Iterator<Entry<OWLObjectProperty, Set<Integer>>> subIterator = successorMap.entrySet().iterator();
			while (subIterator.hasNext()) {
				Entry<OWLObjectProperty, Set<Integer>> subEntry = subIterator.next();
				OWLObjectProperty role = subEntry.getKey();
				for (Integer object : subEntry.getValue()) {

					OWLObjectPropertyAssertionAxiom assertion = AssertionDecoder.getOWLAPIRoleAssertion(role, subject,
							object);
					assertions.add(assertion);
				}
			}
		}
		return assertions;
	}

	// private OWLObjectPropertyAssertionAxiom
	// getOWLAPIRoleAssertion(OWLObjectProperty owlapiRole, Integer
	// subjectInteger,
	// Integer objectInteger) {
	// String owlapiSubjectString =
	// this.indexer.getIndividualString(subjectInteger);
	// OWLNamedIndividual owlapiSubject =
	// this.owlDataFactory.getOWLNamedIndividual(IRI.create(owlapiSubjectString));
	// String owlapiObjectString =
	// this.indexer.getIndividualString(objectInteger);
	// OWLNamedIndividual owlapiObject =
	// this.owlDataFactory.getOWLNamedIndividual(IRI.create(owlapiObjectString));
	// OWLObjectPropertyAssertionAxiom assertion =
	// owlDataFactory.getOWLObjectPropertyAssertionAxiom(owlapiRole,
	// owlapiSubject, owlapiObject);
	// return assertion;
	// }

	@Override
	public Set<Integer> getSubjectsInRoleAssertions(OWLObjectProperty role) {
		Set<Integer> subjects = this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue.get(role);
		if (subjects != null) {
			return new HashSet<Integer>(subjects);
		} else {
			return new HashSet<Integer>();
		}
	}

	@Override
	public Set<Integer> getObjectsInRoleAssertions(OWLObjectProperty role) {
		Set<Integer> subjects = this.roleAssertionMapWithRoleAsKeyAndObjectAsValue.get(role);
		if (subjects != null) {
			return new HashSet<Integer>(subjects);
		} else {
			return new HashSet<Integer>();
		}
	}

	@Override
	public Set<Integer> getAllIndividuals() {
		Set<Integer> allIndividuals = new HashSet<Integer>();
		allIndividuals.addAll(this.roleAssertionMapWithSubjectAsKey.keySet());
		allIndividuals.addAll(this.roleAssertionMapWithObjectAsKey.keySet());
		return allIndividuals;
	}

	@Override
	public Map<OWLObjectProperty, Set<Integer>> getSuccesorRoleAssertionsAsMap(Integer subjectIndividual) {
		Map<OWLObjectProperty, Set<Integer>> successorAssertionsAsMap = this.roleAssertionMapWithSubjectAsKey
				.get(subjectIndividual);
		if (successorAssertionsAsMap == null) {
			successorAssertionsAsMap = new HashMap<OWLObjectProperty, Set<Integer>>();
		}
		return successorAssertionsAsMap;
	}

	@Override
	public Map<OWLObjectProperty, Set<Integer>> getPredecessorRoleAssertionsAsMap(Integer objectIndividual) {
		Map<OWLObjectProperty, Set<Integer>> predecessorAssertionsAsMap = this.roleAssertionMapWithObjectAsKey
				.get(objectIndividual);
		if (predecessorAssertionsAsMap == null) {
			predecessorAssertionsAsMap = new HashMap<OWLObjectProperty, Set<Integer>>();
		}
		return predecessorAssertionsAsMap;

	}

	// @Override
	// public Set<OWLObjectPropertyAssertionAxiom>
	// getOWLAPIRoleAssertions(Integer subject) {
	// Map<OWLObjectProperty, Set<Integer>> sucessorMap =
	// this.roleAssertionMapWithSubjectAsKey.get(subject);
	// Set<OWLObjectPropertyAssertionAxiom> roleAssertions;
	// if(sucessorMap ==null)
	// return null;
	// }

}
