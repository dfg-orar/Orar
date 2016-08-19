package orar.dlfragmentvalidator.HornSHOIF;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import orar.dlfragmentvalidator.DLConstructor;
import orar.dlfragmentvalidator.HornALCHOIF.HornALCHOIF_AxiomValidator;

public class HornSHOIF_AxiomValidator extends HornALCHOIF_AxiomValidator {

	@Override
	public OWLAxiom visit(OWLTransitiveObjectPropertyAxiom axiom) {
		this.constructorsInInputOntology.add(DLConstructor.TRANSITIVITY);
		this.constructorsInValidatedOntology.add(DLConstructor.TRANSITIVITY);
		return axiom;
	}
}
