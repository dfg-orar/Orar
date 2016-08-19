package orar.dlfragmentvalidator.HornSHOIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.DLFragment;
import orar.dlfragmentvalidator.OWLOntologyValidator;

public class HornSHOIF_OWLOntology_Validator extends OWLOntologyValidator {

	public HornSHOIF_OWLOntology_Validator(OWLOntology inputOWLOntology) {
		super(inputOWLOntology, DLFragment.HORN_SHOIF);

	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new HornSHOIF_AxiomValidator();

	}

}
