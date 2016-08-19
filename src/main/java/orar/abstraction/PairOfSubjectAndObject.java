package orar.abstraction;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * A pair of subject and object. This will be used in a map from this pair --> a
 * role, which is then used in transferring role assertions from the abstraction
 * to the original ABox.
 * 
 * @author kien
 *
 */
public class PairOfSubjectAndObject {
	private final OWLNamedIndividual subject;
	private final OWLNamedIndividual object;

	public PairOfSubjectAndObject(OWLNamedIndividual subject, OWLNamedIndividual object) {
		this.subject = subject;
		this.object = object;
	}

	public OWLNamedIndividual getSubject() {
		return subject;
	}

	public OWLNamedIndividual getObject() {
		return object;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PairOfSubjectAndObject other = (PairOfSubjectAndObject) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

}
