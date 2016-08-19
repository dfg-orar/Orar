package orar.materializer.DLLite;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHIF.Hermit_HornSHIF_InnerReasoner;
import orar.materializer.DLLite_MaterializerTemplate;
import orar.modeling.ontology.OrarOntology;

public class DLLite_Materializer_Hermit extends DLLite_MaterializerTemplate {

	public DLLite_Materializer_Hermit(OrarOntology normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Hermit_HornSHIF_InnerReasoner(abstraction);
		return reasoner;
	}

}