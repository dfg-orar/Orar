package orar.refinement.assertiontransferring.HornSHIF;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.AssertionTransporterTemplate;

public class HornSHIF_AssertionTransporter extends AssertionTransporterTemplate {

	public HornSHIF_AssertionTransporter(OrarOntology2 orarOntoloy,
			Map<OWLNamedIndividual, Set<OWLClass>> abstractConceptAssertionsAsMap,
			AbstractRoleAssertionBox abstractRoleAssertionBox) {
		super(orarOntoloy);
		this.abstractConceptAssertionsAsMap = abstractConceptAssertionsAsMap;
		this.abstractRoleAssertionBox = abstractRoleAssertionBox;
		this.abstractSameasMap = new HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>>();
	}

	@Override
	protected void transferSameasAssertions() {
		// nothing to do.
	}

	@Override
	protected void tranferRoleAssertionsBetweenUX() {
		// nothing to do
	}
}
