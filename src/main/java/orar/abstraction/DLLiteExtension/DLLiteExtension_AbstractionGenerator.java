package orar.abstraction.DLLiteExtension;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.abstraction.PairOfSubjectAndObject;
import orar.abstraction.HornSHOIF.HornSHOIF_AbstractionGenerator;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;

/**
 * The diffrence is that we don't need to map y,z to the original individuals.
 * 
 * @author kien
 *
 */
public class DLLiteExtension_AbstractionGenerator extends HornSHOIF_AbstractionGenerator {

	public DLLiteExtension_AbstractionGenerator(OrarOntology2 orarOntology,
			Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		super(orarOntology, typeMap2Individuals);

	}

	protected Set<OWLAxiom> getPredecessorRoleAssertions(OWLNamedIndividual x, IndividualType type) {
		Set<OWLAxiom> preRoleAssertions = new HashSet<OWLAxiom>();
		for (OWLObjectProperty preRole : type.getPredecessorRoles()) {
			// if (propertyIn == null) {
			// Printer.printSet(type.getPreRoles());
			// }
			/*
			 * generate z and get role assertion for it.
			 */
			OWLNamedIndividual z = abstractDataFactory.createAbstractIndividualZ();

			OWLObjectPropertyAssertionAxiom preRoleAssertion = owlDataFactory
					.getOWLObjectPropertyAssertionAxiom(preRole, z, x);
			preRoleAssertions.add(preRoleAssertion);

			/*
			 * Mark z if z has an inverse functional pre-role
			 */
			markZHavingInverseFunctionalRole(z, preRole);

			/*
			 * map z to original individuals
			 */
			// Set<Integer> originalIndsMappedToZ = new HashSet<Integer>();
			//
			// Set<Integer> originalIndsForX =
			// sharedMap.getMap_XAbstractIndiv_2_OriginalIndivs().get(x);
			// for (Integer indForX : originalIndsForX) {
			// originalIndsMappedToZ.addAll(orarOntology.getPredecessors(indForX,
			// preRole));
			// }
			// sharedMap.getMap_ZAbstractIndiv_2_OriginalIndivs().put(z,
			// originalIndsMappedToZ);

			/*
			 * map: (z,x) --> preRole if preRole is inverse functional
			 */
			if (this.metaDataOfOntology.getInverseFunctionalRoles().contains(preRole)) {
				PairOfSubjectAndObject zxPair = new PairOfSubjectAndObject(z, x);

				this.sharedMap.getMap_ZX_2_Role().put(zxPair, preRole);
			}

		}
		return preRoleAssertions;

	}

	protected Set<OWLAxiom> getSuccessorRoleAssertions(OWLNamedIndividual x, IndividualType type) {
		Set<OWLAxiom> propertyAssertions = new HashSet<OWLAxiom>();
		for (OWLObjectProperty succRole : type.getSuccessorRoles()) {
			/*
			 * get y and its assertion
			 */
			OWLNamedIndividual y = abstractDataFactory.createAbstractIndividualY();

			OWLObjectPropertyAssertionAxiom outPropertyAssertion = owlDataFactory
					.getOWLObjectPropertyAssertionAxiom(succRole, x, y);

			propertyAssertions.add(outPropertyAssertion);

			/*
			 * map y to original individuals
			 */
			// Set<Integer> originalIndsMappedToY = new HashSet<Integer>();
			//
			// Set<Integer> originalIndsForX =
			// sharedMap.getMap_XAbstractIndiv_2_OriginalIndivs().get(x);
			// for (Integer indForX : originalIndsForX) {
			// originalIndsMappedToY.addAll(orarOntology.getSuccessors(indForX,
			// succRole));
			// }
			// sharedMap.getMap_YAbstractIndiv_2_OriginalIndivs().put(y,
			// originalIndsMappedToY);

			/*
			 * map: (x,y) --> succRole if succRole is functional
			 */
			if (this.metaDataOfOntology.getFunctionalRoles().contains(succRole)) {
				PairOfSubjectAndObject xyPair = new PairOfSubjectAndObject(x, y);

				this.sharedMap.getMap_XY_2_Role().put(xyPair, succRole);
			}

		}
		return propertyAssertions;
	}

}
