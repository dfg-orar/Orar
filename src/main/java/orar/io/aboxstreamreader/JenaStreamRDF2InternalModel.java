package orar.io.aboxstreamreader;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.riot.system.StreamRDFBase;
//import org.apache.jena.riot.system.StreamRDFBase;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.indexing.IndividualIndexer;
import orar.modeling.ontology2.OrarOntology2;

/**
 * A RDF triple processing class based on Jena. It reads each triple from a file
 * and then add it to the OrarOntology; the triple is handled in
 * {@code #triple(Triple) method}.<br>
 * Like OWLAPI, we only accepts assertions whose concepts and roles are defined,
 * e.g. in the TBox. This requires a set of predefined concept and role names.
 * {@code #definedConceptNames, #definedRoleNames}
 * 
 * See more {@link https://jena.apache.org/documentation/io/rdf-input.html } and
 * {@link https://github.com/apache/jena/blob/master/jena-arq/src-examples/arq/examples/riot/ExRIOT_4.java}
 * for further information on using this class.
 * 
 * @author kien
 *
 */
public class JenaStreamRDF2InternalModel extends StreamRDFBase {
	private final Configuration config = Configuration.getInstance();
	private final String OWL_NAMESPACE = "http://www.w3.org/2002/07/owl#";
	private final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private final String OWL_ONTOLOGY = OWL_NAMESPACE + "Ontology";
	private final String OWL_IMPORTS = OWL_NAMESPACE + "imports";

	private final String OWL_NAMEDINDIVIDUAL = OWL_NAMESPACE + "NamedIndividual";
	private final String OWL_CLASS = OWL_NAMESPACE + "Class";
	private final String OWL_OBJECT_PROPERTY = OWL_NAMESPACE + "ObjectProperty";
	private final String OWL_DATA_PROPERTY = OWL_NAMESPACE + "DatatypeProperty";

	private final String RDF_TYPE = RDF_NAMESPACE + "type";
	private int numberOfAllTriples;
	private int numberOfDeclairedConceptAssertions;
	private int numberOfDeclariedRoleAssertions;

	private static final Logger logger = Logger.getLogger(JenaStreamRDF2InternalModel.class);
	// private final Set<OWLObjectProperty> definedRoleNames;
	// private final Set<OWLClass> definedConceptNames;

	private final Set<String> definedRoleStrings;
	private final Set<String> definedConceptStrings;
	// private Map<OWLNamedIndividual, Set<OWLClass>> classAssertionMap;
	// private Map<OWLNamedIndividual, Map<OWLObjectProperty,
	// Set<OWLNamedIndividual>>> objectPropertyAssertionMap;

	private OWLDataFactory owlDataFactory;
	private final OrarOntology2 orarOntology;
	private IndividualIndexer indexer;

	public JenaStreamRDF2InternalModel(Set<OWLObjectProperty> definedRoleNames, Set<OWLClass> definedConceptNames,
			OrarOntology2 resultingOntology) {

		// this.definedRoleNames = definedRoleNames;
		// this.definedConceptNames = definedConceptNames;
		this.definedConceptStrings = new HashSet<String>();
		this.definedRoleStrings = new HashSet<String>();
		this.orarOntology = resultingOntology;
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		this.numberOfAllTriples = 0;
		this.numberOfDeclairedConceptAssertions = 0;
		this.numberOfDeclariedRoleAssertions = 0;
		this.indexer = IndividualIndexer.getInstance();
		turnOWLAPISignatureToStrings(definedRoleNames, definedConceptNames);

	}

	private void turnOWLAPISignatureToStrings(Set<OWLObjectProperty> definedRoleNames,
			Set<OWLClass> definedConceptNames) {
		for (OWLObjectProperty role : definedRoleNames) {
			this.definedRoleStrings.add(role.getIRI().toString());
		}
		for (OWLClass concept : definedConceptNames) {
			this.definedConceptStrings.add(concept.getIRI().toString());
		}
	}

	@Override
	public void triple(Triple triple) {
		Node subject = triple.getSubject();
		Node predicate = triple.getPredicate();
		Node object = triple.getObject();

		if (isClassAssertion(subject, predicate, object)) {
			addClassAssertion(subject, object);

			// logger.info(""+subject +", "+ predicate +","+ object);
		} else if (isObjectPropertyAssertion(subject, predicate, object)) {
			addObjectPropertyAssertion(subject, predicate, object);

			// logger.info(""+subject +", "+ predicate +","+ object);
		}
		// if (this.numberOfAllTriples % 1000000 == 0) {
		// logger.info("number of triples so far:" + this.numberOfAllTriples);
		// }
		// TODO: take care of import
	}

