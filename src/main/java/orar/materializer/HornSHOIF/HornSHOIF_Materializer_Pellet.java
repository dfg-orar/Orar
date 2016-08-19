package orar.materializer.HornSHOIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHOIF.Pellet_HornSHOIF_InnerReasoner;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

public class HornSHOIF_Materializer_Pellet extends HornSHOIF_Materializer {

	public HornSHOIF_Materializer_Pellet(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Pellet_HornSHOIF_InnerReasoner(abstraction);
		return reasoner;
	}

}
