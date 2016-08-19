package orar.dlfragmentvalidator.HornALCHOIF;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.DLFragment;
import orar.dlfragmentvalidator.OWLOntologyValidator;

public class HornALCHOIF_OWLOntology_Validator extends OWLOntologyValidator {

	public HornALCHOIF_OWLOntology_Validator(OWLOntology inputOWLOntology) {
		super(inputOWLOntology, DLFragment.HORN_ALCHOIF);

	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new HornALCHOIF_AxiomValidator();

	}

}
