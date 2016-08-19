package orar.ruleengine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.data.MetaDataOfOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;

public class TransitivityRuleExecutor implements RuleExecutor {
	private final Set<IndexedRoleAssertion> newRoleAssertions;
	private final OrarOntology2 orarOntology;
	private final MetaDataOfOntology metaDataOfOntology;
	private final OWLDataFactory dataFactory;
	private boolean isABoxExtended;

	private final Logger logger = Logger.getLogger(TransitivityRuleExecutor.class);

	public TransitivityRuleExecutor(OrarOntology2 orarOntology) {
		this.orarOntology = orarOntology;
		this.newRoleAssertions = new HashSet<>();
		this.metaDataOfOntology = MetaDataOfOntology.getInstance();
		this.dataFactory = OWLManager.getOWLDataFactory();
		this.isABoxExtended = false;
	}

	@Override
	public void materialize() {
		long startTime = System.currentTimeMillis();
		
		// get all transitive role assertions.
		Queue<IndexedRoleAssertion> todoTranRoleAssertions = getAllTransitiveRoleAssertions();
		// logger.info("all tran role assertions:"+todoTranRoleAssertions);
		while (!todoTranRoleAssertions.isEmpty()) {
			IndexedRoleAssertion roleT_a_b = todoTranRoleAssertions.poll();
			Integer a = roleT_a_b.getSubject();
			Integer b = roleT_a_b.getObject();
			OWLObjectProperty T = roleT_a_b.getRole().asOWLObjectProperty();
			Set<Integer> many_c = this.orarOntology.getSuccessorsTakingEqualityIntoAccount(b, T);
			for (Integer c : many_c) {
				if (this.orarOntology.addRoleAssertion(a, T, c)) {
					this.isABoxExtended = true;
					IndexedRoleAssertion newRoleT_a_c = new IndexedRoleAssertion(a, T, c);
					this.newRoleAssertions.add(newRoleT_a_c);
					todoTranRoleAssertions.add(newRoleT_a_c);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		long time = (endTime-startTime)/1000;
		logger.info("time in materializer step: "+ time);
	}

	private Queue<IndexedRoleAssertion> getAllTransitiveRoleAssertions() {
		Queue<IndexedRoleAssertion> transRoleAssertions = new LinkedList<IndexedRoleAssertion>();
		Set<OWLObjectProperty> allTranRoles = this.metaDataOfOntology.getTransitiveRoles();
		for (OWLObjectProperty eachTranRole : allTranRoles) {
			Set<Integer> subjects = this.orarOntology.getSubjectsInRoleAssertions(eachTranRole);
			for (Integer eachSubject : subjects) {
				Set<Integer> objects = this.orarOntology.getSuccessors(eachSubject, eachTranRole);
				for (Integer eachObject : objects) {
					transRoleAssertions.add(new IndexedRoleAssertion(eachSubject, eachTranRole, eachObject));
				}
			}
		}
		return transRoleAssertions;
	}

	@Override
	public void incrementalMaterialize(Set<Integer> setOfSameasIndividuals) {
		Set<OWLObjectProperty> allTranRoles = this.metaDataOfOntology.getTransitiveRoles();
		for (OWLObjectProperty eachTranRole : allTranRoles) {
			Set<Integer> allPredecessors = new HashSet<Integer>();
			Set<Integer> allSuccessors = new HashSet<Integer>();
			for (Integer eachIndividual : setOfSameasIndividuals) {
				allPredecessors.addAll(
						this.orarOntology.getPredecessorsTakingEqualityIntoAccount(eachIndividual, eachTranRole));
				allSuccessors
						.addAll(this.orarOntology.getSuccessorsTakingEqualityIntoAccount(eachIndividual, eachTranRole));
			}
			for (Integer eachPre : allPredecessors) {
				for (Integer eachSuc : allSuccessors) {
					if (this.orarOntology.addRoleAssertion(eachPre, eachTranRole, eachSuc)) {
						this.isABoxExtended = true;
						this.newRoleAssertions.add(new IndexedRoleAssertion(eachPre, eachTranRole, eachSuc));
					}
				}
			}
		}

	}

	@Override
	public void incrementalMaterialize(IndexedRoleAssertion roleAssertion) {
		Integer a = roleAssertion.getSubject();
		Integer b = roleAssertion.getObject();
		OWLObjectProperty T = roleAssertion.getRole().asOWLObjectProperty();

		// T(a,b), T(b,c) --> T(a,c)
		Set<Integer> many_c = this.orarOntology.getSuccessorsTakingEqualityIntoAccount(b, T);
		for (Integer c : many_c) {
			if (this.orarOntology.addRoleAssertion(a, T, c)) {
				this.isABoxExtended = true;
				IndexedRoleAssertion newRoleT_a_c = new IndexedRoleAssertion(a, T, c);
				this.newRoleAssertions.add(newRoleT_a_c);

			}
		}

		// T(d,a),T(a,b) --> T(d,b)
		Set<Integer> many_d = this.orarOntology.getPredecessors(a, T);
		for (Integer d : many_d) {
			if (this.orarOntology.addRoleAssertion(d, T, b)) {
				this.isABoxExtended = true;
				IndexedRoleAssertion newRoleT_d_b = new IndexedRoleAssertion(d, T, b);
				this.newRoleAssertions.add(newRoleT_d_b);

			}
		}
	}

	@Override
	public Set<Set<Integer>> getNewSameasAssertions() {
		// nothing to do with sameas
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
