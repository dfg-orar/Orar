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
import orar.modeling.ontology.OrarOntology;

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
	private final OrarOntology orarOntology;
	public AssertionConverter(OrarOntology orarOntology) {
		this.mappings = DataForTransferingEntailments.getInstance();
		this.abstractDataFactory= AbstractDataFactory.getInstance();
		this.orarOntology=orarOntology;
		this.owlDataFactory = OWLManager.getOWLDataFactory();
	}

	public Set<OWLAxiom> getOriginalAxioms(Set<OWLAxiom> abstractAxioms) {
		Set<OWLAxiom> originalAxioms = new HashSet<>();
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
		Set<OWLAxiom> originalConceptAssertions = new HashSet<>();
		OWLClass concept = abstractConceptAssertion.getClassExpression().asOWLClass();
		OWLNamedIndividual abstractInd = abstractConceptAssertion.getIndividual().asOWLNamedIndividual();
		Set<OWLNamedIndividual> originalInds = this.mappings.getOriginalIndividuals(abstractInd);
		for (OWLNamedIndividual ind : originalInds) {
			OWLClassAssertionAxiom originalConceptAssertion = this.owlDataFactory.getOWLClassAssertionAxiom(concept,
					ind);
			originalConceptAssertions.add(originalConceptAssertion);
		}
		return originalConceptAssertions;

	}

	private Set<OWLAxiom> getOriginalAssertions(OWLObjectPropertyAssertionAxiom abstractRoleAssertion) {
		Set<OWLAxiom> originalRoleAssertions = new HashSet<>();
		OWLObjectProperty role = abstractRoleAssertion.getProperty().asOWLObjectProperty();
		OWLNamedIndividual abstractSubject = abstractRoleAssertion.getSubject().asOWLNamedIndividual();
		OWLNamedIndividual abstractObject=abstractRoleAssertion.getObject().asOWLNamedIndividual();
		
		
		if (this.abstractDataFactory.getXAbstractIndividuals().contains(abstractSubject)){
			originalRoleAssertions.addAll(getOriginalSuccessors(abstractObject, role));
		} else if (this.abstractDataFactory.getXAbstractIndividuals().contains(abstractObject)) {
			originalRoleAssertions.addAll(getOriginalPredecessors(role, abstractObject));
		}
		
		
		
		return originalRoleAssertions;

	}
	
	private  Set<OWLObjectPropertyAssertionAxiom> getOriginalSuccessors(OWLNamedIndividual xAbstract, OWLObjectProperty sucRole){
		Set<OWLObjectPropertyAssertionAxiom> originalRoleAssertions = new HashSet<>();
		Set<OWLNamedIndividual> originalIndividualsOfX = this.mappings.getOriginalIndividuals(xAbstract);
		for (OWLNamedIndividual eachOriginalInd_ofX:originalIndividualsOfX){
			Set<OWLNamedIndividual> allSuccessors = this.orarOntology.getSuccessors(eachOriginalInd_ofX, sucRole);
			for (OWLNamedIndividual eachSuccessor:allSuccessors){
				OWLObjectPropertyAssertionAxiom roleAssertion = this.owlDataFactory.getOWLObjectPropertyAssertionAxiom(sucRole, eachOriginalInd_ofX, eachSuccessor);
				originalRoleAssertions.add(roleAssertion);
			}
		}
		return originalRoleAssertions;
	}
	
	private  Set<OWLObjectPropertyAssertionAxiom> getOriginalPredecessors( OWLObjectProperty preRole, OWLNamedIndividual xAbstract){
		Set<OWLObjectPropertyAssertionAxiom> originalRoleAssertions = new HashSet<>();
		Set<OWLNamedIndividual> originalIndividualsOfX = this.mappings.getOriginalIndividuals(xAbstract);
		for (OWLNamedIndividual eachOriginalInd_ofX:originalIndividualsOfX){
			Set<OWLNamedIndividual> allPredecessors = this.orarOntology.getPredecessors(eachOriginalInd_ofX, preRole);
			for (OWLNamedIndividual eachPredecessor:allPredecessors){
				OWLObjectPropertyAssertionAxiom roleAssertion = this.owlDataFactory.getOWLObjectPropertyAssertionAxiom(preRole, eachPredecessor,eachOriginalInd_ofX);
				originalRoleAssertions.add(roleAssertion);
			}
		}
		return originalRoleAssertions;
	}
}
