package orar.innerconsistencychecking;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.impl.blackbox.checker.InconsistentOntologyExplanationGeneratorFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public abstract class InnerConsistencyCheckerTemplate implements InnerConsistencyChecker {
	protected final OWLOntology owlOntology;
	private final Set<Set<OWLAxiom>> explanations;
	// private OWLReasonerFactory reasonerFactory;
	private final long timeoutInSeconds;
	private OWLDataFactory dataFactory;
	private long reasoningTimeInSeconds;

	public InnerConsistencyCheckerTemplate(OWLOntology owlOntology, long timeoutInSeconds) {
		this.owlOntology = owlOntology;
		this.explanations = new HashSet<Set<OWLAxiom>>();
		this.timeoutInSeconds = timeoutInSeconds;
		this.dataFactory = OWLManager.getOWLDataFactory();
		this.reasoningTimeInSeconds = -1;
	}

	protected abstract OWLReasoner getReasoner();

	protected abstract OWLReasonerFactory getOWLReasonerFactory();

	@Override
	public boolean isConsistent() {
		OWLReasoner owlReasoner = getReasoner();
		long startTime = System.currentTimeMillis();

		boolean consistent = owlReasoner.isConsistent();
		long endTime = System.currentTimeMillis();
		this.reasoningTimeInSeconds = (endTime - startTime) / 1000;
		return consistent;

	}

	@Override
	public Set<Set<OWLAxiom>> getExplanations(int maxNumberOfExplanations) {
		computeExplanation(maxNumberOfExplanations);
		return this.explanations;
	}

	private void computeExplanation(int maxNumberOfExplanations) {
		OWLReasonerFactory reasonerFactory = getOWLReasonerFactory();

		/*
		 * Create the explanation generator factory which uses reasoners
		 * provided by the specified reasoner factory. 1000 is the timeout for
		 * reasoning; not clear s or ms.
		 * 
		 */

		InconsistentOntologyExplanationGeneratorFactory explGeneratorFac = new InconsistentOntologyExplanationGeneratorFactory(
				reasonerFactory, this.timeoutInSeconds);

		/*
		 * Now create the actual explanation generator for our ontology
		 * 
		 */
		ExplanationGenerator<OWLAxiom> explGenerator = explGeneratorFac.createExplanationGenerator(this.owlOntology);

		/*
		 * Ask for explanations for some entailment. For inconsistency we ask
		 * for top implies bottom.
		 * 
		 */
		OWLAxiom entailment = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(), dataFactory.getOWLNothing());

		/*
		 * Get our explanations.
		 */

		Set<Explanation<OWLAxiom>> owlapiExplanations = explGenerator.getExplanations(entailment,
				maxNumberOfExplanations);
		for (Explanation<OWLAxiom> eachExplanation : owlapiExplanations) {
			HashSet<OWLAxiom> setOfAxioms = new HashSet<OWLAxiom>(eachExplanation.getAxioms());
			setOfAxioms.remove(entailment);
			this.explanations.add(setOfAxioms);
		}
	}

	public long getReasoningTimeInSeconds() {
		return this.reasoningTimeInSeconds;
	}
}
