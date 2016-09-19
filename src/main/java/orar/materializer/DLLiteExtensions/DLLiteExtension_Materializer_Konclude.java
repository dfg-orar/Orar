package orar.materializer.DLLiteExtensions;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.materializer.HornSHOIF.HornSHOIF_MaterializerOptimized;
import orar.modeling.ontology2.OrarOntology2;
import x.innerreasoner.InnerReasoner;
import x.innerreasoner.HornSHOIF.Konclude_HornSHOIF_InnerReasoner;

/**
 * We extend strategy of HornSHOIF. The differences for the case of
 * DLLiteExtention is not to take care of y and z.
 * 
 * @author kien
 *
 */
public class DLLiteExtension_Materializer_Konclude extends DLLiteExtension_Materializer {
	private int port = 8080;

	public DLLiteExtension_Materializer_Konclude(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	public DLLiteExtension_Materializer_Konclude(OrarOntology2 normalizedOrarOntology, int port) {
		super(normalizedOrarOntology);
		this.port = port;
	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner;
		if (this.port == 8080) {
			reasoner = new Konclude_HornSHOIF_InnerReasoner(abstraction);
		} else {
			reasoner = new Konclude_HornSHOIF_InnerReasoner(abstraction, port);
		}

		return reasoner;
	}

}
