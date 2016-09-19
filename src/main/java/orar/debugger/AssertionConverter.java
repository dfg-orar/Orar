package orar.debugger;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.indexing.IndividualIndexer;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

/**
 * Convert abstract assertions to original assertions
 * 
 * @author kien
 *
 */
public class AssertionConverter {
	private final DataForTransferingEntailments mappings;
	private final AbstractDataFactory abstractDataFactory;
	private OWLDataFactory owlDataFactory;
	private final OrarOntology2 orarOntology;
	private final IndividualIndexer indexer = IndividualIndexer.getInstance();

	public AssertionConverter(OrarOntology2 orarOntology) {
		this.mappings = DataForTransferingEntailments.getInstance();
		this.abstractDataFactory = AbstractDataFactory.getInstance();
		this.orarOntology = orarOntology;
		this.owlDataFactory = OWLManager.getOWLDataFactory();
	}

	public Set<OWLAxiom> getOriginalAxioms(Set<OWLAxiom> abstractAxioms) {
		Set<OWLAxiom> originalAxioms = new HashSet<OWLAxiom>();
		for (OWLAxiom axiom : abstractAxioms) {
			if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
				originalAxioms.addAll(getOriginalAssertions((OWLClassAssertionAxiom) axiom));
			} else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
				originalAxioms.addAll(getOriginalAssertions((OWLObjectPropertyAssertionAxiom) axiom));
			} else {
				originalAxioms.add(axiom);
			}

		}
		return originalAxioms;
	}

	private Set<OWLAxiom> getOriginalAssertions(OWLClassAssertionAxiom abstractConceptAssertion) {
		Set<OWLAxiom> originalConceptAssertions = new HashSet<OWLAxiom>();
		OWLClass concept = abstractConceptAssertion.getClassExpression().asOWLClass();
		OWLNamedIndividual abstractInd = abstractConceptAssertion.getIndividual().asOWLNamedIndividual();
		Set<Integer> originalIndivisualIndexes = this.mappings.getOriginalIndividuals(abstractInd);
		Set<OWLNamedIndividual> originalIndividuals = indexer.getOWLIndividuals(originalIndivisualIndexes);
		for (OWLNamedIndividual ind : originalIndividuals) {
			OWLClassAssertionAxiom originalConceptAssertion = this.owlDataFactory.getOWLClassAssertionAxiom(concept,
					ind);
			originalConceptAssertions.add(originalConceptAssertion);
		}
		return originalConceptAssertions;

	}

	private Set<OWLAxiom> getOriginalAssertions(OWLObjectPropertyAssertionAxiom abstractRoleAssertion) {
		Set<OWLAxiom> originalRoleAssertions = new HashSet<OWLAxiom>();
		OWLObjectProperty role = abstractRoleAssertion.getProperty().asOWLObjectProperty();
		OWLNamedIndividual abstractSubject = abstractRoleAssertion.getSubject().asOWLNamedIndividual();
		OWLNamedIndividual abstractObject = abstractRoleAssertion.getObject().asOWLNamedIndividual();

		if (this.abstractDataFactory.getXAbstractIndividuals().contains(abstractSubject)) {
			originalRoleAssertions.addAll(getOriginalSuccessors(abstractObject, role));
		} else if (this.abstractDataFactory.getXAbstractIndividuals().contains(abstractObject)) {
			originalRoleAssertions.addAll(getOriginalPredecessors(role, abstractObject));
		}

		return originalRoleAssertions;

	}

	private Set<OWLObjectPropertyAssertionAxiom> getOriginalSuccessors(OWLNamedIndividual xAbstract,
			OWLObjectProperty sucRole) {
		Set<OWLObjectPropertyAssertionAxiom> originalRoleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		Set<Integer> originalIndexedIndividualOfX = this.mappings.getOriginalIndividuals(xAbstract);
	
		
		for (Integer eachOriginalInd_ofX : originalIndexedIndividualOfX) {
			Set<Integer> allSuccessorsIndexes = this.orarOntology.getSuccessors(eachOriginalInd_ofX, sucRole);
		 
			for (Integer eachSuccessor : allSuccessorsIndexes) {
				OWLNamedIndividual subject = indexer.getOWLIndividual(eachOriginalInd_ofX);
				OWLNamedIndividual object = indexer.getOWLIndividual(eachSuccessor);
				OWLObjectPropertyAssertionAxiom roleAssertion = this.owlDataFactory
						.getOWLObjectPropertyAssertionAxiom(sucRole, subject, object);
				originalRoleAssertions.add(roleAssertion);
			}
		}
		return originalRoleAssertions;
	}

	private Set<OWLObjectPropertyAssertionAxiom> getOriginalPredecessors(OWLObjectProperty preRole,
			OWLNamedIndividual xAbstract) {
		Set<OWLObjectPropertyAssertionAxiom> originalRoleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		Set<Integer> originalIndexedIndividualOfX = this.mappings.getOriginalIndividuals(xAbstract);
	
		// Set<OWLNamedIndividual> originalIndividualsOfX =
		// this.mappings.getOriginalIndividuals(xAbstract);
		for (Integer eachOriginalInd_ofX : originalIndexedIndividualOfX) {
			Set<Integer> allPredecessors = this.orarOntology.getPredecessors(eachOriginalInd_ofX, preRole);
			for (Integer eachPredecessor : allPredecessors) {
				OWLNamedIndividual predecessor = indexer.getOWLIndividual(eachPredecessor);
				OWLNamedIndividual eachOriginalOWLIndividualOfX = indexer.getOWLIndividual(eachOriginalInd_ofX);
				OWLObjectPropertyAssertionAxiom roleAssertion = this.owlDataFactory
						.getOWLObjectPropertyAssertionAxiom(preRole, predecessor, eachOriginalOWLIndividualOfX);
				originalRoleAssertions.add(roleAssertion);
			}
		}
		return originalRoleAssertions;
	}
}
