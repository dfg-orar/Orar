package orar.abstraction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.data.DataForTransferingEntailments;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

public class RoleAssertionBetweenRepresentativesXGenerator {
	// private final Logger logger =
	// Logger.getLogger(DLiteR_AbstractionGenerator.class);
	protected final DataForTransferingEntailments sharedMap;

	protected final OWLDataFactory owlDataFactory;
	/*
	* 
	*/
	protected final OrarOntology2 orarOntology;

	public RoleAssertionBetweenRepresentativesXGenerator(OrarOntology2 orarOntology) {
		this.sharedMap = DataForTransferingEntailments.getInstance();

		this.owlDataFactory = OWLManager.getOWLDataFactory();

		this.orarOntology = orarOntology;

		// this.mapIndividual2XAbstract= new HashMap<>();

	}

	public Set<OWLAxiom> getRoleAssertionBetweenRepresentativesX() {

		Set<OWLAxiom> generatedSucRoleAssertions = new HashSet<OWLAxiom>();
		Set<Integer> allOriginalIndividuals = orarOntology.getIndividualsInSignature();

		for (Integer eachOriginalInd : allOriginalIndividuals) {
			OWLNamedIndividual abstract_x1 = this.sharedMap.getMapIndividual2XAbstract().get(eachOriginalInd);
			Map<OWLObjectProperty, Set<Integer>> succRolesMap = this.orarOntology
					.getSuccessorRoleAssertionsAsMap(eachOriginalInd);
			Iterator<Entry<OWLObjectProperty, Set<Integer>>> iterator = succRolesMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<OWLObjectProperty, Set<Integer>> succRoleMap2Successors = iterator.next();
				OWLObjectProperty succRole = succRoleMap2Successors.getKey();
				for (Integer succIndivs : succRoleMap2Successors.getValue()) {
					OWLNamedIndividual abstract_x2 = this.sharedMap.getMapIndividual2XAbstract().get(succIndivs);
					OWLObjectPropertyAssertionAxiom x_succRole_Xprime = this.owlDataFactory
							.getOWLObjectPropertyAssertionAxiom(succRole, abstract_x1, abstract_x2);
					generatedSucRoleAssertions.add(x_succRole_Xprime);
				}
			}
		}

		return generatedSucRoleAssertions;
	}
}
