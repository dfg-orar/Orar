package orar.materializer.HornSHOIF_Increment;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHOIF.Konclude_HornSHOIF_InnerReasoner;
import orar.innerreasoner.HornSHOIF.Konclude_HornSHOIF_InnerReasoner2;
import orar.materializer.HornSHOIF.HornSHOIF_MaterializerOptimized;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

public class HornSHOIF_Materializer_KoncludeOptimized_Increment extends HornSHOIF_MaterializerOptimized_Increment {
	private int port = 8080;

	public HornSHOIF_Materializer_KoncludeOptimized_Increment(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	public HornSHOIF_Materializer_KoncludeOptimized_Increment(OrarOntology2 normalizedOrarOntology, int port) {
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
