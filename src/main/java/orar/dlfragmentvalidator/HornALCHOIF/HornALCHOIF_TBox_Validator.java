package orar.dlfragmentvalidator.HornALCHOIF;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import orar.dlfragmentvalidator.TBoxValidator;

public class HornALCHOIF_TBox_Validator extends TBoxValidator {

	public HornALCHOIF_TBox_Validator(Set<OWLAxiom> inputTBoxAxioms) {
		super(inputTBoxAxioms);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new HornALCHOIF_AxiomValidator();
		this.dlfrangment = "hornALCHOIF";
	}

}
