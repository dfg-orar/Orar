package orar.dlfragmentvalidator.ALCHOIF;

import orar.dlfragmentvalidator.OrarOntologyValidator;
import orar.modeling.ontology.OrarOntology;

public class ALCHOIF_OrarOntology_Validator extends OrarOntologyValidator {

	public ALCHOIF_OrarOntology_Validator(OrarOntology inputOrarOntology) {
		super(inputOrarOntology);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new ALCHOIF_AxiomValidator();
		this.dlfrangment = "hornALCHOIF";
	}

}
