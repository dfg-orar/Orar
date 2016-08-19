package orar.dlfragmentvalidator;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Check and get TBox axioms (for both concept and role) in a specific DL
 * fragment.
 * 
 * @author kien
 *
 */
public abstract class TBoxValidator {
	protected Set<OWLAxiom> inputTBoxAxioms;
	protected boolean validatingDone;
	protected AxiomValidator axiomValidator;
	protected final Set<OWLAxiom> validatedAxioms;
	protected String dlfrangment;

	public TBoxValidator(Set<OWLAxiom> inputTBoxAxioms) {
		this.inputTBoxAxioms = inputTBoxAxioms;
		this.validatedAxioms = new HashSet<OWLAxiom>();
		this.validatingDone = false;
		this.dlfrangment = "dlfragment";// default value
		initAxiomValidator();
	}

	/**
	 * initialize {@link this#validatedAxioms, this#dlfrangment}
	 */
	public abstract void initAxiomValidator();

	public Set<DLConstructor> getDLConstructors() {
		if (!validatingDone) {
			validate();
		}
		return axiomValidator.getDLConstructorsInInputOntology();
	}

	public Set<OWLAxiom> getViolatedAxioms() {
		if (!validatingDone) {
			validate();
		}
		return this.axiomValidator.getViolatedAxioms();
	}

	public boolean isNotInTheDLFragment() {
		return this.axiomValidator.isNotViolatedProfile();
	}

	public Set<OWLAxiom> getValidatedAxioms() {
		return this.validatedAxioms;
	}

	public void validate() {
		for (OWLAxiom axiom : this.inputTBoxAxioms) {
			OWLAxiom validatedAxiom = axiom.accept(axiomValidator);
			if (validatedAxiom != null) {
				this.validatedAxioms.add(validatedAxiom);
			}
		}
		this.validatedAxioms.addAll(axiomValidator.getGeneratedAxioms());
		this.validatingDone = true;

	}
}
