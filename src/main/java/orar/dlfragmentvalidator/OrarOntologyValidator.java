package orar.dlfragmentvalidator;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import orar.modeling.ontology.OrarOntology;

public abstract class OrarOntologyValidator {
	protected final OrarOntology inputOrarOntology;
	protected boolean validatingDone;
	protected AxiomValidator axiomValidator;
	protected final Set<OWLAxiom> validatedAxioms;
	protected String dlfrangment;

	public OrarOntologyValidator(OrarOntology inputOrarOntology) {
		this.inputOrarOntology = inputOrarOntology;
		this.validatedAxioms = new HashSet<OWLAxiom>();
		this.validatingDone = false;
		this.dlfrangment = "dlfragment";// default value
		initAxiomValidator();
	}

	/**
	 * initialize  {@link this#validatedAxioms, this#dlfrangment} 
	 */
	public abstract void initAxiomValidator();

	public Set<DLConstructor> getDLConstructors() {
		if (!validatingDone) {
			validateOWLOntology();
		}
		return axiomValidator.getDLConstructorsInInputOntology();
	}

	public Set<OWLAxiom> getViolatedAxioms() {
		if (!validatingDone) {
			validateOWLOntology();
		}
		return this.axiomValidator.getViolatedAxioms();
	}

	public boolean isNotInTheDLFragment() {
		return this.axiomValidator.isNotViolatedProfile();
	}

	/**
	 * @return the input ontology, where its TBox is in the desired DL fragment.
	 * 
	 */
	public OrarOntology getOWLOntologyInTheDLFragment() {
		if (!validatingDone) {
			validateOWLOntology();
		}
		this.inputOrarOntology.addTBoxAxioms(validatedAxioms);
		return this.inputOrarOntology;
	}

	public void validateOWLOntology() {
		for (OWLAxiom axiom : this.inputOrarOntology.getTBoxAxioms()) {
			OWLAxiom validatedAxiom = axiom.accept(axiomValidator);
			if (validatedAxiom != null) {
				this.validatedAxioms.add(validatedAxiom);
			}
		}
		this.validatedAxioms.addAll(axiomValidator.getGeneratedAxioms());
		this.validatingDone = true;

	}
}
