package orar.materializer.HornSHIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHIF.Hermit_HornSHIF_InnerReasoner;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

public class HornSHIF_Materializer_Hermit extends HornSHIF_Materializer {

	public HornSHIF_Materializer_Hermit(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Hermit_HornSHIF_InnerReasoner(abstraction);
		return reasoner;
	}

}
