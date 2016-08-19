package orar.debugger;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public interface OrarDebugger {
	public Set<Set<OWLAxiom>> getExplanationsForInconsistency(int maxNumberOfExplanations, long timeout);
	public long getReasoningTimeInSeconds();
}
