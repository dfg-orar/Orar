package orar.innerexplanation;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.impl.blackbox.checker.InconsistentOntologyExplanationGeneratorFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public abstract class InnerInconsistencyExplanationTemplate implements InnerInconsistencyExplanation {

	// private OWLDataFactory dataFactory;
	// public InnerInconsistencyExplanationTemplate(){
	// this.dataFactory= OWLManager.getOWLDataFactory();
	// }
	protected abstract OWLReasonerFactory getOWLReasonerFactory();

	@Override
	public Set<Set<OWLAxiom>> getExplanations(OWLOntology ontology, int maxNumberOfExplanations, long timeout) {
		Set<Set<OWLAxiom>> explanationSet = new HashSet<>();
		OWLReasonerFactory reasonerFactory = getOWLReasonerFactory();
		
		/*
		 * Create the explanation generator factory which uses reasoners
		 * provided by the specified reasoner factory. 1000 is the timeout for
		 * reasoning; not clear s or ms.
		 * 
		 */

		InconsistentOntologyExplanationGeneratorFactory explGeneratorFac = new InconsistentOntologyExplanationGeneratorFactory(
				reasonerFactory, timeout);
		
		/*
		 * Now create the actual explanation generator for our ontology
		 * 
		 */
		ExplanationGenerator<OWLAxiom> explGenerator = explGeneratorFac.createExplanationGenerator(ontology);

		/*
		 * Ask for explanations for some entailment. For inconsistency we ask
		 * for top implies bottom.
		 * 
		 */
		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		OWLAxiom entailment = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(), dataFactory.getOWLNothing());

		/*
		 * Get all our explanations. 
		 */

//		Set<Explanation<OWLAxiom>> expl = explGenerator.getExplanations(entailment);
		Set<Explanation<OWLAxiom>> expl = explGenerator.getExplanations(entailment, maxNumberOfExplanations);
		for (Explanation<OWLAxiom> ex : expl) {
			explanationSet.add(ex.getAxioms());

		}
		
		return explanationSet;
	}

}
