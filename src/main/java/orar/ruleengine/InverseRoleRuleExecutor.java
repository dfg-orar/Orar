package orar.ruleengine;

import java.io.IOException;
import java.security.spec.EncodedKeySpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;

import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;
import orar.modeling.roleassertion2.IndexedRoleAssertionList;
import orar.refinement.abstractroleassertion.RoleAssertionList;
import orar.rolereasoning.AxiomOfSpecificTypeGetter;
import x.util.Pause;
import x.util.PrintingHelper;

public class InverseRoleRuleExecutor implements RuleExecutor {
	private static final Logger logger = Logger.getLogger(InverseRoleRuleExecutor.class);
	private final Set<IndexedRoleAssertion> newRoleAssertions;
	
	private final OrarOntology2 orarOntology;
	private final OWLDataFactory dataFactory;
	private boolean isABoxExtended;
	
	private IndexedRoleAssertionList entailedRoleAssertionFromInverseAxioms;
	/**
	 * map: role --> its inverses
	 */
	private Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> inverseRoleMap;

	public InverseRoleRuleExecutor(OrarOntology2 orarOntology) {
		this.orarOntology = orarOntology;
		this.newRoleAssertions = new HashSet<IndexedRoleAssertion>();
		this.dataFactory = OWLManager.getOWLDataFactory();
		this.isABoxExtended = false;
		this.inverseRoleMap = new HashMap<OWLObjectProperty, Set<OWLObjectPropertyExpression>>();
		this.entailedRoleAssertionFromInverseAxioms= new IndexedRoleAssertionList();
		/*
		 * Create a map from each atomic role to its inverses
		 */
		createMapFromRoleToItsInverses();
	}

	@Override
	public void materialize() {
//		long startTime = System.currentTimeMillis();
		/*
		 * For each role R and each inverese role S of R: R(a,b) ---> add
		 * S(b,a). Be careful as S can also be an inverse of an atomic role.
		 */

		Set<OWLObjectProperty> allKeys = this.inverseRoleMap.keySet();
		for (OWLObjectProperty R : allKeys) {
			Set<Integer> allSubjectsOf_R = this.orarOntology.getSubjectsInRoleAssertions(R);
			for (Integer eachSubjectOf_R : allSubjectsOf_R) {
				Set<Integer> allObjectsOf_R = this.orarOntology.getSuccessors(eachSubjectOf_R, R);
				Set<OWLObjectPropertyExpression> allInverseOf_R = this.inverseRoleMap.get(R);

				for (OWLObjectPropertyExpression inverseOf_R : allInverseOf_R) {
					// case of atomic role
					if (inverseOf_R instanceof OWLObjectProperty) {
						OWLObjectProperty atomicInverseOf_R = inverseOf_R.asOWLObjectProperty();
						for (Integer eachObjectOf_R : allObjectsOf_R) {
							this.entailedRoleAssertionFromInverseAxioms.addRoleAssertion(
									eachObjectOf_R, atomicInverseOf_R, eachSubjectOf_R);
//							addRoleAssertion(eachObjectOf_R, atomicInverseOf_R, eachSubjectOf_R);
						}
					}
					// case of inverse role
					if (inverseOf_R instanceof OWLObjectInverseOf) {
						OWLObjectProperty inverseOfEacSuperRoleOf_R = inverseOf_R.getNamedProperty();
						for (Integer eachObjectOf_R : allObjectsOf_R) {
							this.entailedRoleAssertionFromInverseAxioms.addRoleAssertion(
							eachSubjectOf_R, inverseOfEacSuperRoleOf_R, eachObjectOf_R);
//							addRoleAssertion(eachSubjectOf_R, inverseOfEacSuperRoleOf_R, eachObjectOf_R);
						}
					}

				}
			}
		}
//		long endObtainingNewRoles = System.currentTimeMillis();
//		logger.info("Time for obtaining role assertions wrt the invesr role axioms: "
//				+ (endObtainingNewRoles - startTime) / 1000);
		/*
		 * Add entailed role assertions got from inverse role assertion
		 */
		logger.info("number of possibly new role assertions "+this.entailedRoleAssertionFromInverseAxioms.getSize());
		for (int i = 0; i < this.entailedRoleAssertionFromInverseAxioms.getSize(); i++) {
			addRoleAssertion(this.entailedRoleAssertionFromInverseAxioms.getSubject(i),
					this.entailedRoleAssertionFromInverseAxioms.getRole(i),
					this.entailedRoleAssertionFromInverseAxioms.getObject(i));
		}
//		long insertingTimeNow = System.currentTimeMillis();
//		logger.info("Time for inserting new role assertions wrt the role hierarchy: "
//				+ (insertingTimeNow - endObtainingNewRoles) / 1000);
//		long endTime = System.currentTimeMillis();
//		long time = (endTime-startTime)/1000;
//		logger.info("number of new role assertions "+this.newRoleAssertions.size());
//		logger.info("time in materializer step: "+ time);
	}

