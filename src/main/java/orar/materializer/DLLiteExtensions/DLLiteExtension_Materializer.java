package orar.materializer.DLLiteExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.DLLiteExtension.DLLiteExtension_AbstractionGenerator;
import orar.innerreasoner.InnerReasoner;
import orar.materializer.DLLiteExtension_MaterializerTemplate;
import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.AssertionTransporter;
import orar.refinement.assertiontransferring.HornSHOIF.HornSHOIF_AssertionTransporter;
import orar.type.IndividualType;

public abstract class DLLiteExtension_Materializer extends DLLiteExtension_MaterializerTemplate {
	public DLLiteExtension_Materializer(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected List<OWLOntology> getAbstractions(Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		AbstractionGenerator abstractionGenerator = new DLLiteExtension_AbstractionGenerator(normalizedORAROntology,
				typeMap2Individuals);
		List<OWLOntology> abstractions = new ArrayList<OWLOntology>();
		abstractions.add(abstractionGenerator.getAbstractOntology());
		return abstractions;
	}

	@Override
	protected AssertionTransporter getAssertionTransporter(
			Map<OWLNamedIndividual, Set<OWLClass>> entailedAbstractConceptAssertions,
			AbstractRoleAssertionBox entailedAbstractRoleAssertion,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> entailedSameasMap) {
		AssertionTransporter assertionTransporter = new HornSHOIF_AssertionTransporter(normalizedORAROntology,
				entailedAbstractConceptAssertions, entailedAbstractRoleAssertion, entailedSameasMap);
		return assertionTransporter;
	}

	@Override
	protected abstract InnerReasoner getInnerReasoner(OWLOntology abstraction);
}
