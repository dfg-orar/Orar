package orar.graphutil;

import java.util.Set;

public interface ConnectedComponent<T> {

	/**
	 * @param inputGraph
	 *            as a set of small connected components. E.g. <1,2>, <2,3,4>,
	 *            <5,6>, <5,7>
	 * 
	 * @return resulting connected componenets. E.g. <1,2,3,4>, <5,6,7>
	 */
	public Set<Set<T>> getConnectedComponenets(Set<Set<T>> inputGraph);
}
