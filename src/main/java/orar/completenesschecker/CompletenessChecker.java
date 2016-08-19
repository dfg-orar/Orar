package orar.completenesschecker;

/**
 * @author kien
 *
 */
public interface CompletenessChecker {
	/**
	 * compute entailments.
	 */
	public void computeEntailments();

	/**
	 * @return true if concept assertions derived by abstraction materializer
	 *         are identical to the ones by OWLReasoner, false otherwise.
	 */
	public boolean isConceptAssertionComplete();

	/**
	 * @return true if role assertions derived by abstraction materializer are
	 *         identical to the ones by OWLReasoner, false otherwise.
	 */
	public boolean isRoleAssertionComplete();

	/**
	 * @return true if sameas assertions derived by abstraction materializer are
	 *         identical to the ones by OWLReasoner, false otherwise.
	 */
	public boolean isSameasComplete();

}
