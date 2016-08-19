package orar.dlfragmentvalidator.DLLiteH;

import orar.dlfragmentvalidator.OrarOntologyValidator;
import orar.modeling.ontology.OrarOntology;

public class DLLiteH_OrarOntology_Validator extends OrarOntologyValidator {

	public DLLiteH_OrarOntology_Validator(OrarOntology inputOrarOntology) {
		super(inputOrarOntology);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new DLLiteH_AxiomValidator();
		this.dlfrangment = "DLLite_R";
	}

}
