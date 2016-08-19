package orar.ruleengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;

public class SameasRuleExecutor implements RuleExecutor {

	private final OrarOntology2 orarOntology;
	private boolean isABoxExtended;
	// private Queue<Set<OWLNamedIndividual>> localTodoSameas;
	//	private final Logger logger = Logger.getLogger(SameasRuleExecutor.class);
	Logger logger=Logger.getLogger(SameasRuleExecutor.class);
	public SameasRuleExecutor(OrarOntology2 orarOntology) {
		this.orarOntology = orarOntology;
		this.isABoxExtended = false;
	}

	@Override
	public void materialize() {
		long startTime = System.currentTimeMillis();
		// compute connect components
		List<Set<Integer>> components = computeConnectedComponents();
		// put components to the map.
		for (Set<Integer> component : components) {
			for (Integer ind : component) {
				if (this.orarOntology.addManySameAsAssertions(ind, component)) {
					this.isABoxExtended = true;
				}
			}
		}
		long endTime = System.currentTimeMillis();
		long time = (endTime-startTime)/1000;
		logger.info("time in materializer step: "+ time);
	}

	private List<Set<Integer>> computeConnectedComponents() {
		Set<Integer> allIndividualsInSameasMap = this.orarOntology.getSameasBox().getAllIndividuals();
		Queue<Integer> todoIndividuals = new LinkedList<Integer>(allIndividualsInSameasMap);
		List<Set<Integer>> components = new ArrayList<Set<Integer>>();
		while (!todoIndividuals.isEmpty()) {
			Integer a = todoIndividuals.poll();
			// compute component for each individual a in todoIndividuals.
			Set<Integer> newComponent = new HashSet<Integer>();
			newComponent.add(a);
			Stack<Integer> stackForDFS = new Stack<Integer>();
			stackForDFS.push(a);
			while (!stackForDFS.isEmpty()) {
				Integer ind = stackForDFS.pop();
				Set<Integer> sameasOf_a = this.orarOntology.getSameIndividuals(ind);
				sameasOf_a.removeAll(newComponent);
				newComponent.addAll(sameasOf_a);
				stackForDFS.addAll(sameasOf_a);
				todoIndividuals.removeAll(sameasOf_a);
			}
			components.add(newComponent);
		}
		return components;
	}

	@Override
	public Set<Set<Integer>> getNewSameasAssertions() {
		// return empty set in this rule
		return new HashSet<Set<Integer>>();
	}

	@Override
	public Set<IndexedRoleAssertion> getNewRoleAssertions() {
		// return empty set in this rule
		return new HashSet<>();
	}

	@Override
	public boolean isABoxExtended() {

		return this.isABoxExtended;
	}

	@Override
	public void incrementalMaterialize(Set<Integer> setOfSameasIndividuals) {
		// logger.info("SameasRuleExecutor.incrementalMaterialize");
		// get union of equivalent individuals in the set.
		Set<Integer> accumulatedSameasIndividuals = new HashSet<Integer>();
		accumulatedSameasIndividuals.addAll(setOfSameasIndividuals);
		for (Integer ind : setOfSameasIndividuals) {
			accumulatedSameasIndividuals.addAll(this.orarOntology.getSameIndividuals(ind));
		}
		// update the map
		for (Integer eachIndividual : accumulatedSameasIndividuals) {
			if (this.orarOntology.addManySameAsAssertions(eachIndividual, accumulatedSameasIndividuals)) {
				this.isABoxExtended = true;
			}
		}
	}

	@Override
	public void clearOldBuffer() {
		// nothing to clear

	}

	@Override
	public void incrementalMaterialize(IndexedRoleAssertion roleAssertion) {
		// nothing to to

	}
}
