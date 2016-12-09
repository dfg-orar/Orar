package orar.innerreasoner.HornSHIF;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class Pellet_HornSHIF_InnerReasoner extends HornSHIF_InnerReasonerTemplate {

	public Pellet_HornSHIF_InnerReasoner(OWLOntology owlOntology) {
		super(owlOntology);

	}

	@Override
	protected OWLReasoner getOWLReasoner(OWLOntology ontology) {
		OWLReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
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
