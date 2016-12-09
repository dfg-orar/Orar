package orar.materializer.HornSHOIF_Increment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.HornSHOIF.HornSHOIF_AbstractionGenerator;
import orar.abstraction.HornSHOIF_Increment.HornSHOIF_AbstractionGenerator_Increment;
import orar.innerreasoner.InnerReasoner;
import orar.materializer.MaterializerTemplate2;
import orar.materializer.MaterializerTemplate_Increment;
import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.AssertionTransporter;
import orar.refinement.assertiontransferring.HornSHOIF.HornSHOIF_AssertionTransporterOptimized;
import orar.refinement.assertiontransferring.HornSHOIF_Increment.HornSHOIF_AssertionTransporterOptimized_Increment;
import orar.type.IndividualType;

public abstract class HornSHOIF_MaterializerOptimized_Increment extends MaterializerTemplate_Increment {

	public HornSHOIF_MaterializerOptimized_Increment(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected List<OWLOntology> getAbstractions(Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		AbstractionGenerator abstractionGenerator = new HornSHOIF_AbstractionGenerator_Increment(normalizedORAROntology,
				typeMap2Individuals);
		List<OWLOntology> abstractions = new ArrayList<OWLOntology>();
		abstractions.add(abstractionGenerator.getAbstractOntology());
		return abstractions;
	}

	@Override
	protected AssertionTransporter getAssertionTransporter(
			Map<OWLNamedIndividual, Set<OWLClass>> entailedAbstractConceptAssertionsForX,
			Map<OWLNamedIndividual, Set<OWLClass>> entailedAbstractConceptAssertionsForY,
			Map<OWLNamedIndividual, Set<OWLClass>> entailedAbstractConceptAssertionsForZ,
			AbstractRoleAssertionBox entailedAbstractRoleAssertion,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> entailedSameasMap) {
		AssertionTransporter assertionTransporter = new HornSHOIF_AssertionTransporterOptimized_Increment(normalizedORAROntology,
				entailedAbstractConceptAssertionsForX, entailedAbstractConceptAssertionsForY,
				entailedAbstractConceptAssertionsForZ, entailedAbstractRoleAssertion, entailedSameasMap);
		return assertionTransporter;
	}

	@Override
	protected abstract InnerReasoner getInnerReasoner(OWLOntology abstraction);

	// public boolean isOntologyConsistent() {
	// return true;
	// // TODO:FIX ME.
	// // does not support this for Horn SHOIF yet.
	// }
}
