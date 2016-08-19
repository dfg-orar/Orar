package orar.abstraction.HornSHIF;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.abstraction.AbstractionGeneratorTemplate;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;

public class HornSHIF_AbstractionGenerator extends AbstractionGeneratorTemplate {

	public HornSHIF_AbstractionGenerator(OrarOntology2 orarOntology,
			Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		super(orarOntology, typeMap2Individuals);

	}

	@Override
	protected Set<OWLAxiom> getConceptAssertionsForConceptType(IndividualType type) {
		/*
		 * for HornSHIF, there is no need for concept-type
		 */
		return new HashSet<OWLAxiom>();
	}

	@Override
	protected void markXHavingFunctionalRole(OWLNamedIndividual xIndividual, IndividualType type) {
		Set<OWLObjectProperty> sucRoles = type.getSuccessorRoles();
		/*
		 * if sucRoles contains some functional roles
		 */
		if (intersectionNotEmpty(sucRoles, this.metaDataOfOntology.getFunctionalRoles())) {
			this.sharedMap.getxAbstractHavingFunctionalRole().add(xIndividual);
		}

	}

	@Override
	protected void markZHavingInverseFunctionalRole(OWLNamedIndividual zIndividual, OWLObjectProperty role) {
		if (this.metaDataOfOntology.getInverseFunctionalRoles().contains(role)) {
			this.sharedMap.getzAbstractHavingInverseFunctionalRole().add(zIndividual);
		}

	}

	/**
	 * check if intersection of two set is not empty without modifying the sets.
	 * 
	 * @param set1
	 * @param set2
	 * @return true if the intersection of the two sets is not empty, false
	 *         otherwise
	 */
	private <T> boolean intersectionNotEmpty(Set<T> set1, Set<T> set2) {
		HashSet<T> copySet1 = new HashSet<T>(set1);
		copySet1.retainAll(set2);
		return (!copySet1.isEmpty());
	}

}
