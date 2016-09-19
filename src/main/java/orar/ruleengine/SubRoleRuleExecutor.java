package orar.ruleengine;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.data.MetaDataOfOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;
import orar.modeling.roleassertion2.IndexedRoleAssertionList;
import x.util.PrintingHelper;

public class SubRoleRuleExecutor implements RuleExecutor {
	private final Logger logger = Logger.getLogger(SubRoleRuleExecutor.class);
	private final Set<IndexedRoleAssertion> newRoleAssertions;
	private final OrarOntology2 orarOntology;
	private final MetaDataOfOntology metaDataOfOntology;

	private boolean isABoxExtended;
	private IndexedRoleAssertionList entailedRoleAssertionFromRoleHierarchy;

	public SubRoleRuleExecutor(OrarOntology2 orarOntology) {
		this.orarOntology = orarOntology;
		this.newRoleAssertions = new HashSet<IndexedRoleAssertion>();
		this.entailedRoleAssertionFromRoleHierarchy = new IndexedRoleAssertionList();

		this.metaDataOfOntology = MetaDataOfOntology.getInstance();

		this.isABoxExtended = false;
	}

	@Override
	public void materialize() {
		long startTime = System.currentTimeMillis();
		Set<OWLObjectProperty> allRolesHavingSuperRoles = this.metaDataOfOntology.getSubRoleMap().keySet();

		// logger.info("Role hierarchy:");
		// PrintingHelper.printMap(this.metaDataOfOntology.getSubRoleMap());
		//
		for (OWLObjectProperty R : allRolesHavingSuperRoles) {
			// logger.info("***DEBUG*** R "+ R);
			// Pause.pause();
			Set<Integer> allSubjectsOf_R = this.orarOntology.getSubjectsInRoleAssertions(R);
			// logger.info("***DEBUG***allSubjectsOf_R "+ allSubjectsOf_R);
			// Pause.pause();
			for (Integer eachSubjectOf_R : allSubjectsOf_R) {
				Set<Integer> allObjectsOf_R = this.orarOntology.getSuccessors(eachSubjectOf_R, R);
				Set<? extends OWLObjectPropertyExpression> allSuperRolesOf_R = this.metaDataOfOntology.getSubRoleMap()
						.get(R);
				for (OWLObjectPropertyExpression eachSuperRoleOf_R : allSuperRolesOf_R) {
					// case of atomic role
					if (eachSuperRoleOf_R instanceof OWLObjectProperty) {
						OWLObjectProperty eacAtomicSuperRoleOf_R = eachSuperRoleOf_R.asOWLObjectProperty();
						for (Integer eachObjectOf_R : allObjectsOf_R) {
							// addRoleAssertion(eachSubjectOf_R,
							// eacAtomicSuperRoleOf_R, eachObjectOf_R);
							entailedRoleAssertionFromRoleHierarchy.addRoleAssertion(eachSubjectOf_R,
									eacAtomicSuperRoleOf_R, eachObjectOf_R);
							// logger.info("***DEBUG***add "+ eachSubjectOf_R+
							// ","+ eacAtomicSuperRoleOf_R+ ","+
							// eachObjectOf_R);
							// Pause.pause();
						}
					}
					// case of inverse role
					if (eachSuperRoleOf_R instanceof OWLObjectInverseOf) {
						OWLObjectProperty inverseOfEacSuperRoleOf_R = eachSuperRoleOf_R.getNamedProperty();
						for (Integer eachObjectOf_R : allObjectsOf_R) {
							// addRoleAssertion(eachObjectOf_R,
							// inverseOfEacSuperRoleOf_R, eachSubjectOf_R);
							entailedRoleAssertionFromRoleHierarchy.addRoleAssertion(eachObjectOf_R,
									inverseOfEacSuperRoleOf_R, eachSubjectOf_R);
						}
					}

				}
			}
		}
		long expandingRoleHierarcyTimeNow = System.currentTimeMillis();
		if (Configuration.getInstance().getLogInfos().contains(LogInfo.TIME_STAMP_FOR_EACH_STEP)) {
			logger.info("Time for obtaining role assertions wrt the role hierarchy: "
					+ (expandingRoleHierarcyTimeNow - startTime) / 1000);

			logger.info(
					"number of possibly new role assertions " + this.entailedRoleAssertionFromRoleHierarchy.getSize());
		}
		/*
		 * Add entailed role assertions got from role hierarchy to the ontology
		 */
		for (int i = 0; i < entailedRoleAssertionFromRoleHierarchy.getSize(); i++) {
			addRoleAssertion(entailedRoleAssertionFromRoleHierarchy.getSubject(i),
					entailedRoleAssertionFromRoleHierarchy.getRole(i),
					entailedRoleAssertionFromRoleHierarchy.getObject(i));
		}
		if (Configuration.getInstance().getLogInfos().contains(LogInfo.TIME_STAMP_FOR_EACH_STEP)) {
			logger.info("number of new role assertions for incremental steps " + this.newRoleAssertions.size());
			long insertingTimeNow = System.currentTimeMillis();
			logger.info("Time for inserting new role assertions wrt the role hierarchy: "
					+ (insertingTimeNow - expandingRoleHierarcyTimeNow) / 1000);
		}
	}

