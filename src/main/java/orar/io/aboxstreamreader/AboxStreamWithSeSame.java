package orar.io.aboxstreamreader;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;



import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.helpers.RDFHandlerBase;
import org.openrdf.rio.rdfxml.RDFXMLParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Customized RDFHander class to process triples. It just count how many
 * triples/statements. Each statement is parsed in streaming way.
 * 
 * @author kien
 *
 */
public class AboxStreamWithSeSame extends RDFHandlerBase {

	private int countedStatements;

	Set<OWLClassAssertionAxiom> conceptAssertions;
	Set<OWLObjectPropertyAssertionAxiom> roleAssertions;
	OWLDataFactory dataFactory;

	public AboxStreamWithSeSame() {
		this.countedStatements = 0;
		this.conceptAssertions = new HashSet<OWLClassAssertionAxiom>();
		this.roleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		this.dataFactory = manager.getOWLDataFactory();

	}

	@Override
	public void handleStatement(Statement st) {
		if (!(st.getObject() instanceof LiteralImpl)
				&& !(st.getSubject() instanceof LiteralImpl)) {
			countedStatements++;
			// if (countedStatements < 20){
			processStatement(st);
			// System.out.println(st);
			// System.out.println(st.getPredicate());
			// }
		}
	}

	private void processStatement(Statement st) {

		URI predicate = st.getPredicate();
		String subject = st.getSubject().stringValue();
		String object = st.getObject().stringValue();
		if (predicate.stringValue().equals(
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
			// System.out.println("Concept assertion");
			System.out.println(predicate);

			System.out.println("Subject:           " + subject);
			System.out.println("Object:            " + object);

			OWLClass owlConcept = dataFactory.getOWLClass(IRI.create(object));
			OWLNamedIndividual owlIndividual = dataFactory
					.getOWLNamedIndividual(IRI.create(subject));
			this.conceptAssertions.add(dataFactory.getOWLClassAssertionAxiom(
					owlConcept, owlIndividual));
		}
		// role assertion
		else {

			OWLNamedIndividual subjectInd = dataFactory
					.getOWLNamedIndividual(IRI.create(st.getSubject()
							.stringValue()));
			OWLNamedIndividual objectInd = dataFactory
					.getOWLNamedIndividual(IRI.create(st.getObject()
							.stringValue()));
			OWLObjectProperty property = dataFactory.getOWLObjectProperty(IRI
					.create(st.getPredicate().stringValue()));

			System.out.println(predicate);

			System.out.println("Subject:           " + subject);
			System.out.println("Object:            " + object);

			this.roleAssertions.add(dataFactory
					.getOWLObjectPropertyAssertionAxiom(property, subjectInd,
							objectInd));
		}
	}

	public int getCountedStatements() {
		return countedStatements;
	}

	public static void main(String[] args) throws RDFParseException,
			RDFHandlerException, IOException {
		long count = 0;
		RDFParser rdfParser = new RDFXMLParser();
		AboxStreamWithSeSame rdfHandler = new AboxStreamWithSeSame();

		// for (int i = 0; i < 100; i++) {
		// String fileName = "/Users/kien/benchmarks/uobm/uobm100/univ" + i
		// + ".owl";
		// File file = new File(fileName);
		// InputStream inputStream = new FileInputStream(file);
		// rdfParser.setRDFHandler(rdfHandler);
		// rdfParser.parse(inputStream,
		// "http://swat.cse.lehigh.edu/onto/univ-bench.owl");
		// System.out.println(i);
		// System.out.println(rdfHandler.getCountedStatements());
		// }

		String fileName = "src/test/resources/streamreadertest/testuobm.owl";
//		String fileName = "src/test/resources/streamreadertest/test1.owl";
		File file = new File(fileName);
		InputStream inputStream = new FileInputStream(file);
		rdfParser.setRDFHandler(rdfHandler);
		rdfParser.parse(inputStream,
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl");
		// System.out.println(i);

		System.out.println("Number of class assertions:"
				+ rdfHandler.conceptAssertions.size());
		System.out.println("Number of property assertions:"
				+ rdfHandler.roleAssertions.size());
		System.out.println("Total assertions: " + rdfHandler.countedStatements);
	}

}
