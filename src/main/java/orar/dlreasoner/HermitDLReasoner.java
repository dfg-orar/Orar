package orar.dlreasoner;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class HermitDLReasoner extends DLReasonerTemplate {

	public HermitDLReasoner(OWLOntology owlOntology) {
		super(owlOntology);

	}

	@Override
	protected OWLReasoner getOWLReasoner(OWLOntology ontology) {
		OWLReasoner reasoner = new Reasoner(ontology);
		return reasoner;
	}

	@Override
	protected void dispose() {
		reasoner.dispose();

	}
}
