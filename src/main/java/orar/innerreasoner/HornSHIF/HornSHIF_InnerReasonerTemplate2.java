package orar.innerreasoner.HornSHIF;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasonerTemplate;
import orar.innerreasoner.InnerReasonerTemplate2;

public abstract class HornSHIF_InnerReasonerTemplate2 extends InnerReasonerTemplate2 {

	public HornSHIF_InnerReasonerTemplate2(OWLOntology owlOntology) {
		super(owlOntology);
	}

	/**
	 * compute assertions by R_t^2, (N(u)-->T(u,u))
	 */
	@Override
	protected void computeRoleAssertionForInstancesOfLoopConcepts() {
		Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>(this.instancesOfLoopConcepts);
		/*
		 * retain only X-individuals.
		 */
		individuals.retainAll(this.abstractDataFactory.getXAbstractIndividuals());
		for (OWLNamedIndividual eachX : individuals) {
			for (OWLObjectProperty tranRol : this.metadataOfOntology.getTransitiveRoles()) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(eachX, tranRol).getFlattened();
				if (objects.contains(eachX)) {
					this.roleAssertionList.addLoopRoleAssertion(eachX, tranRol);
				}
			}
		}
	}

	/**
	 * compute assertions by R^3_t: N(u), M(x) -->T(u,x)
	 */
	@Override
	protected void computeRoleAssertionForInstancesOfConceptHasTranRole() {
		// Do nothing in case of Horn-SHIF
	}

	@Override
	protected void computeRoleAssertionForInstancesOfSingletonConcept() {
		// Do nothing in case of Horn-SHIF
	}

	/**
	 * compute sameas assertions between representative of concept-types and
	 * those of types.
	 */
	@Override
	protected void computeEntailedSameasAssertions() {
		// Do nothing in case of Horn-SHIF

	}
	@SuppressWarnings("Do not use, onlty for compatiablity")
	public Map<OWLNamedIndividual, Set<OWLClass>> getEntailedConceptAssertionsAsMap() {

		return new HashMap<OWLNamedIndividual, Set<OWLClass>>();
	}

}
