package orar.dlfragmentvalidator;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DLExpressivityChecker;

public abstract class OWLOntologyValidator {

	protected final OWLOntology inputOWLOntology;
	protected boolean validatingDone;
	protected AxiomValidator axiomValidator;
	protected final Set<OWLAxiom> validatedAxioms;
	protected final DLFragment targetedDLFrangment;
	protected String actualFragment;// DL fragment of the input ontology
	protected boolean isInputOntologyInHorn = true;// default
	protected final Set<DLConstructor> dlConstructorsInInputOntology;
	protected final Set<DLConstructor> dlConstructorsInValidatedOntology;

	public OWLOntologyValidator(OWLOntology inputOWLOntology, DLFragment targetedDL) {
		this.inputOWLOntology = inputOWLOntology;
		this.validatedAxioms = new HashSet<OWLAxiom>();
		this.validatingDone = false;
		this.targetedDLFrangment = targetedDL;
		this.dlConstructorsInInputOntology = new HashSet<DLConstructor>();
		this.dlConstructorsInValidatedOntology = new HashSet<DLConstructor>();
		initAxiomValidator();
	}

	public abstract void initAxiomValidator();

	public Set<DLConstructor> getDLConstructorsInInputOntology() {
		if (!validatingDone) {
			validateOWLOntology();
		}
		return axiomValidator.getDLConstructorsInInputOntology();
	}
	
	public Set<DLConstructor> getDLConstructorsInValidatedOntology() {
		if (!validatingDone) {
			validateOWLOntology();
		}
		return axiomValidator.getDLConstructorsInValidatedOntology();
	}

	public Set<OWLAxiom> getViolatedAxioms() {
		if (!validatingDone) {
			validateOWLOntology();
		}
		return this.axiomValidator.getViolatedAxioms();
	}

	public boolean isInputOntologyAlreadyInTheTargetedDLFragment() {
		return this.axiomValidator.isNotViolatedProfile();
	}

	public OWLOntology getOWLOntologyInTheTargetedDLFragment() {
		if (!validatingDone) {
			validateOWLOntology();
		}

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntologyID oldID = this.inputOWLOntology.getOntologyID();
		IRI iri = oldID.getOntologyIRI();

		IRI newIRI;
		if (iri != null) {
			newIRI = IRI.create(iri.toString() + "_in_" + this.targetedDLFrangment);

		} else {
			newIRI = IRI.create("http://www.uniulm.ki/ontology/in_" + this.targetedDLFrangment);
		}

		try {
			OWLOntology ontologyInDLFragment = manager.createOntology(newIRI);
			manager.addAxioms(ontologyInDLFragment, this.validatedAxioms);
			return ontologyInDLFragment;

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void validateOWLOntology() {
		HashSet<OWLAxiom> allAxioms = new HashSet<OWLAxiom>();
		allAxioms.addAll(this.inputOWLOntology.getTBoxAxioms(true));
		allAxioms.addAll(this.inputOWLOntology.getRBoxAxioms(true));
		allAxioms.addAll(this.inputOWLOntology.getABoxAxioms(true));
		allAxioms.addAll(this.inputOWLOntology.getAxioms(AxiomType.DECLARATION));
		/*
		 * BE CAREFUL: method OWLOntology.getAxioms() does not return all
		 * concept assertions. This is a bug of OWLAPI
		 */
		// for (OWLAxiom axiom : this.inputOWLOntology.getAxioms()) {
		for (OWLAxiom axiom : allAxioms) {
			OWLAxiom validatedAxiom = axiom.accept(axiomValidator);
			if (validatedAxiom != null) {
				this.validatedAxioms.add(validatedAxiom);
			}
		}
		this.validatedAxioms.addAll(axiomValidator.getGeneratedAxioms());
		this.validatingDone = true;
		/*
		 * check Horn or Non-Horn
		 */
		checkTheOntologyInHornOrNonHorn(inputOWLOntology, this.axiomValidator.getDLConstructorsInInputOntology());
		/*
		 * get actual DL-Constructors in the input ontology
		 */
		this.dlConstructorsInInputOntology.addAll(this.axiomValidator.getDLConstructorsInInputOntology());
		this.dlConstructorsInValidatedOntology.addAll(this.axiomValidator.getDLConstructorsInValidatedOntology());
	}

	private void checkTheOntologyInHornOrNonHorn(OWLOntology inputOntology, Set<DLConstructor> constructors) {

		/*
		 * get DLExpressivity
		 */
		Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
		ontologies.add(inputOntology);
		// TODO: could be improved since DLExpressivityChecker by OWLAPI is not
		// so reliable.
		DLExpressivityChecker checker = new DLExpressivityChecker(ontologies);
		String dlname = checker.getDescriptionLogicName();

		HashSet<Object> nonhornConstructors = new HashSet<Object>();
		nonhornConstructors.add(DLConstructor.NonHorn_DISJUNCTION);
		nonhornConstructors.add(DLConstructor.NonHorn_NEGATION);
		nonhornConstructors.add(DLConstructor.NonHorn_UNIVERSAL_RESTRICTION);
		HashSet<DLConstructor> copyOfConstructors = new HashSet<DLConstructor>(constructors);
		copyOfConstructors.retainAll(nonhornConstructors);
		if (!copyOfConstructors.isEmpty()) {
			this.isInputOntologyInHorn = false;
			this.actualFragment = "NonHorn_" + dlname;
		} else {
			this.actualFragment = "Horn_" + dlname;
		}

	}

	public boolean isInputOntologyInHorn() {
		return isInputOntologyInHorn;
	}

	public void setInputOntologyInHorn(boolean isInputOntologyInHorn) {
		this.isInputOntologyInHorn = isInputOntologyInHorn;
	}

	public Set<OWLAxiom> getValidatedAxioms() {
		return validatedAxioms;
	}

	public DLFragment getTargetedDLFrangment() {
		return targetedDLFrangment;
	}

	public String getActualFragment() {
		return actualFragment;
	}

	public int getNumberOfMaxCardinalityAxioms() {
		return axiomValidator.getNumberOfCardinalityAxioms();
	}
}
