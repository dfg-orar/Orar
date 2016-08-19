package orar.dlfragmentvalidator.ALCHOIF;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import orar.dlfragmentvalidator.TBoxValidator;

public class ALCHOIF_TBox_Validator extends TBoxValidator {

	public ALCHOIF_TBox_Validator(Set<OWLAxiom> inputTBoxAxioms) {
		super(inputTBoxAxioms);
	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new ALCHOIF_AxiomValidator();
		this.dlfrangment = "ALCHOIF";
	}

}
