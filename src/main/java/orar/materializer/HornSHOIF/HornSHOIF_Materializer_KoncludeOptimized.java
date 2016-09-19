package orar.materializer.HornSHOIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import x.innerreasoner.InnerReasoner;
import x.innerreasoner.HornSHOIF.Konclude_HornSHOIF_InnerReasoner;
import x.innerreasoner.HornSHOIF.Konclude_HornSHOIF_InnerReasoner2;

public class HornSHOIF_Materializer_KoncludeOptimized extends HornSHOIF_MaterializerOptimized {
	private int port = 8080;

	public HornSHOIF_Materializer_KoncludeOptimized(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	public HornSHOIF_Materializer_KoncludeOptimized(OrarOntology2 normalizedOrarOntology, int port) {
		super(normalizedOrarOntology);
		this.port = port;
	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner;
		if (this.port == 8080) {
			reasoner = new Konclude_HornSHOIF_InnerReasoner2(abstraction);
		} else {
			reasoner = new Konclude_HornSHOIF_InnerReasoner2(abstraction, port);
		}

		return reasoner;
	}

}
