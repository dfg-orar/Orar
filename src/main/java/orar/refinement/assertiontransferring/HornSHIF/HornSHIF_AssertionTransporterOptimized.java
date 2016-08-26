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
import orar.refinement.assertiontransferring.AssertionTransporterTemplateOptimized;

public class HornSHIF_AssertionTransporterOptimized extends AssertionTransporterTemplateOptimized {

	public HornSHIF_AssertionTransporterOptimized(OrarOntology2 orarOntoloy,
			Map<OWLNamedIndividual, Set<OWLClass>> xAstractConceptAssertionsAsMapInput,
			Map<OWLNamedIndividual, Set<OWLClass>> yAstractConceptAssertionsAsMapInput,
			Map<OWLNamedIndividual, Set<OWLClass>> zAstractConceptAssertionsAsMapInput,
			AbstractRoleAssertionBox abstractRoleAssertionBox) {
		super(orarOntoloy);
		this.xAbstractConceptAssertionsAsMap = xAstractConceptAssertionsAsMapInput;
		this.yAbstractConceptAssertionsAsMap=yAstractConceptAssertionsAsMapInput;
		this.zAbstractConceptAssertionsAsMap=zAstractConceptAssertionsAsMapInput;
				
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
