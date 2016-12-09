package orar.innerreasoner.HornSHOIF;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.config.DebugLevel;
import orar.innerreasoner.InnerReasonerTemplate;
import orar.util.PrintingHelper;

public abstract class HornSHOIF_InnerReasonerTemplate extends InnerReasonerTemplate {
	private Logger logger = Logger.getLogger(HornSHOIF_InnerReasonerTemplate.class);

	public HornSHOIF_InnerReasonerTemplate(OWLOntology owlOntology) {
		super(owlOntology);
	}

	/**
	 * compute assertions by R_t^2, (N(u)-->T(u,u))
	 */
	@Override
	protected void computeRoleAssertionForInstancesOfLoopConcepts() {
		Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>(this.instancesOfLoopConcepts);
		/*
		 * retain only U-individuals.
		 */
		individuals.retainAll(this.abstractDataFactory.getUAbstractIndividuals());
		if (config.getDebuglevels().contains(DebugLevel.ADDING_MARKING_AXIOMS)) {
			logger.info("***DEBUG*** individuals U are instances of loop concepts:");
			PrintingHelper.printSet(individuals);
		}
		for (OWLNamedIndividual eachU : individuals) {
			for (OWLObjectProperty tranRol : this.metadataOfOntology.getTransitiveRoles()) {
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(eachU, tranRol).getFlattened();
				if (objects.contains(eachU)) {
					this.roleAssertionList.addLoopRoleAssertion(eachU, tranRol);
				}
			}
		}
	}

	/**
	 * compute assertions by R^3_t: N(u), M(x) -->T(u,x)
	 */
	@Override
	protected void computeRoleAssertionForInstancesOfConceptHasTranRole() {
		Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>(this.instancesOfHasTranConcepts);
		/*
		 * retain only U-individuals.
		 */
		individuals.retainAll(this.abstractDataFactory.getUAbstractIndividuals());

		for (OWLNamedIndividual eachU : individuals) {
			for (OWLObjectProperty tranRole : this.metadataOfOntology.getTransitiveRoles()) {
				/*
				 * query for tranRole
				 */
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(eachU, tranRole).getFlattened();
				/*
				 * retain only x-individuals
				 */
				objects.retainAll(this.abstractDataFactory.getXAbstractIndividuals());

				for (OWLNamedIndividual eachObject : objects) {
					this.roleAssertionList.addUX_RoleAssertionForCTypeAndType(eachU, tranRole, eachObject);
				}

				/*
				 * query for inverse of tranRole
				 */
				OWLObjectInverseOf invTranRole = this.dataFactory.getOWLObjectInverseOf(tranRole);
				Set<OWLNamedIndividual> subjects = reasoner.getObjectPropertyValues(eachU, invTranRole).getFlattened();
				/*
				 * retain only x-individuals
				 */
				subjects.retainAll(this.abstractDataFactory.getXAbstractIndividuals());

				for (OWLNamedIndividual eachSubject : subjects) {
					this.roleAssertionList.addUX_RoleAssertionForCTypeAndType(eachSubject, tranRole, eachU);
				}

			}
		}
	}

	@Override
	protected void computeRoleAssertionForInstancesOfSingletonConcept() {
		Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>(this.instancesOfSingletonConcepts);
		for (Set<OWLNamedIndividual> eachSetOfNominals:this.nominalConceptMap2Instances.values()){
		individuals.addAll(eachSetOfNominals);
		}
		if (config.getDebuglevels().contains(DebugLevel.ADDING_MARKING_AXIOMS)) {
			logger.info("***DEBUG*** individuals are instances of singleton concepts:");
			PrintingHelper.printSet(individuals);
		}
		/*
		 * retain only U-individuals.
		 */
		individuals.retainAll(this.abstractDataFactory.getUAbstractIndividuals());

		if (config.getDebuglevels().contains(DebugLevel.ADDING_MARKING_AXIOMS)) {
			logger.info("***DEBUG*** individuals U are instances of singleton concepts:");
			PrintingHelper.printSet(individuals);
		}
		for (OWLNamedIndividual eachU : individuals) {
			Set<OWLObjectProperty> allRoles = this.owlOntology.getObjectPropertiesInSignature(true);
			allRoles.remove(this.dataFactory.getOWLTopObjectProperty());
			allRoles.remove(this.dataFactory.getOWLBottomObjectProperty());
			for (OWLObjectProperty role : allRoles) {
				/*
				 * query for assertion of the form role(eachU, ?x)
				 */
				Set<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(eachU, role).getFlattened();

				/*
				 * retain only x-individuals
				 */
				objects.retainAll(this.abstractDataFactory.getXAbstractIndividuals());
				for (OWLNamedIndividual eachObject : objects) {
					this.roleAssertionList.addUX_RoleAssertionForCTypeAndType(eachU, role, eachObject);
				}

				// logger.info("***DEBUG*** pause 1");
				// Pause.pause();
				/*
				 * query for assertion of the form role(x, eachU)
				 */
				OWLObjectInverseOf inverseRole = this.dataFactory.getOWLObjectInverseOf(role);
				// logger.info("inverseROle:"+inverseRole);
				// logger.info("u:"+eachU);
				// logger.info("before asering for inver rolse asesrtoin");
				// Pause.pause();
				Set<OWLNamedIndividual> subjects = reasoner.getObjectPropertyValues(eachU, inverseRole).getFlattened();
				// logger.info("after asering for inver rolse asesrtoin");
				// Pause.pause();
				// logger.info("subjects: " +subjects);

				/*
				 * retain only x-individuals
				 */
				subjects.retainAll(this.abstractDataFactory.getXAbstractIndividuals());
				for (OWLNamedIndividual eachSubject : subjects) {
					this.roleAssertionList.addUX_RoleAssertionForCTypeAndType(eachSubject, role, eachU);
				}
				// logger.info("***DEBUG*** pause 2: after query for inverse
				// role asesrtions");
				// Pause.pause();
			}
		}
	}

	/**
	 * compute sameas assertions between representative of concept-types and
	 * those of types.
	 */
	@Override
	protected void computeEntailedSameasAssertions() {
		// Set<OWLNamedIndividual> allIndividualsFromConceptType =
		// this.abstractDataFactory.getUAbstractIndividuals();
		// logger.info("***DEBUG***number of u
		// individuals:"+this.abstractDataFactory.getUAbstractIndividuals().size());
		Set<OWLNamedIndividual> allIndividualsFromConceptType = this.owlOntology.getIndividualsInSignature();
		allIndividualsFromConceptType.retainAll(this.abstractDataFactory.getUAbstractIndividuals());
		// logger.info("***DEBUG***number of u individuals in the ontology:"+
		// allIndividualsFromConceptType.size());
		Queue<OWLNamedIndividual> todoIndividuals = new LinkedList<OWLNamedIndividual>(allIndividualsFromConceptType);
		while (!todoIndividuals.isEmpty()) {
			OWLNamedIndividual anIndividual = todoIndividuals.poll();
			Set<OWLNamedIndividual> equivalentIndividuals = reasoner.getSameIndividuals(anIndividual).getEntities();
			/*
			 * Note to remove the indv itself as we DONT use (u=u) to transfer
			 * assertions.
			 */
			equivalentIndividuals.remove(anIndividual);
			if (!equivalentIndividuals.isEmpty()) {
				this.sameAsMap.put(anIndividual, equivalentIndividuals);
				// don't need to query for equivalentIndividuals. So we remove
				// them from todoIndividuals.
				todoIndividuals.removeAll(equivalentIndividuals);
			}
		}

	}

}
