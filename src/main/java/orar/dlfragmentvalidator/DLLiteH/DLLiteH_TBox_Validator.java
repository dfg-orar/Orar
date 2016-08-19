package orar.dlfragmentvalidator.DLLiteH;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import orar.dlfragmentvalidator.TBoxValidator;

public class DLLiteH_TBox_Validator extends TBoxValidator {
	public DLLiteH_TBox_Validator(Set<OWLAxiom> inputTBoxAxioms) {
		super(inputTBoxAxioms);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new DLLiteH_AxiomValidator();
		this.dlfrangment = "DLLiteR";
	}
}
