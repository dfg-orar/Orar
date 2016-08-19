package orar.dlfragmentvalidator.HornALCHOIF;

import orar.dlfragmentvalidator.OrarOntologyValidator;
import orar.modeling.ontology.OrarOntology;

public class HornALCHOIF_OrarOntology_Validator extends OrarOntologyValidator {

	public HornALCHOIF_OrarOntology_Validator(OrarOntology inputOrarOntology) {
		super(inputOrarOntology);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new HornALCHOIF_AxiomValidator();
		this.dlfrangment = "hornALCHOIF";
	}

}
