package orar.type;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public interface IndividualType {

	/**
	 * @return a set of concepts occurring in the type. Note that changing in
	 *         this set will reflect in changing in the type.
	 */
	public Set<OWLClass> getConcepts();

	/**
	 * @return a set of successor roles occurring in the type. Note that
	 *         changing in this set will reflect in changing in the type.
	 */
	public Set<OWLObjectProperty> getSuccessorRoles();

	/**
	 * @return a set of predecessor roles occurring in the type.Note that
	 *         changing in this set will reflect in changing in the type.
	 */
	public Set<OWLObjectProperty> getPredecessorRoles();

	// public ExtendedPartOfType getExtendedPart();

}
