package orar.innerreasoner.HornSHOIF;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class Fact_HornSHOIF_InnerReasoner extends HornSHOIF_InnerReasonerTemplate {

	public Fact_HornSHOIF_InnerReasoner(OWLOntology owlOntology) {
		super(owlOntology);

	}

	@Override
	protected OWLReasoner getOWLReasoner(OWLOntology ontology) {
		OWLReasonerFactory reasonerFactory = new JFactFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

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
