package orar.innerreasoner.HornSHIF;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Hermit_HornSHIF_InnerReasoner extends HornSHIF_InnerReasonerTemplate {

	public Hermit_HornSHIF_InnerReasoner(OWLOntology owlOntology) {
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

	@Override
	public long getOverheadTimeToSetupReasoner() {
		return 0;
	}
}
