package orar.modeling.roleassertion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

/**
 * Store role assertion using maps. For example R(a,b) and R(a,c) will be stored
 * in two map like: 1. a ---> < R --->{b,c} > 2. first entry: b ---->< R---->{a}
 * >, second entry: c ---> <R-->{c}>
 * 
 * @author kien
 *
 */
public class MapbasedRoleAssertionBox implements RoleAssertionBox {

	/**
	 * Store role assertions grouping by subjects and roles. For example R(a,b)
	 * and R(a,c) will be stored in the map like: a ---> < R --->{b,c} >
	 */
	private final Map<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>> roleAssertionMapWithSubjectAsKey;

	/**
	 * Store role assertions grouping by objects and roles. For example R(a,b)
	 * and R(a,c) will be stored in the map like:<br>
	 * first entry: b ---> <R --->{a}> <br>
	 * second entry: c ---><R --->{a}>
	 */
	private final Map<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>> roleAssertionMapWithObjectAsKey;

	/**
	 * Partial store of role assertions grouping by roles. E.g. R(a,b), R(a,c)
	 * will be stored in the map as: R ---> <a>
	 */
	private final Map<OWLObjectProperty, Set<OWLNamedIndividual>> roleAssertionMapWithRoleAsKeyAndSubjectAsValue;

	/**
	 * Partial store of role assertions grouping by roles. E.g. R(a,b), R(c,d)
	 * will be stored in the map as: R ---> <b,d>
	 *
	 */
	private final Map<OWLObjectProperty, Set<OWLNamedIndividual>> roleAssertionMapWithRoleAsKeyAndObjectAsValue;
	// TODO: maps from role to individuals should be restricted for using in
	// role-hierarchy rule, trans rule, and functionality rule.

	public MapbasedRoleAssertionBox() {
		this.roleAssertionMapWithSubjectAsKey = new HashMap<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>>();
		this.roleAssertionMapWithObjectAsKey = new HashMap<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>>();
		this.roleAssertionMapWithRoleAsKeyAndObjectAsValue = new HashMap<OWLObjectProperty, Set<OWLNamedIndividual>>();
		this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue = new HashMap<OWLObjectProperty, Set<OWLNamedIndividual>>();
	}

	@Override
	public boolean addRoleAssertion(OWLNamedIndividual subject, OWLObjectProperty role, OWLNamedIndividual object) {
		boolean hasNewElement = addRoleAssertionToMapWithIndividualAsKey(subject, role, object,
				this.roleAssertionMapWithSubjectAsKey);
		if (hasNewElement) {
			addRoleAssertionToMapWithIndividualAsKey(object, role, subject, this.roleAssertionMapWithObjectAsKey);
			addRoleAssertionToMapWithRoleAsKey(role, subject, this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue);
			addRoleAssertionToMapWithRoleAsKey(role, object, this.roleAssertionMapWithRoleAsKeyAndObjectAsValue);
		}
		return hasNewElement;

	}

	private void addRoleAssertionToMapWithRoleAsKey(OWLObjectProperty role, OWLNamedIndividual individual,
			Map<OWLObjectProperty, Set<OWLNamedIndividual>> roleAssertionMapWithRoleAsKey) {
		// TODO: optimization: add only in case of TRANS or FUCTIONAL roles
		Set<OWLNamedIndividual> existingSet = roleAssertionMapWithRoleAsKey.get(role);
		if (existingSet == null) {
			existingSet = new HashSet<OWLNamedIndividual>();
		}
		existingSet.add(individual);
		roleAssertionMapWithRoleAsKey.put(role, existingSet);

	}

	/**
	 * @param ind1
	 * @param property
	 * @param ind2
	 * @param map
	 *            add the entry {@code ind1 --> (property--->{ind2})} to the
	 *            {@code map}
	 */
	boolean addRoleAssertionToMapWithIndividualAsKey(OWLNamedIndividual ind1, OWLObjectProperty property,
			OWLNamedIndividual ind2, Map<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>> map) {

		Map<OWLObjectProperty, Set<OWLNamedIndividual>> subMap = map.get(ind1);
		if (subMap == null) {
			subMap = new HashMap<OWLObjectProperty, Set<OWLNamedIndividual>>();
		}
		Set<OWLNamedIndividual> neighbours = subMap.get(property);
		if (neighbours == null) {
			neighbours = new HashSet<OWLNamedIndividual>();
		}
		boolean addingSuccess = neighbours.add(ind2);
		subMap.put(property, neighbours);
		map.put(ind1, subMap);
		return addingSuccess;
	}

