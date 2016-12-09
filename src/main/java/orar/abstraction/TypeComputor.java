package orar.abstraction;

import java.util.Map;
import java.util.Set;

import orar.modeling.ontology.OrarOntology;
import orar.type.IndividualType;

/**
 * @author kien Interface for classes that compute the type of all individuals.
 */
public interface TypeComputor {

	/**
	 * -compute the type of every individual in the ontology.<br>
	 * -equal individuals will be taken into account. Note that equality
	 * assertions will be read as they are, no equality closure computing is
	 * performed. Therefore, this method will return semantically correct result
	 * if the equality closure is computed in the input ontology.
	 * 
	 * @param orarOntology
	 *            internal data structure for an ontology.
	 * @return A map: type ---> set of individuals of this type
	 */
	public Map<IndividualType, Set<Integer>> computeTypes();

	/**
	 * Compute type of an individual; used in incremental algorithm
	 * 
	 * @param individual
	 * @return type of this particular individual
	 */
	public IndividualType computeType(Integer individual);

	public void computeTypeIncrementally(Set<Integer> individualsHavingNewAssertions);
	public void computeTypeIncrementally(Integer individualHavingNewAssertions);

}
