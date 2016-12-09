package orar.io.ontologyreader;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.io.aboxstreamreader.ABoxStreamReader;
import orar.io.aboxstreamreader.JenaMultipleABoxesStreamReader;

public class StreamOntologyReader2OWLAPI {

	// private final AromaOntology internalOntology;
	private OWLOntology owlOntology;
	private OWLOntology owlOntologyTBox;
	private String aboxListFileName;
	ABoxStreamReader aboxReader;
	private boolean done = false;
	private OWLOntologyManager manager;

	/**
	 * @param owlOntologyTBox
	 *            an OWLAPI internalOntology containing TBox, RBox axioms, and
	 *            possibly some assertions.
	 * @param aboxListFile
	 *            a file contain a list of ABox files
	 */
	public StreamOntologyReader2OWLAPI(OWLOntology owlOntologyTBox,
			String aboxListFile) {
		this.manager = OWLManager.createOWLOntologyManager();
		createNewOWLOntology();
		this.owlOntologyTBox = owlOntologyTBox;
		this.aboxListFileName = aboxListFile;

		Set<OWLObjectProperty> definedObjectProperties = this.owlOntologyTBox
				.getObjectPropertiesInSignature(true);
		Set<OWLClass> definedClasses = this.owlOntologyTBox
				.getClassesInSignature(true);

		this.aboxReader = new JenaMultipleABoxesStreamReader(definedObjectProperties,
				definedClasses, aboxListFileName, owlOntology);

	}

	private void createNewOWLOntology() {

		try {
			this.owlOntology = manager.createOntology();
		} catch (OWLOntologyCreationException e) {

			e.printStackTrace();
		}
	}

	private void readFromTBox() {

		manager.addAxioms(this.owlOntology,
				this.owlOntologyTBox.getTBoxAxioms(true));

		manager.addAxioms(this.owlOntology,
				this.owlOntologyTBox.getRBoxAxioms(true));

		manager.addAxioms(this.owlOntology,
				this.owlOntologyTBox.getABoxAxioms(true));

	}

	private void readFromABoxes() {

		aboxReader.readABoxes();

	}

	private void readOntology() {

		readFromTBox();
		readFromABoxes();
		/*
		 * Note: getSignature needs to be called after readFromABoxes();
		 */
		// getSignature();
		this.done = true;
	}

	public OWLOntology getOWLAPIOntology() {
		if (!done) {
			readOntology();
		}
		return this.owlOntology;
	}

}
