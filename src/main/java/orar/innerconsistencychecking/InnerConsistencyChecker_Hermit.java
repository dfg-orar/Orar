package orar.innerconsistencychecking;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class InnerConsistencyChecker_Hermit extends InnerConsistencyCheckerTemplate {

	public InnerConsistencyChecker_Hermit(OWLOntology owlOntology, long timeoutInSeconds) {
		super(owlOntology, timeoutInSeconds);

	}

	@Override
	protected OWLReasoner getReasoner() {
		OWLReasoner reasoner = new Reasoner(this.owlOntology);
		return reasoner;

	}

	@Override
	protected OWLReasonerFactory getOWLReasonerFactory() {

		return new Reasoner.ReasonerFactory();
	}

}
