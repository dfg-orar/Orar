package orar.ruleengine;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.data.MetaDataOfOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;

public class FunctionalityRuleExecutor implements RuleExecutor {

	private final Set<Set<Integer>> newSameasAssertions;
	private final OrarOntology2 orarOntology;
	private final MetaDataOfOntology metaDataOfOntology;
	// private final Logger logger =
	// Logger.getLogger(FunctionalityRuleExecutor.class);
	Logger logger = Logger.getLogger(FunctionalityRuleExecutor.class);

	public FunctionalityRuleExecutor(OrarOntology2 orarOntology) {
		this.orarOntology = orarOntology;
		this.newSameasAssertions = new HashSet<Set<Integer>>();
		this.metaDataOfOntology = MetaDataOfOntology.getInstance();

	}

	@Override
	public void materialize() {
//		long startTime = System.currentTimeMillis();
		// for functional roles
		mergeSuccessorsOfFunctionalRole();
		mergePredecessorsOfInvFunctionalRole();
//		long endTime = System.currentTimeMillis();
//		long time = (endTime - startTime) / 1000;
//		logger.info("time in materializer step: " + time);
	}

	private void mergePredecessorsOfInvFunctionalRole() {
		// for inverse functional roles
		Set<OWLObjectProperty> allInvFuncRoles = this.metaDataOfOntology.getInverseFunctionalRoles();
		// logger.info("********DEBUG**** allInvFuncRoles: "+allInvFuncRoles);
		for (OWLObjectProperty eachInvFuncRole : allInvFuncRoles) {
			Set<Integer> allObjects = this.orarOntology.getObjectsInRoleAssertions(eachInvFuncRole);
			// logger.info("********DEBUG**** allObjects: "+allObjects);
			for (Integer eachObject : allObjects) {
				Set<Integer> allSubjects = this.orarOntology.getPredecessorsTakingEqualityIntoAccount(eachObject,
						eachInvFuncRole);
				// logger.info("********DEBUG**** allSubjects: "+allSubjects);
				if (allSubjects.size() > 1) {
					this.newSameasAssertions.add(allSubjects);
//					addConceptNamesWrtNewSameIndividuals(allSubjects);
				}
			}
		}

	}

	private void addConceptNamesWrtNewSameIndividuals(Set<Integer> sameIndividuals) {
		Set<OWLClass> allConcepts = new HashSet<OWLClass>();
		/*
		 * collect all concepts
		 */
		for (Integer eachInd : sameIndividuals) {
			allConcepts.addAll(this.orarOntology.getAssertedConcepts(eachInd));
		}

		/*
		 * add concepts
		 */
		for (Integer eachInd : sameIndividuals) {
			this.orarOntology.addManyConceptAssertions(eachInd, allConcepts);
		}

	}

	private void mergeSuccessorsOfFunctionalRole() {
		Set<OWLObjectProperty> allFuncRoles = this.metaDataOfOntology.getFunctionalRoles();
		for (OWLObjectProperty eachFuncRole : allFuncRoles) {
			// logger.info("functional role: " + eachFuncRole);
			Set<Integer> allSubjects = this.orarOntology.getSubjectsInRoleAssertions(eachFuncRole);
			for (Integer eachSubject : allSubjects) {
				// logger.info("each Subject: " + eachSubject);
				Set<Integer> allObjects = this.orarOntology.getSuccessorsTakingEqualityIntoAccount(eachSubject,
						eachFuncRole);
				// logger.info("all objects:" + allObjects);
				if (allObjects.size() > 1) {
					this.newSameasAssertions.add(allObjects);
//					addConceptNamesWrtNewSameIndividuals(allObjects);
				}
			}
		}

	}

	@Override
	public void incrementalMaterialize(Set<Integer> setOfSameasIndividuals) {
		// logger.info("FunctionalityRuleExecutor.incrementalMaterialize is
		// called");
		mergeSuccessorsForFunctionalRoles(setOfSameasIndividuals);
		mergePredecessorsForFunctionalRoles(setOfSameasIndividuals);
	}

	private void mergePredecessorsForFunctionalRoles(Set<Integer> setOfSameasIndividuals) {
		Set<OWLObjectProperty> allInvFuncRoles = this.metaDataOfOntology.getInverseFunctionalRoles();
		for (OWLObjectProperty eachInvFuncRole : allInvFuncRoles) {
			Set<Integer> allSubjects = new HashSet<Integer>();
			for (Integer eachIndividual : setOfSameasIndividuals) {
				Set<Integer> subjects = this.orarOntology.getPredecessors(eachIndividual, eachInvFuncRole);
				allSubjects.addAll(subjects);
			}
			if (allSubjects.size() > 1) {
				this.newSameasAssertions.add(allSubjects);
//				addConceptNamesWrtNewSameIndividuals(allSubjects);
			}
		}

	}

	private void mergeSuccessorsForFunctionalRoles(Set<Integer> setOfSameasIndividuals) {
		Set<OWLObjectProperty> allFuncRoles = this.metaDataOfOntology.getFunctionalRoles();
		for (OWLObjectProperty eachFuncRole : allFuncRoles) {
			Set<Integer> allObjects = new HashSet<Integer>();
			for (Integer eachIndividual : setOfSameasIndividuals) {
				Set<Integer> objects = this.orarOntology.getSuccessorsTakingEqualityIntoAccount(eachIndividual,
						eachFuncRole);
				allObjects.addAll(objects);
			}
			if (allObjects.size() > 1) {
				this.newSameasAssertions.add(allObjects);
//				addConceptNamesWrtNewSameIndividuals(allObjects);
			}
		}
	}

	@Override
	public void incrementalMaterialize(IndexedRoleAssertion roleAssertion) {
		Integer subject = roleAssertion.getSubject();
		OWLObjectProperty role = roleAssertion.getRole();
		Integer object = roleAssertion.getObject();
		if (this.metaDataOfOntology.getFunctionalRoles().contains(role)) {
			mergeSuccessorsForFunctaionalRole(role, subject, object);
		}

	}

	private void mergeSuccessorsForFunctaionalRole(OWLObjectProperty role, Integer subject, Integer object) {
		Set<Integer> allObjects = this.orarOntology.getSuccessorsTakingEqualityIntoAccount(subject, role);
		allObjects.add(object);
		if (allObjects.size() > 1) {
			this.newSameasAssertions.add(allObjects);
//			addConceptNamesWrtNewSameIndividuals(allObjects);
		}

	}

	@Override
	public Set<Set<Integer>> getNewSameasAssertions() {

		return this.newSameasAssertions;
	}

	@Override
	public Set<IndexedRoleAssertion> getNewRoleAssertions() {
		// return nothings
		return (new HashSet<IndexedRoleAssertion>());
	}

	@Override
	public boolean isABoxExtended() {
		// we don't check it here. So we return false.
		return false;
	}

	@Override
	public void clearOldBuffer() {
		this.newSameasAssertions.clear();
	}

}
