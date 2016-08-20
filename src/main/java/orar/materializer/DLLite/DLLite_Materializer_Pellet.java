package orar.materializer.DLLite;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHIF.Pellet_HornSHIF_InnerReasoner;
import orar.materializer.DLLite_MaterializerTemplate;
import orar.modeling.ontology2.OrarOntology2;

public class DLLite_Materializer_Pellet extends DLLite_MaterializerTemplate {

	public DLLite_Materializer_Pellet(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Pellet_HornSHIF_InnerReasoner(abstraction);
		return reasoner;
	}

}