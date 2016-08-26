package orar.abstraction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.BasicIndividualType;
import orar.type.BasicIndividualTypeFactory;
import orar.type.BasicIndividualTypeFactory_UsingWeakHashMap;
import orar.type.IndividualType;
import orar.util.MapOperator;

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
		Set<Integer> processedIndividuals = new HashSet<>();
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
				Set<OWLObjectProperty> preRoles = getPreRoles(sameIndsOfCurrentIndividual, orarOntology);
				Set<OWLObjectProperty> sucRoles = getSuccRoles(sameIndsOfCurrentIndividual, orarOntology);

				// /*
				// * small optimization: update also concept for all same
				// insividuals. It will reduce number of refinement steps
				// */
//				 for (Integer eachInd : sameIndsOfCurrentIndividual) {
//				 orarOntology.addManyConceptAssertions(eachInd, concepts);
//				 }
//				 
//				 for (Integer eachInd : sameIndsOfCurrentIndividual) {
//					 orarOntology.addManyConceptAssertions(eachInd, concepts);
//					 }
				 orarOntology.addManyConceptAssertions(currentIndividual, concepts);
				// create type and add to the resulting map
				IndividualType type = new BasicIndividualType(concepts, preRoles, sucRoles);

				// Map type to a set of individuals

				// MapOperator.addValuesToMap(typeMap2Individuals, type,
				// sameIndsOfCurrentIndividual);
				MapOperator.addValueToMap(typeMap2Individuals, type, currentIndividual);
				// Map individual to its type
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
	private Set<OWLObjectProperty> getSuccRoles(Set<Integer> individuals, OrarOntology2 orarOntology) {
		Set<OWLObjectProperty> accumulatedRoles = new HashSet<OWLObjectProperty>();
		for (Integer ind : individuals) {

			Set<OWLObjectProperty> sucRoles = orarOntology.getSuccessorRoleAssertionsAsMap(ind).keySet();

			accumulatedRoles.addAll(sucRoles);
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
	private Set<OWLObjectProperty> getPreRoles(Set<Integer> individuals, OrarOntology2 orarOntology) {
		Set<OWLObjectProperty> accumulatedRoles = new HashSet<OWLObjectProperty>();
		for (Integer ind : individuals) {

			Set<OWLObjectProperty> sucRoles = orarOntology.getPredecessorRoleAssertionsAsMap(ind).keySet();

			accumulatedRoles.addAll(sucRoles);
		}
		return accumulatedRoles;

	}

}