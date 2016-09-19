package orar.materializer.DLLiteExtensions;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.modeling.ontology2.OrarOntology2;
import x.innerreasoner.InnerReasoner;
import x.innerreasoner.HornSHOIF.Hermit_HornSHOIF_InnerReasoner;

public class DLLiteExtension_Materializer_Hermit extends DLLiteExtension_Materializer {

	public DLLiteExtension_Materializer_Hermit(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Hermit_HornSHOIF_InnerReasoner(abstraction);
		return reasoner;
	}

}
