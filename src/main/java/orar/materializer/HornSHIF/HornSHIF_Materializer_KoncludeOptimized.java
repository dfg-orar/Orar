package orar.materializer.HornSHIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHIF.Konclude_HornSHIF_InnerReasonerOptimized;
import orar.modeling.ontology2.OrarOntology2;

public class HornSHIF_Materializer_KoncludeOptimized extends HornSHIF_MaterializerOptimized {
	private int port = 8080;

	public HornSHIF_Materializer_KoncludeOptimized(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	public HornSHIF_Materializer_KoncludeOptimized(OrarOntology2 normalizedOrarOntology, int port) {
		super(normalizedOrarOntology);
		this.port = port;
	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner;
		if (port == 8080) {
			reasoner = new Konclude_HornSHIF_InnerReasonerOptimized(abstraction);
		} else {
			reasoner = new Konclude_HornSHIF_InnerReasonerOptimized(abstraction, this.port);
		}
		return reasoner;
	}

}
