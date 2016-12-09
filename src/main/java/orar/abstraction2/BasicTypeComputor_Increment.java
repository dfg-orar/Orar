package orar.abstraction2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

import orar.abstraction.BasicTypeComputor;
import orar.data.DataForTransferingEntailments_Increment;
import orar.modeling.conceptassertion2.ConceptAssertionBox2;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;

/**
 * Computing types for all individuals, taking equivalent individuals into
 * account.
 * 
 * @author kien
 *
 */
public class BasicTypeComputor_Increment extends BasicTypeComputor {
	private ConceptAssertionBox2 conceptAssertionBox;
	public BasicTypeComputor_Increment(OrarOntology2 orarOntology) {
		super(orarOntology);
		conceptAssertionBox = this.orarOntology.getConceptAssertionBox();
		this.typeMap2Individuals=DataForTransferingEntailments_Increment.getInstance().getMapType_2_Individuals();
	}

	@Override
	public Map<IndividualType, Set<Integer>> computeTypes() {
		super.computeTypes();
		makeConceptAssertionMapShareConceptTypes();
		return this.typeMap2Individuals;
	}

	/**
	 * Update concept assertion map in the ontology to make individuals of the
	 * same type share the same concept type.
	 */
	private void makeConceptAssertionMapShareConceptTypes() {
		ConceptAssertionBox2 conceptAssertionBox = this.orarOntology.getConceptAssertionBox();
		Iterator<Entry<IndividualType, Set<Integer>>> iterator = this.typeMap2Individuals.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<IndividualType, Set<Integer>> entry = iterator.next();
			Set<OWLClass> conceptType = entry.getKey().getConcepts();
			Set<Integer> individuals = entry.getValue();
			for (int eachIndiv : individuals) {
				conceptAssertionBox.setAssertedConcepts(eachIndiv, conceptType);
			}
		}
	}

	@Override
	public void computeTypeIncrementally(Set<Integer> individualsHavingNewAssertions) {
		ConceptAssertionBox2 conceptAssertionBox = this.orarOntology.getConceptAssertionBox();
		for (Integer indiv : individualsHavingNewAssertions) {

			computeTypeIncrementally(indiv);
		}
	}
	
	@Override
	public void computeTypeIncrementally(Integer individualHavingNewAssertions) {
		
		IndividualType newType = this.computeType(individualHavingNewAssertions);
		Set<Integer> existingOriginalIndividuals = this.typeMap2Individuals.get(newType);
		if (existingOriginalIndividuals == null) {
			existingOriginalIndividuals = new HashSet<>();
			/*
			 * put to the map type-->individuals
			 */
			this.typeMap2Individuals.put(newType, existingOriginalIndividuals);
			/*
			 * remove indiv from them map of its old type
			 */

		}
		existingOriginalIndividuals.add(individualHavingNewAssertions);

		/*
		 * update concept assertion map for indiv
		 */
		conceptAssertionBox.setAssertedConcepts(individualHavingNewAssertions, newType.getConcepts());
		
	}
}