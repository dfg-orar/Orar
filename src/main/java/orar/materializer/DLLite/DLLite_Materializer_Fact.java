package orar.materializer.DLLite;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.innerreasoner.InnerReasoner;
import orar.innerreasoner.HornSHIF.Fact_HornSHIF_InnerReasoner;
import orar.materializer.DLLite_ConceptMaterializerTemplate;
import orar.modeling.ontology2.OrarOntology2;

public class DLLite_Materializer_Fact extends DLLite_ConceptMaterializerTemplate {

	public DLLite_Materializer_Fact(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected InnerReasoner getInnerReasoner(OWLOntology abstraction) {
		InnerReasoner reasoner = new Fact_HornSHIF_InnerReasoner(abstraction);
		return reasoner;
	}

}