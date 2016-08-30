package orar.refinement.assertiontransferring.DLLiteExtensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.HornSHOIF.HornSHOIF_AssertionTransporterOptimized;

/**
 * @author kien
 *
 */
public class DLLiteExtension_AssertionTransporter extends HornSHOIF_AssertionTransporterOptimized {

	public DLLiteExtension_AssertionTransporter(OrarOntology2 orarOntoloy,
			Map<OWLNamedIndividual, Set<OWLClass>> abstractConceptAssertionsAsMapForX,
			AbstractRoleAssertionBox abstractRoleAssertionBox,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> abstractSameasMap) {
		/*
		 * We just need to input the empty map for concept assertions of y,z
		 */
		super(orarOntoloy, abstractConceptAssertionsAsMapForX, new HashMap<>(), new HashMap<>(),
				abstractRoleAssertionBox, abstractSameasMap);

	}
}
