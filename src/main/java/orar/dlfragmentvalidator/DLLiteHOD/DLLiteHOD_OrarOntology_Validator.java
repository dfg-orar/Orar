package orar.dlfragmentvalidator.DLLiteHOD;

import orar.dlfragmentvalidator.OrarOntologyValidator;
import orar.modeling.ontology.OrarOntology;

public class DLLiteHOD_OrarOntology_Validator extends OrarOntologyValidator {

	public DLLiteHOD_OrarOntology_Validator(OrarOntology inputOrarOntology) {
		super(inputOrarOntology);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new DLLiteHOD_AxiomValidator();
		this.dlfrangment = "DLLite_HOD";
	}

}