	/**
	 * Add a triple <subject, rdf:type, object> to the internal ontology:<br>
	 * -add the triple <br>
	 * -update the signature with involved individuals <br>
	 * -update the number of declared concept assertions
	 * 
	 * @param subject:
	 *            individual
	 * @param object:
	 *            owlClass
	 */
	private void addClassAssertion(Node subject, Node object) {
		/*
		 * add a class assertion.
		 */
		OWLClass owlClass = owlDataFactory.getOWLClass(IRI.create(object.toString()));

		int individualIndex = indexer.getIndexOfIndividualString(subject.toString());

		this.numberOfDeclairedConceptAssertions++;
		boolean hasNewElement = this.orarOntology.addConceptAssertion(individualIndex, owlClass);
		if (hasNewElement) {
			this.orarOntology.increaseNumberOfInputConceptAssertions(1);
			/*
			 * update in Signature is doned by adding all individuals in the
			 * IndividualIndexer
			 */
		}

	}

	/**
	 * Add a triple <subject, predicate, object> to the internal ontology:<br>
	 * -add the triple <br>
	 * -update the signature with involved individuals<br>
	 * -update the number of declared role assertions
	 * 
	 * @param subject
	 * @param predicate
	 * @param object
	 */
	private void addObjectPropertyAssertion(Node subject, Node predicate, Node object) {

		int subjectIndex = indexer.getIndexOfIndividualString(subject.toString());

		int objectIndex = indexer.getIndexOfIndividualString(object.toString());

		OWLObjectProperty property = owlDataFactory.getOWLObjectProperty(IRI.create(predicate.toString()));

		this.numberOfDeclariedRoleAssertions++;
		boolean hasNewElement = this.orarOntology.addRoleAssertion(subjectIndex, property, objectIndex);
		if (hasNewElement) {
			this.orarOntology.increaseNumberOfInputRoleAssertions(1);
			/*
			 * Update in Signature is done all at one by getting from the
			 * IndividualIndexer
			 */

		}
	}

	// private void printTriple(Node subject, Node predicate, Node object) {
	//
	// logger.info("Triples #" + countDeclairedAssertions);
	//
	// logger.info(subject);
	// logger.info(predicate);
	// logger.info(object);
	//
	// }

	private boolean isClassAssertion(Node subject, Node predicate, Node object) {

		boolean isClassAssertion = predicate.toString().equals(RDF_TYPE);
		boolean isPredefinedClass = this.definedConceptStrings.contains(object.toString());
		return (isClassAssertion && isPredefinedClass);
	}

	private boolean isObjectPropertyAssertion(Node subject, Node predicate, Node object) {

		boolean isData = subject.isLiteral() || object.isLiteral();
		/*
		 * We need to check subject and object because some ontology is not so
		 * "cleaned". Same name could be used for both object and data
		 * proroperty
		 */
		return (!isData && this.definedRoleStrings.contains(predicate.toString()));
	}

	/**
	 * @param triple
	 * @return true if this triple is a declaration one, e.g. to declare
	 *         ontology, property, class, individual.
	 */
	private boolean isDeclarationTriple(Node subject, Node predicate, Node object) {

		if (predicate.toString().equals(RDF_TYPE)) {
			if (object.toString().equals(OWL_CLASS) || object.toString().equals(OWL_NAMEDINDIVIDUAL)
					|| object.toString().equals(OWL_OBJECT_PROPERTY) || object.toString().equals(OWL_DATA_PROPERTY)
					|| object.toString().equals(OWL_ONTOLOGY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param triple
	 * @return true if this triple is a triple to import other ontologies.
	 */
	private boolean isImportTriple(Node subject, Node predicate, Node object) {
		if (predicate.toString().equals(OWL_IMPORTS)) {
			return true;
		}
		return false;
	}

	@Override
	public void finish() {
		if (this.config.getDebuglevels().contains(DebugLevel.STREAM_PARSING)) {

			logger.info("Number of individuals indexed (so far):" + indexer.getSize());

			logger.info("===Begin:Statistic for this file/resource.===");
			logger.info("Number of parsed triples: " + numberOfAllTriples);

			logger.info("Number of declaried concept assertions (so far): " + this.numberOfDeclairedConceptAssertions);
			logger.info("Number of declaried role assertions (so far): " + this.numberOfDeclariedRoleAssertions);
			int numberOfDeclariedAssertions = this.numberOfDeclairedConceptAssertions
					+ this.numberOfDeclariedRoleAssertions;
			logger.info("Number of declaired concept and role assertions: " + numberOfDeclariedAssertions);
			logger.info("===End:Statistic for this file/resource.===");
		}
		/*
		 * reset counter
		 */
		this.numberOfDeclairedConceptAssertions = 0;
		this.numberOfDeclariedRoleAssertions = 0;
		this.numberOfAllTriples = 0;
	}

}
