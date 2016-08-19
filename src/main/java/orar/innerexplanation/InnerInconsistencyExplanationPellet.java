package orar.innerexplanation;

import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

/**
 * InconsistencyExplanation interface for the inner reasoners, e.g. HermiT,...
 * 
 * @author kien
 *
 */
public class InnerInconsistencyExplanationPellet extends InnerInconsistencyExplanationTemplate {

	@Override
	protected OWLReasonerFactory getOWLReasonerFactory() {

		return PelletReasonerFactory.getInstance();
		
	}

}