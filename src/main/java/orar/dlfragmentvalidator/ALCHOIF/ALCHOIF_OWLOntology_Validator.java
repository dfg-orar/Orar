package orar.dlfragmentvalidator.ALCHOIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.DLFragment;
import orar.dlfragmentvalidator.OWLOntologyValidator;

public class ALCHOIF_OWLOntology_Validator extends OWLOntologyValidator {

	public ALCHOIF_OWLOntology_Validator(OWLOntology inputOWLOntology) {
		super(inputOWLOntology, DLFragment.ALCHOIF);

	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new ALCHOIF_AxiomValidator();

	}

}