	private void addRoleAssertion(Integer eachSubjectOf_R, OWLObjectProperty eacAtomicSuperRoleOf_R,
			Integer eachObjectOf_R) {
		if (this.orarOntology.addRoleAssertion(eachSubjectOf_R, eacAtomicSuperRoleOf_R, eachObjectOf_R)) {
			this.isABoxExtended = true;
			if (isTranOrCountingOrInverseRole(eacAtomicSuperRoleOf_R)) {
				IndexedRoleAssertion newRoleAssertion = new IndexedRoleAssertion(eachSubjectOf_R,
						eacAtomicSuperRoleOf_R, eachObjectOf_R);
				this.newRoleAssertions.add(newRoleAssertion);
			}
		}

	}

	private boolean isTranOrCountingOrInverseRole(OWLObjectProperty role) {
		return (this.metaDataOfOntology.getTransitiveRoles().contains(role)
				|| this.metaDataOfOntology.getFunctionalRoles().contains(role)
				|| this.metaDataOfOntology.getInverseFunctionalRoles().contains(role));

	}

	@Override
	public void incrementalMaterialize(Set<Integer> setOfSameasIndividuals) {
		// nothing to do with sameas
	}

	@Override
	public void incrementalMaterialize(IndexedRoleAssertion roleAssertion) {
		OWLObjectProperty role = roleAssertion.getRole().asOWLObjectProperty();
		Integer subject = roleAssertion.getSubject();
		Integer object = roleAssertion.getObject();
		Set<? extends OWLObjectPropertyExpression> allSuperRoles = this.metaDataOfOntology.getSubRoleMap().get(role);
		if (allSuperRoles != null) {
			for (OWLObjectPropertyExpression eachSuperRole : allSuperRoles) {
				if (eachSuperRole instanceof OWLObjectProperty) {
					addRoleAssertion(subject, eachSuperRole.asOWLObjectProperty(), object);
				}

				if (eachSuperRole instanceof OWLObjectInverseOf) {
					addRoleAssertion(object, eachSuperRole.getNamedProperty(), subject);
				}
			}
		}
	}

	@Override
	public Set<Set<Integer>> getNewSameasAssertions() {
		// return empty set
		return new HashSet<Set<Integer>>();
	}

	@Override
	public Set<IndexedRoleAssertion> getNewRoleAssertions() {
		return this.newRoleAssertions;
	}

	@Override
	public boolean isABoxExtended() {
		return this.isABoxExtended;
	}

	@Override
	public void clearOldBuffer() {
		this.newRoleAssertions.clear();

	}

}
