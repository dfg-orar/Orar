package orar.materializer.HornSHIF;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.HornSHIF.HornSHIF_AbstractionGenerator;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.AssertionTransporter;
import orar.refinement.assertiontransferring.HornSHIF.HornSHIF_AssertionTransporter;
import orar.type.IndividualType;
import x.innerreasoner.InnerReasoner;
import x.materializer.MaterializerTemplate;

public abstract class HornSHIF_Materializer extends MaterializerTemplate {

	public HornSHIF_Materializer(OrarOntology2 normalizedOrarOntology) {
		super(normalizedOrarOntology);

	}

	@Override
	protected List<OWLOntology> getAbstractions(Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		int numberOfTypesPerOntology = this.config.getNumberOfTypePerOntology();
		/*
		 * 
		 */
		if (numberOfTypesPerOntology < 0) {
			numberOfTypesPerOntology = typeMap2Individuals.size();
		}
		AbstractionGenerator abstractionGenerator = new HornSHIF_AbstractionGenerator(normalizedORAROntology,
				typeMap2Individuals);
		List<OWLOntology> abstractions = abstractionGenerator.getAbstractOntologies(numberOfTypesPerOntology);
		return abstractions;
	}

	@Override
	protected AssertionTransporter getAssertionTransporter(
			Map<OWLNamedIndividual, Set<OWLClass>> entailedAbstractConceptAssertions,
			AbstractRoleAssertionBox entailedAbstractRoleAssertion,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> entailedSameasMap) {
		AssertionTransporter assertionTransporter = new HornSHIF_AssertionTransporter(normalizedORAROntology,
				entailedAbstractConceptAssertions, entailedAbstractRoleAssertion);
		return assertionTransporter;
	}

	@Override
	protected abstract InnerReasoner getInnerReasoner(OWLOntology abstraction);

	public boolean isOntologyConsistent(){
		return true;
		//TODO:FIX ME
		// does not support this for Horn SHIF yet.
	}
}
