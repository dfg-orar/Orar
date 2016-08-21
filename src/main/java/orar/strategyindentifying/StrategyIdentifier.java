package orar.strategyindentifying;

import java.util.Set;

import orar.dlfragmentvalidator.DLConstructor;

public interface StrategyIdentifier {
	/**
	 * @return a set of DL constructors in the TBox
	 */
	public Set<DLConstructor> getDLConstructors();

	/**
	 * @return a strategy name for the abstraction refinement algorithm
	 */
	public StrategyName getStrategyName();

}
