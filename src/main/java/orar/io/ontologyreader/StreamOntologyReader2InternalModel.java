package orar.io.ontologyreader;

import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.indexing.IndividualIndexer;
import orar.io.aboxstreamreader.ABoxStreamReader;
import orar.io.aboxstreamreader.JenaMultipleABoxesStreamReader;
import orar.modeling.ontology2.MapbasedOrarOntology2;
import orar.modeling.ontology2.OrarOntology2;

public class StreamOntologyReader2InternalModel {

	private final OrarOntology2 internalOntology;

	private OWLOntology owlOntologyTBox;
	private String aboxListFileName;
	ABoxStreamReader aboxReader;
	private boolean done = false;
	private IndividualIndexer indexer;

	/**
	 * @param owlOntologyTBox
	 *            an OWLAPI internalOntology containing TBox, RBox axioms, and
	 *            possibly some assertions.
	 * @param aboxListFile
	 *            a file contain a list of ABox files
	 */
	public StreamOntologyReader2InternalModel(OWLOntology owlOntologyTBox, String aboxListFile) {
		this.internalOntology = new MapbasedOrarOntology2();
		this.owlOntologyTBox = owlOntologyTBox;
		this.aboxListFileName = aboxListFile;

		Set<OWLObjectProperty> definedObjectProperties = this.owlOntologyTBox.getObjectPropertiesInSignature(true);
		Set<OWLClass> definedClasses = this.owlOntologyTBox.getClassesInSignature(true);

		this.aboxReader = new JenaMultipleABoxesStreamReader(definedObjectProperties, definedClasses, aboxListFileName,
				internalOntology);
		this.indexer = IndividualIndexer.getInstance();
	}

	private void readFromTBox() {

		this.internalOntology.addTBoxAxioms(owlOntologyTBox.getTBoxAxioms(true));

		this.internalOntology.addTBoxAxioms(owlOntologyTBox.getRBoxAxioms(true));

		// this.internalOntology.addAllTBoxAxioms(owlOntology.getAxioms(
		// AxiomType.FUNCTIONAL_OBJECT_PROPERTY, true));
		//
		// this.internalOntology.addAllTBoxAxioms(owlOntology.getAxioms(
		// AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, true));
		//
		// this.internalOntology.addAllTBoxAxioms(owlOntology.getAxioms(
		// AxiomType.INVERSE_OBJECT_PROPERTIES, true));

	}

	private void readFromABoxes() {

		aboxReader.readABoxes();

		/*
		 * Add assertions (if any) from owlOntology to internalOntology
		 */
		addClassAssertions();
		addObjectPropertyAssertions();

	}

	private void addClassAssertions() {

		for (OWLClassAssertionAxiom classAssertion : owlOntologyTBox.getAxioms(AxiomType.CLASS_ASSERTION, true)) {
			// OWLNamedIndividual individual =
			// classAssertion.getIndividual().asOWLNamedIndividual();
			String individualString = classAssertion.getIndividual().asOWLNamedIndividual().getIRI().toString();
			int individualIndex = indexer.getIndexOfIndividualString(individualString);

			OWLClass owlClass = classAssertion.getClassExpression().asOWLClass();

			if (this.internalOntology.addConceptAssertion(individualIndex, owlClass)) {
				this.internalOntology.increaseNumberOfInputConceptAssertions(1);
			}
		}
	}

	private void addObjectPropertyAssertions() {
		for (OWLObjectPropertyAssertionAxiom assertion : owlOntologyTBox.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION,
				true)) {

			String subjectString = assertion.getSubject().asOWLNamedIndividual().getIRI().toString();
			int subjectIndex = indexer.getIndexOfIndividualString(subjectString);

			OWLObjectProperty property = assertion.getProperty().asOWLObjectProperty();

			String objectString = assertion.getObject().asOWLNamedIndividual().getIRI().toString();
			int objectIndex = indexer.getIndexOfIndividualString(objectString);

			if (this.internalOntology.addRoleAssertion(subjectIndex, property, objectIndex)) {
				this.internalOntology.increaseNumberOfInputRoleAssertions(1);
			}

		}
	}

	private void getSignature() {

		this.internalOntology.addIndividualsToSignature(
				indexer.getIndexesOfOWLIndividuals(owlOntologyTBox.getIndividualsInSignature(true)));

		this.internalOntology.addRoleNamesToSignature(owlOntologyTBox.getObjectPropertiesInSignature(true));

		this.internalOntology.addConceptNamesToSignature(owlOntologyTBox.getClassesInSignature(true));
		this.internalOntology.addIndividualsToSignature(IndividualIndexer.getInstance().getAllEncodedIndividuals());
	}

	private void readOntology() {

		readFromTBox();
		readFromABoxes();
		/*
		 * Note: getSignature needs to be called after readFromABoxes();
		 */
		getSignature();
		this.done = true;
	}

	public OrarOntology2 getOntology() {
		if (!done) {
			readOntology();
		}
		return this.internalOntology;
	}

}
