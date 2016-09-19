package orar.abstraction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.BasicIndividualType;
import orar.type.BasicIndividualTypeFactory;
import orar.type.BasicIndividualTypeFactory_UsingWeakHashMap;
import orar.type.IndividualType;
import x.util.MapOperator;

/**
 * Computing types for all individuals, taking equivalent individuals into
 * account.
 * 
 * @author kien
 *
 */
public class BasicTypeComputor implements TypeComputor {
	// private final BasicIndividualTypeFactory typeFactory;

	// private Configuration config;
	// private static Logger logger =
	// Logger.getLogger(HornSHOIF_TypeComputor.class);

	public BasicTypeComputor() {
		// typeFactory =
		// BasicIndividualTypeFactory_UsingWeakHashMap.getInstance();

		// this.config = Configuration.getInstance();
	}

	@Override
	public Map<IndividualType, Set<Integer>> computeTypes(OrarOntology2 orarOntology) {
		Map<IndividualType, Set<Integer>> typeMap2Individuals = new HashMap<IndividualType, Set<Integer>>();
		Set<Integer> todoIndividuals = orarOntology.getIndividualsInSignature();
		Set<Integer> processedIndividuals = new HashSet<Integer>();
		/*
		 * compute type for each individual, taking into account other equal
		 * individuals
		 */
		for (int currentIndividual : todoIndividuals) {
			if (!processedIndividuals.contains(currentIndividual)) {
				/*
				 * collect from assertions of "currentIndividual" and its equal
				 * individuals
				 */
				Set<Integer> sameIndsOfCurrentIndividual = orarOntology.getSameIndividuals(currentIndividual);
				// sameInds should contains also the "currentIndividual"
				sameIndsOfCurrentIndividual.add(currentIndividual);

				processedIndividuals.addAll(sameIndsOfCurrentIndividual);
				// get element of accumulate type
				Set<OWLClass> concepts = getConcepts(sameIndsOfCurrentIndividual, orarOntology);
				Set<OWLObjectProperty> preRoles = getPreRoles(sameIndsOfCurrentIndividual, currentIndividual,
						orarOntology);
				Set<OWLObjectProperty> sucRoles = getSuccRoles(sameIndsOfCurrentIndividual, currentIndividual,
						orarOntology);

				// /*
				// * small optimization: update also concept for all same
				// insividuals. It will reduce number of refinement steps
				// */
				// for (Integer eachInd : sameIndsOfCurrentIndividual) {
				// orarOntology.addManyConceptAssertions(eachInd, concepts);
				// }
				//
				// for (Integer eachInd : sameIndsOfCurrentIndividual) {
				// orarOntology.addManyConceptAssertions(eachInd, concepts);
				// }
				if (sameIndsOfCurrentIndividual.size() > 1) {
					orarOntology.addManyConceptAssertions(currentIndividual, concepts);
				}
				// create type and add to the resulting map
				IndividualType type = new BasicIndividualType(concepts, preRoles, sucRoles);

				// Map type to a set of individuals

				// MapOperator.addValuesToMap(typeMap2Individuals, type,
				// sameIndsOfCurrentIndividual);
				MapOperator.addValueToMap(typeMap2Individuals, type, currentIndividual);
				/*
				 * Map individual to its type, only need for computing
				 * over-approximation
				 */
				mapIndividual2Type(sameIndsOfCurrentIndividual, type);
			}
		}

		return typeMap2Individuals;

	}

	protected void mapIndividual2Type(Set<Integer> individuals, IndividualType type) {
		// Do nothing. This is needed only when we need to connect
		// representative individuals in case of computing an
		// over-approximation,
		// e.g. x1--R-->x2.
	}

