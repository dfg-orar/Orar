package orar.materializer.DLLite;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHIF.Konclude_HornSHIF_InnerReasoner;
import orar.materializer.DLLite_MaterializerTemplate;
import orar.modeling.ontology.OrarOntology;

public class DLLite_Materializer_Konclude extends DLLite_MaterializerTemplate {

	private int port = 8080;

	public DLLite_Materializer_Konclude(OrarOntology normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	public DLLite_Materializer_Konclude(OrarOntology normalizedOrarOntology, int port) {
		super(normalizedOrarOntology);
		this.port = port;
	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner;
		if (this.port == 8080) {
			reasoner = new Konclude_HornSHIF_InnerReasoner(abstraction);
		} else {
			reasoner = new Konclude_HornSHIF_InnerReasoner(abstraction, port);
		}

		return reasoner;
	}
}