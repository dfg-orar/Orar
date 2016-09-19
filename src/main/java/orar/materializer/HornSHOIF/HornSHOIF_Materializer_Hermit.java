package orar.materializer.HornSHOIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import x.innerreasoner.InnerReasoner;
import x.innerreasoner.HornSHOIF.Hermit_HornSHOIF_InnerReasoner;

public class HornSHOIF_Materializer_Hermit extends HornSHOIF_MaterializerOptimized {

	public HornSHOIF_Materializer_Hermit(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Hermit_HornSHOIF_InnerReasoner(abstraction);
		return reasoner;
	}

}