	/**
	 * @param individual
	 * @return asserted concepts of the {@code individual} taking into account
	 *         sameas individuals.
	 * 
	 */
	private Set<OWLClass> getConcepts(Integer individual, OrarOntology2 orarOntology) {
		Set<OWLClass> accumulatedConcepts = new HashSet<OWLClass>();
		Set<Integer> sameIndsOfCurrentIndividual = orarOntology.getSameIndividuals(individual);
		// sameInds should contains also the "currentIndividual"
		sameIndsOfCurrentIndividual.add(individual);
		// accumulate types
		for (Integer ind : sameIndsOfCurrentIndividual) {

			Set<OWLClass> concepts = orarOntology.getAssertedConcepts(ind);
			if (concepts == null) {
				concepts = new HashSet<OWLClass>();
			}
			accumulatedConcepts.addAll(concepts);
		}
		return accumulatedConcepts;
	}

	/**
	 * @param individuals
	 * @return asserted concepts of all individual in the {@code individuals}
	 *         taking into account sameas individuals.
	 * 
	 */
	private Set<OWLClass> getConcepts(Set<Integer> individuals, OrarOntology2 orarOntology) {
		Set<OWLClass> accumulatedConcepts = new HashSet<OWLClass>();
		for (Integer individual : individuals) {
			accumulatedConcepts.addAll(orarOntology.getAssertedConcepts(individual));
		}
		return accumulatedConcepts;
	}

	/**
	 * @param individuals
	 * @param orarOntology
	 * @return a set of successor roles of all individual in {@code individuals}
	 */
	private Set<OWLObjectProperty> getSuccRoles(Set<Integer> individuals, int representative,
			OrarOntology2 orarOntology) {
		Set<OWLObjectProperty> accumulatedRoles = new HashSet<OWLObjectProperty>();
		for (Integer ind : individuals) {
			Map<OWLObjectProperty, Set<Integer>> sucAssertionMap = orarOntology.getSuccessorRoleAssertionsAsMap(ind);
			Set<OWLObjectProperty> sucRoles = sucAssertionMap.keySet();
			accumulatedRoles.addAll(sucRoles);

			/*
			 * add role assertion for the representative ind. THis will reduce
			 * refinement steps. And we only do it when there are at least 2
			 * different same individuals.
			 */
			if (individuals.size() > 1) {
				Iterator<Entry<OWLObjectProperty, Set<Integer>>> iterator = sucAssertionMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<OWLObjectProperty, Set<Integer>> entry = iterator.next();
					OWLObjectProperty sucRole = entry.getKey();
					Set<Integer> succInds = entry.getValue();
					for (Integer eachSucc : succInds) {
						orarOntology.addRoleAssertion(representative, sucRole, eachSucc);
					}
				}
			}
		}
		return accumulatedRoles;

	}

	/**
	 * 
	 * @param individuals
	 * @param orarOntology
	 * @return a set of predecessor roles of all individual in
	 *         {@code individuals}
	 */
	private Set<OWLObjectProperty> getPreRoles(Set<Integer> individuals, Integer representative,
			OrarOntology2 orarOntology) {
		Set<OWLObjectProperty> accumulatedRoles = new HashSet<OWLObjectProperty>();
		for (Integer ind : individuals) {

			Map<OWLObjectProperty, Set<Integer>> predecessorAssertionMap = orarOntology
					.getPredecessorRoleAssertionsAsMap(ind);
			Set<OWLObjectProperty> preRoles = predecessorAssertionMap.keySet();
			accumulatedRoles.addAll(preRoles);
			/*
			 * add role assertions of same individual for the representative
			 * one. This will reduce refinements.And we only do it when there
			 * are at least 2 different same individuals.
			 */
			if (individuals.size() > 1) {
				Iterator<Entry<OWLObjectProperty, Set<Integer>>> iterator = predecessorAssertionMap.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<OWLObjectProperty, Set<Integer>> entry = iterator.next();
					Set<Integer> predecessors = entry.getValue();
					OWLObjectProperty preRole = entry.getKey();
					for (Integer eachPredecessors : predecessors) {
						orarOntology.addRoleAssertion(eachPredecessors, preRole, representative);
					}
				}
			}

		}
		return accumulatedRoles;

	}

}