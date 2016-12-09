package orar.abstraction.HornSHOIF_Increment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import orar.abstraction.HornSHIF.HornSHIF_AbstractionGenerator;
import orar.abstraction.HornSHIF_Increment.HornSHIF_AbstractionGenerator_Increment;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;

public class HornSHOIF_AbstractionGenerator_Increment extends HornSHIF_AbstractionGenerator_Increment {

	public HornSHOIF_AbstractionGenerator_Increment(OrarOntology2 orarOntology,
			Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		super(orarOntology, typeMap2Individuals);

	}

	@Override
	protected Set<OWLAxiom> getConceptAssertionsForConceptType(IndividualType type) {
		Set<OWLAxiom> abstractAssertions = new HashSet<OWLAxiom>();
		/*
		 * create u
		 */
		OWLNamedIndividual u = abstractDataFactory.createAbstractIndividualU();
		/*
		 * map u to original individuals.
		 */
		Set<Integer> originalIndsForThisType = this.typeMap2Individuals.get(type);
		sharedMap.getMap_UAbstractIndiv_2_OriginalIndivs().put(u, originalIndsForThisType);
		/*
		 * create abstract class assertions for u
		 */
		abstractAssertions.addAll(getConceptAssertions(u, type));
		return abstractAssertions;
	}

}
