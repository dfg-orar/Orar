package orar.debugger;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerconsistencychecking.InnerConsistencyChecker;
import orar.innerexplanation.InnerInconsistencyExplanation;
import orar.innerexplanation.InnerInconsistencyExplanationHermit;
import orar.innerreasoner.InnerReasoner;
import orar.modeling.ontology.OrarOntology;

public class OrarDebugger_Hermit extends OrarDebuggerDLLiteTemplate {

	public OrarDebugger_Hermit(OrarOntology normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerInconsistencyExplanation getInnerInconsistencyExplaner() {

		return new InnerInconsistencyExplanationHermit();
	}

}
