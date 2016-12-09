package orar.data;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.abstraction.PairOfSubjectAndObject;
import orar.type.IndividualType;

public interface DataForTransferringEntailmentInterface {

	Map<OWLNamedIndividual, IndividualType> getMap_XAbstractIndiv_2_Type();

	Map<OWLNamedIndividual, Set<Integer>> getMap_XAbstractIndiv_2_OriginalIndivs();

	Map<OWLNamedIndividual, Set<Integer>> getMap_YAbstractIndiv_2_OriginalIndivs();

	Map<OWLNamedIndividual, Set<Integer>> getMap_ZAbstractIndiv_2_OriginalIndivs();

	Map<OWLNamedIndividual, Set<Integer>> getMap_UAbstractIndiv_2_OriginalIndivs();

	Map<PairOfSubjectAndObject, OWLObjectProperty> getMap_XY_2_Role();

	Map<PairOfSubjectAndObject, OWLObjectProperty> getMap_ZX_2_Role();

	Map<IndividualType, Set<Integer>> getMapType_2_Individuals();

	/**
	 * @return a set of abstract indiv x whose type contains functional roles.
	 */
	Set<OWLNamedIndividual> getxAbstractHavingFunctionalRole();

	/**
	 * @return a set of abstract indiv z whose type contains inverse functional
	 *         roles.
	 */
	Set<OWLNamedIndividual> getzAbstractHavingInverseFunctionalRole();

	Map<Integer, OWLNamedIndividual> getMapIndividual2XAbstract();

	/**
	 * @param abstractInd
	 * @return a set of original individuals for which the abstractInd
	 *         represents.<b> Note </b> that changing in this set will affect
	 *         the mapping.
	 */
	Set<Integer> getOriginalIndividuals(OWLNamedIndividual abstractInd);

	/**
	 * Clear all maps.
	 */
	void clear();

	void clearMapOfTypes();

}