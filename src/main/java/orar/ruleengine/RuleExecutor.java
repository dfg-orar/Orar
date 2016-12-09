package orar.ruleengine;

import java.util.Set;

import orar.modeling.roleassertion2.IndexedRoleAssertion;

public interface RuleExecutor {

	public void materialize();

	public void incrementalMaterialize(Set<Integer> setOfSameasIndividuals);

	public void incrementalMaterialize(IndexedRoleAssertion roleAssertion);

	public Set<Set<Integer>> getNewSameasAssertions();

	public Set<IndexedRoleAssertion> getNewRoleAssertions();

	public boolean isABoxExtended();

	public void clearOldBuffer();
	public void setIncrementalAfterFirstAbstraction();
}
