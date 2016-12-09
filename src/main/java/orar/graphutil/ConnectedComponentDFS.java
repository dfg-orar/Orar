package orar.graphutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import orar.util.PrintingHelper;

public class ConnectedComponentDFS<T> implements ConnectedComponent<T> {

	private SimpleUndirectedGraph<T> graph;

	@Override
	public Set<Set<T>> getConnectedComponenets(Set<Set<T>> inputSets) {
		graph = new SimpleUndirectedGraph<>();
		initGraph(inputSets);

		Set<T> allIndividualsInSameasMap = this.graph.getAllNodes();
		Queue<T> todoIndividuals = new LinkedList<T>(allIndividualsInSameasMap);
		Set<Set<T>> components = new HashSet<Set<T>>();

		while (!todoIndividuals.isEmpty()) {
			T a = todoIndividuals.poll();
			// compute component for each individual a in todoIndividuals.
			Set<T> newComponent = new HashSet<T>();
			newComponent.add(a);
			Stack<T> stackForDFS = new Stack<T>();
			stackForDFS.push(a);
			while (!stackForDFS.isEmpty()) {
				T ind = stackForDFS.pop();
				List<T> sameasOf_a = this.graph.getConnectedNodesOf(ind);
				sameasOf_a.removeAll(newComponent);
				newComponent.addAll(sameasOf_a);
				stackForDFS.addAll(sameasOf_a);
				todoIndividuals.removeAll(sameasOf_a);
			}
			components.add(newComponent);
		}
		return components;

	}

	private void initGraph(Set<Set<T>> inputSets) {
		for (Set<T> eachSet : inputSets) {
			this.graph.addEdge(eachSet);
		}
	}

	public static void main(String[] args) {

		ConnectedComponent<Integer> cc = new ConnectedComponentDFS<>();
		Set<Integer> set12 = new HashSet<>();
		set12.add(1);
		set12.add(2);

		Set<Integer> set23 = new HashSet<>();
		set12.add(4);
		set12.add(3);

		Set<Integer> set45 = new HashSet<>();
		set45.add(3);
		set45.add(5);

		Set<Set<Integer>> allSet = new HashSet<>();
		allSet.add(set23);
		allSet.add(set12);
		allSet.add(set45);
		PrintingHelper.printSet(cc.getConnectedComponenets(allSet));
	}
}
