package orar.dlfragmentvalidator.DLLiteH;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.dlfragmentvalidator.DLFragment;
import orar.dlfragmentvalidator.OWLOntologyValidator;

public class DLLiteH_OWLOntology_Validator extends OWLOntologyValidator {

	public DLLiteH_OWLOntology_Validator(OWLOntology inputOWLOntology) {
		super(inputOWLOntology, DLFragment.DLLITE_R);

	}

	@Override
	public void initAxiomValidator() {
		this.axiomValidator = new DLLiteH_AxiomValidator();

	}

}
