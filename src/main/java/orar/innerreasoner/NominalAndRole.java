package orar.innerreasoner;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Used to keep track of marking axioms of the form: <br>
 * {o} SubClassOf forall.R.C <br>
 * where C is a fresh concept name. <br>
 * Then every instance a of C, we obtain R(o,a)
 * 
 * @author kien
 *
 */
public class NominalAndRole {
	private final OWLNamedIndividual nominal;
	private final OWLObjectProperty role;

	public NominalAndRole(OWLNamedIndividual nominal, OWLObjectProperty role) {
		this.nominal = nominal;
		this.role = role;
	}

	public OWLNamedIndividual getNominal() {
		return nominal;
	}

	public OWLObjectProperty getRole() {
		return role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nominal == null) ? 0 : nominal.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		NominalAndRole other = (NominalAndRole) obj;
		if (nominal == null) {
			if (other.nominal != null)
				return false;
		} else if (!nominal.equals(other.nominal))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NominalAndRole [nominal=" + nominal + ", role=" + role + "]";
	}

}
