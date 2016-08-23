package orar.modeling.ontology2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import orar.indexing.IndividualIndexer;

/**
 * Get OWLAPI assertions from indexes of individuals.
 * 
 * @author kien
 *
 */
public class AssertionDecoder {
	private static IndividualIndexer indexer = IndividualIndexer.getInstance();
	private static OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();

	//	private static Logger logger =Logger.getLogger(AssertionDecoder.class);
	/**
	 * @param owlClass
	 * @param individualLong
	 * @return OWLAPI concept assertion of the individual whose index number is {@code individualLong}
	 */
	public static OWLClassAssertionAxiom getOWLAPIConceptAssertoin(OWLClass owlClass, Integer individualLong) {
		String individualString = indexer.getIndividualString(individualLong);
//		logger.info("index= "+individualLong);
//		logger.info("individualString= "+individualString);
//		PrintingHelper.printMap(indexer.viewMapIndividuslString2Integer());
//		PrintingHelper.printMap(indexer.viewMapIndividuslString2Integer());
		OWLNamedIndividual owlapiIndividual = owlDataFactory.getOWLNamedIndividual(IRI.create(individualString));
		OWLClassAssertionAxiom classAssertion = owlDataFactory.getOWLClassAssertionAxiom(owlClass, owlapiIndividual);
		return classAssertion;
	}

	/**
	 * @param owlapiRole
	 * @param subjectLong
	 * @param objectLong
	 * @return OWLAPI role assertion of the subject whose index number is {@code subjectLong} and object whose index number is {@code objectLong}
	 */
	public static OWLObjectPropertyAssertionAxiom getOWLAPIRoleAssertion(OWLObjectProperty owlapiRole, Integer subjectLong,
			Integer objectLong) {
		String owlapiSubjectString = indexer.getIndividualString(subjectLong);
		OWLNamedIndividual owlapiSubject = owlDataFactory.getOWLNamedIndividual(IRI.create(owlapiSubjectString));
		String owlapiObjectString = indexer.getIndividualString(objectLong);
		OWLNamedIndividual owlapiObject = owlDataFactory.getOWLNamedIndividual(IRI.create(owlapiObjectString));
		OWLObjectPropertyAssertionAxiom assertion = owlDataFactory.getOWLObjectPropertyAssertionAxiom(owlapiRole,
				owlapiSubject, owlapiObject);
		return assertion;
	}

	/**
	 * @param individualsInLong
	 * @return OWLAPI sameas assertions of individuals whose index numbers are {@code individualsInLong}
	 */
	public static OWLSameIndividualAxiom getOWLAPISameasAssertion(Set<Integer> individualsInLong) {
		Set<OWLNamedIndividual> allOWLAPIIndividuals = new HashSet<>();
		for (Integer ind : individualsInLong) {
			String indString = indexer.getIndividualString(ind);
			OWLNamedIndividual owlapiInd = owlDataFactory.getOWLNamedIndividual(IRI.create(indString));
			allOWLAPIIndividuals.add(owlapiInd);
		}
		return owlDataFactory.getOWLSameIndividualAxiom(allOWLAPIIndividuals);
	}

	public static Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getSameasMapInOWLAPI(Map<Integer, Set<Integer>> encodedSameasMap){
		HashMap<OWLNamedIndividual, Set<OWLNamedIndividual>> sameasMap = new HashMap<>();
		Iterator<Entry<Integer, Set<Integer>>> iterator = encodedSameasMap.entrySet().iterator();
		while (iterator.hasNext()){
			Entry<Integer, Set<Integer>> entry = iterator.next();
			Integer key = entry.getKey();
			
			OWLNamedIndividual keyOWLIndividual = indexer.getOWLIndividual(key);
			Set<Integer> values = entry.getValue();
			Set<OWLNamedIndividual> valueOWLIndividuals = indexer.getOWLIndividuals(values);
			sameasMap.put(keyOWLIndividual, valueOWLIndividuals);
		}
		return sameasMap;
	}
}