	private void addRoleAssertion(Integer subject, OWLObjectProperty eacAtomicSuperRoleOf_R,
			Integer eachObjectOf_R) {
		if (this.orarOntology.addRoleAssertion(subject, eacAtomicSuperRoleOf_R, eachObjectOf_R)) {
			this.isABoxExtended = true;

			 IndexedRoleAssertion newRoleAssertion = new IndexedRoleAssertion( subject,eacAtomicSuperRoleOf_R, eachObjectOf_R);
			this.newRoleAssertions.add(newRoleAssertion);

		}

	}

	private void createMapFromRoleToItsInverses() {
		Set<OWLInverseObjectPropertiesAxiom> allInverseRoleAxioms = AxiomOfSpecificTypeGetter
				.getInverseObjectPropertyAxioms(orarOntology);
		
//		logger.info("***DEBUG*** allInverseRoleAxioms:"+allInverseRoleAxioms);
//		Pause.pause();
		
		for (OWLInverseObjectPropertiesAxiom invroleAxiom : allInverseRoleAxioms) {
			OWLObjectPropertyExpression left = invroleAxiom.getFirstProperty().getSimplified();
			OWLObjectPropertyExpression right = invroleAxiom.getSecondProperty().getSimplified();

			// add entry for left
			if (left instanceof OWLObjectProperty) {
				addEntryToIverseRoleMap(left.asOWLObjectProperty(), right);
			} else if (left instanceof OWLObjectInverseOf) {
				addEntryToIverseRoleMap(left.getNamedProperty(), dataFactory.getOWLObjectInverseOf(right));
			}

			// add entry for right
			if (right instanceof OWLObjectProperty) {
				addEntryToIverseRoleMap(right.asOWLObjectProperty(), left);
			} else if (right instanceof OWLObjectInverseOf) {
				addEntryToIverseRoleMap(right.getNamedProperty(), dataFactory.getOWLObjectInverseOf(left));
			}

		}
		logger.info("Inverse role maps:");
		PrintingHelper.printMap(this.inverseRoleMap);
	}

	/**
	 * add value to the existing value of the key.
	 * 
	 * @param key
	 * @param value
	 */
	private void addEntryToIverseRoleMap(OWLObjectProperty key, OWLObjectPropertyExpression value) {
		Set<OWLObjectPropertyExpression> existingValues = this.inverseRoleMap.get(key);
		if (existingValues == null) {
			existingValues = new HashSet<OWLObjectPropertyExpression>();
		}
		existingValues.add(value);
		this.inverseRoleMap.put(key, existingValues);
	}

	@Override
	public void incrementalMaterialize(Set<Integer> setOfSameasIndividuals) {
		// nothing to do

	}

	@Override
	public void incrementalMaterialize(IndexedRoleAssertion roleAssertion) {
		OWLObjectProperty role = roleAssertion.getRole().asOWLObjectProperty();
		Integer subject = roleAssertion.getSubject();
		Integer object = roleAssertion.getObject();

		Set<? extends OWLObjectPropertyExpression> allInverseRoles = this.inverseRoleMap.get(role);
		if (allInverseRoles != null) {
			for (OWLObjectPropertyExpression eachInverseRole : allInverseRoles) {
				if (eachInverseRole instanceof OWLObjectProperty) {
					addRoleAssertion(object, eachInverseRole.asOWLObjectProperty(), subject);
				}

				if (eachInverseRole instanceof OWLObjectInverseOf) {

					addRoleAssertion(subject, eachInverseRole.getNamedProperty(), object);
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
