package orar.dlfragmentvalidator.DLLiteHOD;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import orar.dlfragmentvalidator.TBoxValidator;

public class DLLiteHOD_TBox_Validator extends TBoxValidator {
	public DLLiteHOD_TBox_Validator(Set<OWLAxiom> inputTBoxAxioms) {
		super(inputTBoxAxioms);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new DLLiteHOD_AxiomValidator();
		this.dlfrangment = "DLLiteHOD";
	}
}
