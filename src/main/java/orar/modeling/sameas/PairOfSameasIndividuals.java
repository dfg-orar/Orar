package orar.modeling.sameas;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class PairOfSameasIndividuals {

	private final OWLNamedIndividual individual1;
	private final OWLNamedIndividual individual2;

	public PairOfSameasIndividuals(OWLNamedIndividual ind1, OWLNamedIndividual ind2) {
		this.individual1 = ind1;
		this.individual2 = ind2;
	}

	public OWLNamedIndividual getIndividual1() {
		return individual1;
	}

	public OWLNamedIndividual getIndividual2() {
		return individual2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((individual1 == null) ? 0 : individual1.hashCode());
		result = prime * result + ((individual2 == null) ? 0 : individual2.hashCode());
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
		PairOfSameasIndividuals other = (PairOfSameasIndividuals) obj;
		if (individual1 == null) {
			if (other.individual1 != null)
				return false;
		} else if (!individual1.equals(other.individual1))
			return false;
		if (individual2 == null) {
			if (other.individual2 != null)
				return false;
		} else if (!individual2.equals(other.individual2))
			return false;
		return true;
	}
	
}
