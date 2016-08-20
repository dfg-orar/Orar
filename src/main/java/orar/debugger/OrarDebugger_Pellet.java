package orar.debugger;

import orar.innerexplanation.InnerInconsistencyExplanation;
import orar.innerexplanation.InnerInconsistencyExplanationPellet;
import orar.modeling.ontology2.OrarOntology2;

public class OrarDebugger_Pellet extends OrarDebuggerDLLiteTemplate {

	public OrarDebugger_Pellet(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerInconsistencyExplanation getInnerInconsistencyExplaner() {

		return new InnerInconsistencyExplanationPellet();
	}

}
