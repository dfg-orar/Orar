package orar.dlfragmentvalidator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ValidatorDataFactory {

	private final String IRI_CLASS_BASE = "http://www.arom#ProfilingC";
	private final String IRI_PROPERTY_BASE = "http://www.arom#ProfilingP";
	private final String IRI_INDIVIDUAL_BASE = "http://www.arom#ProfilingInd";
	private final Set<OWLClass> newClasses;
	private final Set<OWLObjectProperty> newProperties;
	private long count;
	private OWLDataFactory owlDataFactory;
	/*
	 * used to replace all anonymous individuals by fresh named individuals
	 */
	private final Map<OWLAnonymousIndividual, OWLNamedIndividual> anonymous2NamedIndividualMap;
	private static ValidatorDataFactory instance;

	private ValidatorDataFactory() {
		count = 0;
		owlDataFactory = OWLManager.getOWLDataFactory();
		newClasses = new HashSet<OWLClass>();
		newProperties = new HashSet<OWLObjectProperty>();
		anonymous2NamedIndividualMap = new HashMap<OWLAnonymousIndividual, OWLNamedIndividual>();

	}

	public static ValidatorDataFactory getInstance() {
		if (instance == null) {
			instance = new ValidatorDataFactory();

		}
		return instance;
	}

	public OWLNamedIndividual getNamedIndividual(OWLIndividual individual) {

		if (individual instanceof OWLNamedIndividual) {
			return (OWLNamedIndividual) individual;
		}
		/*
		 * else: it is a anonymous individual
		 */
		OWLAnonymousIndividual anonymousInd = (OWLAnonymousIndividual) individual;

		OWLNamedIndividual namedIndividual = this.anonymous2NamedIndividualMap
				.get(anonymousInd);
		if (namedIndividual == null) {
			namedIndividual = getFreshNamedIndividual();
			this.anonymous2NamedIndividualMap
					.put(anonymousInd, namedIndividual);
		}
		return namedIndividual;

	}

	public OWLNamedIndividual getFreshNamedIndividual() {
		count++;
		String name = IRI_INDIVIDUAL_BASE + count;
		OWLNamedIndividual freshInd = owlDataFactory.getOWLNamedIndividual(IRI
				.create(name));
		return freshInd;
	}

	public OWLObjectProperty getFreshProperty() {

		count++;
		String name = IRI_PROPERTY_BASE + count;
		OWLObjectProperty freshP = owlDataFactory.getOWLObjectProperty(IRI
				.create(name));
		newProperties.add(freshP);
		return freshP;
	}

	public OWLClass getFreshOWLClass() {
		count++;
		String name = IRI_CLASS_BASE + count;
		OWLClass freshClass = owlDataFactory.getOWLClass(IRI.create(name));
		newClasses.add(freshClass);
		return freshClass;
	}

	public void clear() {
		newClasses.clear();
		newProperties.clear();
		anonymous2NamedIndividualMap.clear();
		count = 0;
	}

	public Set<OWLNamedIndividual> getNamedIndividualGeneratedDuringValidation() {

		return new HashSet<OWLNamedIndividual>(this.anonymous2NamedIndividualMap.values());
	}
}