	@Override
	public int getNumberOfRoleAssertions() {
		int numberOfRoleAssertions = 0;

		Iterator<Entry<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>>> iteratorOfRoleAssertionMap = this.roleAssertionMapWithSubjectAsKey
				.entrySet().iterator();
		while (iteratorOfRoleAssertionMap.hasNext()) {
			Entry<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>> entry = iteratorOfRoleAssertionMap
					.next(); // e.g. a ---><R -->{b1,b2}, S -->{c1,c2}

			Map<OWLObjectProperty, Set<OWLNamedIndividual>> successorMap = entry.getValue();
			Iterator<Entry<OWLObjectProperty, Set<OWLNamedIndividual>>> iteratorForSuccessorMap = successorMap
					.entrySet().iterator();
			while (iteratorForSuccessorMap.hasNext()) {
				Entry<OWLObjectProperty, Set<OWLNamedIndividual>> successorMapEntry = iteratorForSuccessorMap.next();
				// e.g R -->{b1,b2}
				numberOfRoleAssertions += successorMapEntry.getValue().size();
			}
		}
		return numberOfRoleAssertions;
	}

	@Override
	public Set<OWLObjectPropertyAssertionAxiom> getOWLAPIRoleAssertions() {
		OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();
		Set<OWLObjectPropertyAssertionAxiom> assertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		Iterator<Entry<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>>> iterator = this.roleAssertionMapWithSubjectAsKey
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Map<OWLObjectProperty, Set<OWLNamedIndividual>>> entry = iterator.next();
			OWLNamedIndividual subject = entry.getKey();
			Map<OWLObjectProperty, Set<OWLNamedIndividual>> successorMap = entry.getValue();
			Iterator<Entry<OWLObjectProperty, Set<OWLNamedIndividual>>> subIterator = successorMap.entrySet()
					.iterator();
			while (subIterator.hasNext()) {
				Entry<OWLObjectProperty, Set<OWLNamedIndividual>> subEntry = subIterator.next();
				OWLObjectProperty role = subEntry.getKey();
				for (OWLNamedIndividual object : subEntry.getValue()) {
					OWLObjectPropertyAssertionAxiom assertion = owlDataFactory.getOWLObjectPropertyAssertionAxiom(role,
							subject, object);
					assertions.add(assertion);
				}
			}
		}
		return assertions;
	}

	@Override
	public Set<OWLNamedIndividual> getSubjectsInRoleAssertions(OWLObjectProperty role) {
		Set<OWLNamedIndividual> subjects = this.roleAssertionMapWithRoleAsKeyAndSubjectAsValue.get(role);
		if (subjects != null) {
			return new HashSet<OWLNamedIndividual>(subjects);
		} else {
			return new HashSet<OWLNamedIndividual>();
		}
	}

	@Override
	public Set<OWLNamedIndividual> getObjectsInRoleAssertions(OWLObjectProperty role) {
		Set<OWLNamedIndividual> subjects = this.roleAssertionMapWithRoleAsKeyAndObjectAsValue.get(role);
		if (subjects != null) {
			return new HashSet<OWLNamedIndividual>(subjects);
		} else {
			return new HashSet<OWLNamedIndividual>();
		}
	}

	@Override
	public Set<OWLNamedIndividual> getAllIndividuals() {
		Set<OWLNamedIndividual> allIndividuals = new HashSet<OWLNamedIndividual>();
		allIndividuals.addAll(this.roleAssertionMapWithSubjectAsKey.keySet());
		allIndividuals.addAll(this.roleAssertionMapWithObjectAsKey.keySet());
		return allIndividuals;
	}

	@Override
	public Map<OWLObjectProperty, Set<OWLNamedIndividual>> getSuccesorRoleAssertionsAsMap(
			OWLNamedIndividual subjectIndividual) {
		Map<OWLObjectProperty, Set<OWLNamedIndividual>> successorAssertionsAsMap = this.roleAssertionMapWithSubjectAsKey
				.get(subjectIndividual);
		if (successorAssertionsAsMap == null) {
			successorAssertionsAsMap = new HashMap<OWLObjectProperty, Set<OWLNamedIndividual>>();
		}
		return successorAssertionsAsMap;
	}

	@Override
	public Map<OWLObjectProperty, Set<OWLNamedIndividual>> getPredecessorRoleAssertionsAsMap(
			OWLNamedIndividual objectIndividual) {
		Map<OWLObjectProperty, Set<OWLNamedIndividual>> predecessorAssertionsAsMap = this.roleAssertionMapWithObjectAsKey
				.get(objectIndividual);
		if (predecessorAssertionsAsMap == null) {
			predecessorAssertionsAsMap = new HashMap<OWLObjectProperty, Set<OWLNamedIndividual>>();
		}
		return predecessorAssertionsAsMap;
	}

	// @Override
	// public Set<OWLObjectPropertyAssertionAxiom>
	// getOWLAPIRoleAssertions(OWLNamedIndividual subject) {
	// Map<OWLObjectProperty, Set<OWLNamedIndividual>> sucessorMap =
	// this.roleAssertionMapWithSubjectAsKey.get(subject);
	// Set<OWLObjectPropertyAssertionAxiom> roleAssertions;
	// if(sucessorMap ==null)
	// return null;
	// }

}
