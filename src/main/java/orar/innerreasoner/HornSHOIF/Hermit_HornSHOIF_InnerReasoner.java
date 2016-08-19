package orar.innerreasoner.HornSHOIF;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class Hermit_HornSHOIF_InnerReasoner extends HornSHOIF_InnerReasonerTemplate {

	public Hermit_HornSHOIF_InnerReasoner(OWLOntology owlOntology) {
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
