package orar.io.ontologyreader;

import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import orar.dlfragmentvalidator.ValidatorDataFactory;
import orar.indexing.IndividualIndexer;
import orar.modeling.ontology2.MapbasedOrarOntology2;
import orar.modeling.ontology2.OrarOntology2;

/**
 * Convert an OWLAPI ontology into the internal ontology data structure.
 * 
 * @author kien
 *
 */
public class OWLAPI2OrarConverter {
	private static final Logger logger = Logger.getLogger(OWLAPI2OrarConverter.class);
	private OWLOntology owlOntology;
	private IndividualIndexer indexer;
	private OrarOntology2 internalOntology;

	boolean done = false;

	public OWLAPI2OrarConverter(OWLOntology owlOntology) {
		this.owlOntology = owlOntology;
		this.internalOntology = new MapbasedOrarOntology2();
		this.indexer=IndividualIndexer.getInstance();
	}

	private void convert() {
		obtainSignature();
		obtainAxioms();
		obtainClassAssertions();
		obtainObjectPropertyAssertions();
		obtainSameasAssertions();
		done = true;
	}

	private void obtainSameasAssertions() {
		for (OWLSameIndividualAxiom sameasAssertions : owlOntology.getAxioms(AxiomType.SAME_INDIVIDUAL, true)) {
			Set<OWLNamedIndividual> individuals = sameasAssertions.getIndividualsInSignature();
			Set<Integer> individualStrings=indexer.getIndexesOfOWLIndividuals(individuals);
			this.internalOntology.addSameasAssertion(individualStrings);
		}

	}

	private void obtainSignature() {
		this.internalOntology.addIndividualsToSignature(indexer.getIndexesOfOWLIndividuals(owlOntology.getIndividualsInSignature(true)));
//		logger.info("***DEBUG*** all individuals:"+owlOntology.getIndividualsInSignature(true));
		this.internalOntology.addConceptNamesToSignature(owlOntology.getClassesInSignature(true));
		this.internalOntology.addRoleNamesToSignature(owlOntology.getObjectPropertiesInSignature(true));
	}

	private void obtainAxioms() {
		this.internalOntology.addTBoxAxioms(owlOntology.getTBoxAxioms(true));

		this.internalOntology.addTBoxAxioms(owlOntology.getRBoxAxioms(true));
		//
		// this.internalOntology.addAllTBoxAxioms(owlOntology.getAxioms(
		// AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, true));
		// this.internalOntology.getRBoxAxioms().addAll(
		// owlOntology.getAxioms(AxiomType.FUNCTIONAL_OBJECT_PROPERTY,
		// true));
		// this.internalOntology.getRBoxAxioms().addAll(
		// owlOntology
		// .getAxioms(AxiomType.INVERSE_OBJECT_PROPERTIES, true));
	}

	private void obtainClassAssertions() {

		for (OWLClassAssertionAxiom classAssertion : owlOntology.getAxioms(AxiomType.CLASS_ASSERTION, true)) {
			OWLNamedIndividual individual = ValidatorDataFactory.getInstance()
					.getNamedIndividual(classAssertion.getIndividual());

			// OWLNamedIndividual individual =
			// classAssertion.getIndividual().asOWLNamedIndividual();
			Integer individualIndex = indexer.getIndexOfOWLIndividual(individual);
			OWLClass owlClass = classAssertion.getClassExpression().asOWLClass();
			this.internalOntology.addConceptAssertion(individualIndex, owlClass);
		}
	}

	private void obtainObjectPropertyAssertions() {
		for (OWLObjectPropertyAssertionAxiom assertion : owlOntology.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION,
				true)) {

			OWLNamedIndividual subject = assertion.getSubject().asOWLNamedIndividual();
			 Integer subjectIndex = this.indexer.getIndexOfOWLIndividual(subject);
			
			OWLObjectPropertyExpression property = assertion.getProperty();
			
			OWLNamedIndividual object = assertion.getObject().asOWLNamedIndividual();
			 Integer objectIndex = this.indexer.getIndexOfOWLIndividual(object);
			
			if (property instanceof OWLObjectProperty) {
				if (subject instanceof OWLNamedIndividual && object instanceof OWLNamedIndividual) {
					this.internalOntology.addRoleAssertion(subjectIndex,
							property.asOWLObjectProperty(), objectIndex);
				}
			}

			if (property instanceof OWLObjectInverseOf) {
				if (subject instanceof OWLNamedIndividual && object instanceof OWLNamedIndividual) {
					this.internalOntology.addRoleAssertion(objectIndex, property.getNamedProperty(),
							subjectIndex);
				}
			}

		}
	}

	public OrarOntology2 getInternalOntology() {
		if (!done) {
			convert();
		}
		return this.internalOntology;
	}
}
