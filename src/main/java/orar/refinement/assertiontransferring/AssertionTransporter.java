package orar.refinement.assertiontransferring;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import orar.modeling.roleassertion2.IndexedRoleAssertionList;
import orar.refinement.abstractroleassertion.RoleAssertionList;

/**
 * Adding assertions to the original ABox based on the entailments of the
 * abstraction.
 * 
 * @author kien
 *
 */
public interface AssertionTransporter {

	/**
	 * Adding assertions to the original ABox based on the entailments of the
	 * abstraction.
	 */
	public void updateOriginalABox();

	/**
	 * @return true if the original ABox has been extended; false otherwise
	 */
	public boolean isABoxExtended();

	/**
	 * @return a list of new role assertions haven added to the original ABox.
	 *         This will be used as an input for the rule engine.
	 */
	public IndexedRoleAssertionList getNewlyAddedRoleAssertions();
	

	/**
	 * @return new sameas assertions haven added to the original ABox.
	 *         This will be used as an input for the rule engine.
	 */
	public Set<Set<Integer>> getNewlyAddedSameasAssertions();
}
