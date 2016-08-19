package orar.dlfragmentvalidator.DLLiteHOD;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.DLFragment;
import orar.dlfragmentvalidator.OWLOntologyValidator;
import orar.dlfragmentvalidator.DLLiteH.DLLiteH_AxiomValidator;

public class DLLiteHOD_OWLOntology_Validator extends OWLOntologyValidator {

	public DLLiteHOD_OWLOntology_Validator(OWLOntology inputOWLOntology) {
		super(inputOWLOntology, DLFragment.DLLITE_HOD);

	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new DLLiteHOD_AxiomValidator();

	}

}
