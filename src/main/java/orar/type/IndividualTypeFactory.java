package orar.type;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public interface IndividualTypeFactory {
	public IndividualType getIndividualType(Set<OWLClass> atomicConcepts, Set<OWLObjectProperty> preRoles,
			Set<OWLObjectProperty> sucRoles);

	public void remove(IndividualType indivType);

	
	public void clear();
}
