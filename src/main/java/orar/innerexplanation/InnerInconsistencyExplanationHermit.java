package orar.innerexplanation;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 * InconsistencyExplanation interface for the inner reasoners, e.g. HermiT,...
 * 
 * @author kien
 *
 */
public class InnerInconsistencyExplanationHermit extends InnerInconsistencyExplanationTemplate {

	@Override
	protected OWLReasonerFactory getOWLReasonerFactory() {

		return new Reasoner.ReasonerFactory();

	}

}