package orar.graphutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

public class SimpleUndirectedGraph<T> {
	private Logger logger = Logger.getLogger(SimpleUndirectedGraph.class);
	private Map<T, List<T>> gMap;

	public SimpleUndirectedGraph() {
		this.gMap = new HashMap<>();
	}

	public List<T> getConnectedNodesOf(T u) {
		return this.gMap.get(u);
	}

	public void addEdge(T u, T v) {
		/*
		 * add connected node of u
		 */
		List<T> connectedNodeOf_u = gMap.get(u);
		if (connectedNodeOf_u == null) {
			connectedNodeOf_u = new ArrayList<>();
			gMap.put(u, connectedNodeOf_u);
		}
		connectedNodeOf_u.add(v);

		/*
		 * add connected node of v
		 */
		List<T> connectedNodeOf_v = gMap.get(v);
		if (connectedNodeOf_v == null) {
			connectedNodeOf_v = new ArrayList<>();
			gMap.put(v, connectedNodeOf_v);
		}
		connectedNodeOf_v.add(u);
	}

	public void addEdge(Set<T> setOfNodes) {
		for (T eachNode : setOfNodes) {
			/*
			 * add connected node of u
			 */
			List<T> connectedNodeOf_eachNode = gMap.get(eachNode);
			if (connectedNodeOf_eachNode == null) {
				connectedNodeOf_eachNode = new ArrayList<>();
				gMap.put(eachNode, connectedNodeOf_eachNode);
			}
			connectedNodeOf_eachNode.addAll(setOfNodes);
		}
	}

	public void printGraph() {
		Iterator<Entry<T, List<T>>> iterator = this.gMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<T, List<T>> entry = iterator.next();
			logger.info(entry.getKey() + " --> " + entry.getValue());

		}
	}

	/**
	 * @return (copy) set of all nodes of the graphs.
	 */
	public Set<T> getAllNodes() {
		return new HashSet<>(this.gMap.keySet());
	}

	public static void main(String[] args) {
		SimpleUndirectedGraph<Integer> graph = new SimpleUndirectedGraph<>();
		graph.addEdge(1, 2);
		Set<Integer> set234 = new HashSet<>();
		set234.add(2);
		set234.add(3);
		set234.add(4);
		graph.addEdge(set234);

		graph.printGraph();
		System.out.println(graph.getAllNodes());
	}

}
