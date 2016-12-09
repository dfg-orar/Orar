package orar.type;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class BasicIndividualType implements IndividualType {

	private final Set<OWLClass> concepts_;
	private final Set<OWLObjectProperty> preRoles_;
	private final Set<OWLObjectProperty> sucRoles_;

	public BasicIndividualType(Set<OWLClass> atomicConcepts, Set<OWLObjectProperty> preRoles,
			Set<OWLObjectProperty> sucRoles) {

		this.concepts_ = atomicConcepts;
		this.preRoles_ = preRoles;
		this.sucRoles_ = sucRoles;

	}

	@Override
	public Set<OWLClass> getConcepts() {
		return this.concepts_;
	}

	@Override
	public Set<OWLObjectProperty> getPredecessorRoles() {
		return this.preRoles_;
	}

	@Override
	public Set<OWLObjectProperty> getSuccessorRoles() {
		return this.sucRoles_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concepts_ == null) ? 0 : concepts_.hashCode());
		result = prime * result + ((preRoles_ == null) ? 0 : preRoles_.hashCode());
		result = prime * result + ((sucRoles_ == null) ? 0 : sucRoles_.hashCode());
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
		BasicIndividualType other = (BasicIndividualType) obj;
		if (concepts_ == null) {
			if (other.concepts_ != null)
				return false;
		} else if (!concepts_.equals(other.concepts_))
			return false;
		if (preRoles_ == null) {
			if (other.preRoles_ != null)
				return false;
		} else if (!preRoles_.equals(other.preRoles_))
			return false;
		if (sucRoles_ == null) {
			if (other.sucRoles_ != null)
				return false;
		} else if (!sucRoles_.equals(other.sucRoles_))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HornSHOIF_IndividualType [concepts=" + concepts_ + ", preRoles=" + preRoles_ + ", sucRoles=" + sucRoles_
				+ "]";
	}

}
