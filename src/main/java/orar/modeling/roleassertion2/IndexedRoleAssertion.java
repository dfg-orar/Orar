package orar.modeling.roleassertion2;

import org.semanticweb.owlapi.model.OWLObjectProperty;

public class IndexedRoleAssertion {
	private final Integer subject;
	private final OWLObjectProperty role;
	private final Integer object;

	public IndexedRoleAssertion(Integer subject, OWLObjectProperty role, Integer object) {
		this.subject = subject;
		this.role = role;
		this.object = object;
	}

	public Integer getSubject() {
		return subject;
	}

	public OWLObjectProperty getRole() {
		return role;
	}

	public Integer getObject() {
		return object;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		IndexedRoleAssertion other = (IndexedRoleAssertion) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

}
