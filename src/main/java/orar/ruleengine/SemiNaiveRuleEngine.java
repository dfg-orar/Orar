package orar.ruleengine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertion;

public class SemiNaiveRuleEngine implements RuleEngine {
	private Queue<Set<Integer>> todoSameasAssertions;
	private Queue<IndexedRoleAssertion> todoRoleAssertions;
	private final OrarOntology2 orarOntology;
	private final Logger logger = Logger.getLogger(SemiNaiveRuleEngine.class);
	private RuleExecutor sameasRule;
	private RuleExecutor subroRule;
	private RuleExecutor tranRule;
	private RuleExecutor funcRule;
	private RuleExecutor inverseRule;
	private List<RuleExecutor> ruleExecutors;
	private long reasoningTime;

	public SemiNaiveRuleEngine(OrarOntology2 orarOntology) {
		this.orarOntology = orarOntology;
		this.todoSameasAssertions = new LinkedList<Set<Integer>>();
		this.todoRoleAssertions = new LinkedList<IndexedRoleAssertion>();

		this.sameasRule = new SameasRuleExecutor(orarOntology);
		this.funcRule = new FunctionalityRuleExecutor(orarOntology);
		this.tranRule = new TransitivityRuleExecutor(orarOntology);
		this.subroRule = new SubRoleRuleExecutor(orarOntology);
//		this.inverseRule = new InverseRoleRuleExecutor(orarOntology);

		this.ruleExecutors = new ArrayList<RuleExecutor>();
		this.ruleExecutors.add(sameasRule);
		this.ruleExecutors.add(subroRule);
		this.ruleExecutors.add(tranRule);
		this.ruleExecutors.add(funcRule);
//		this.ruleExecutors.add(inverseRule);
	}

	@Override
	public void materialize() {
		long starTime = System.currentTimeMillis();
		for (RuleExecutor ruleEx : this.ruleExecutors) {
			ruleEx.materialize();
			logger.info("Size of new role assertion from"+ ruleEx.getClass().getName()+": "+ruleEx.getNewRoleAssertions().size());
			logger.info("Size of new sameas assertion from"+ ruleEx.getClass().getName()+": "+ruleEx.getNewSameasAssertions().size());
			this.todoRoleAssertions.addAll(ruleEx.getNewRoleAssertions());
			this.todoSameasAssertions.addAll(ruleEx.getNewSameasAssertions());
		}
		long startTimeOfIncrementalReasoning=System.currentTimeMillis();
		incrementalMaterialize();
		long endTime = System.currentTimeMillis();
		this.reasoningTime = (endTime - starTime) / 1000;
		long incrementalReasoningTime = (endTime-startTimeOfIncrementalReasoning)/1000;
		logger.info("Reasoning time for incremental rule reasoning step: " + incrementalReasoningTime);
		logger.info("Reasoning time for deductive closure computing in this step: " + this.reasoningTime);
	}

	@Override
	public void incrementalMaterialize() {
		boolean hasNewElement=false;
		while (!this.todoRoleAssertions.isEmpty() || !this.todoSameasAssertions.isEmpty()) {
			while (!this.todoSameasAssertions.isEmpty()) {
				Set<Integer> setOfSameasIndividuals = this.todoSameasAssertions.poll();
				for (RuleExecutor ruleEx : this.ruleExecutors) {
					ruleEx.clearOldBuffer();
					ruleEx.incrementalMaterialize(setOfSameasIndividuals);
					this.todoRoleAssertions.addAll(ruleEx.getNewRoleAssertions());
					this.todoSameasAssertions.addAll(ruleEx.getNewSameasAssertions());
				}
			}

			while (!this.todoRoleAssertions.isEmpty()) {
				IndexedRoleAssertion aRoleAssertion = this.todoRoleAssertions.poll();
				for (RuleExecutor ruleEx : this.ruleExecutors) {
					ruleEx.clearOldBuffer();
					ruleEx.incrementalMaterialize(aRoleAssertion);
					this.todoRoleAssertions.addAll(ruleEx.getNewRoleAssertions());
					this.todoSameasAssertions.addAll(ruleEx.getNewSameasAssertions());
				}
			}
		}
	}

	@Override
	public OrarOntology2 getOntology() {

		return this.orarOntology;
	}

	@Override
	public void addTodoSameasAssertions(Set<Set<Integer>> todoSameasAssertions) {
		this.todoSameasAssertions.addAll(todoSameasAssertions);

	}

	@Override
	public void addTodoRoleAsesrtions(Set<IndexedRoleAssertion> odoRoleAssertions) {
		this.todoRoleAssertions.addAll(odoRoleAssertions);

	}

	@Override
	public long getReasoningTime() {

		return this.reasoningTime;
	}

}
