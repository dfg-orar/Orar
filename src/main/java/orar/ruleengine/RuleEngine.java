package orar.ruleengine;

import java.util.Set;

import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;

public interface RuleEngine {

	public void materialize();

	public void incrementalMaterialize();

	public OrarOntology2 getOntology();

	public void addTodoSameasAssertions(Set<Set<Integer>> todoSameasAssertions);

	public void addTodoRoleAsesrtions(Set<IndexedRoleAssertion> odoRoleAssertions);

	public long getReasoningTime();
}
