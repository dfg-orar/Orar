package orar.debugger;

import orar.innerexplanation.InnerInconsistencyExplanation;
import orar.innerexplanation.InnerInconsistencyExplanationHermit;
import orar.modeling.ontology2.OrarOntology2;

public class OrarDebugger_Hermit extends OrarDebuggerDLLiteTemplate {

	public OrarDebugger_Hermit(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerInconsistencyExplanation getInnerInconsistencyExplaner() {

		return new InnerInconsistencyExplanationHermit();
	}

}
