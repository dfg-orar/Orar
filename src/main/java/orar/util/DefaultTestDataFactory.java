package orar.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

public class DefaultTestDataFactory {
	private String IRIString = "http://www.test.org/af#";
	private String abstractIRI = "http://www.af#abstract";
	private String normIRIC = "http://www.af#NormTBoxC";
	private OWLOntologyManager owlManager;
	private OWLDataFactory dataFactory;
	private static DefaultTestDataFactory instance;

	private DefaultTestDataFactory() {
		owlManager = OWLManager.createOWLOntologyManager();
		dataFactory = owlManager.getOWLDataFactory();

	}

	public static DefaultTestDataFactory getInsatnce() {
		if (instance == null) {
			instance = new DefaultTestDataFactory();
		}
		return instance;
	}

	public void setIRI(String iri) {
		this.IRIString = iri;
	}

	/**
	 * @param name
	 * @return OWLClass with IRI string: IRI#name. The default IRI
	 *         =http://www.test.org/af#
	 */
	public OWLClass getConcept(String name) {
		return dataFactory.getOWLClass(IRI.create(IRIString + name));
	}

	/**
	 * @param fullIRIName
	 * @return OWLClass with the given full IRI String
	 */
	public OWLClass getConceptWithFullname(String fullIRIName) {
		return dataFactory.getOWLClass(IRI.create(fullIRIName));
	}

	public Set<OWLClassAssertionAxiom> getSetOfConceptAssertions(OWLClassAssertionAxiom... conceptAssertions) {
		HashSet<OWLClassAssertionAxiom> setOfCA = new HashSet<OWLClassAssertionAxiom>();
		for (OWLClassAssertionAxiom axiom : conceptAssertions) {
			setOfCA.add(axiom);
		}
		return setOfCA;
	}

	public Set<OWLObjectPropertyAssertionAxiom> getSetOfRoleAssertions(
			OWLObjectPropertyAssertionAxiom... roleAssertions) {
		HashSet<OWLObjectPropertyAssertionAxiom> setOfCA = new HashSet<OWLObjectPropertyAssertionAxiom>();
		for (OWLObjectPropertyAssertionAxiom axiom : roleAssertions) {
			setOfCA.add(axiom);
		}
		return setOfCA;
	}

	/**
	 * @param indName
	 * @return OWLNamedIndividual with IRI string: IRI#name. The default IRI
	 *         =http://www.test.org/af#
	 */
	public OWLNamedIndividual getIndividual(String indName) {

		return dataFactory.getOWLNamedIndividual(IRI.create(IRIString + indName));
	}

	/**
	 * @param propertyName
	 * @return OWLObjectProperty with IRI string: IRI#name. The default IRI
	 *         =http://www.test.org/af#
	 */
	public OWLObjectProperty getRole(String propertyName) {
		return dataFactory.getOWLObjectProperty(IRI.create(IRIString + propertyName));
	}

	public OWLClassAssertionAxiom getConceptAssertion(String conceptName, String indName) {
		OWLClass aClass = getConcept(conceptName);
		OWLNamedIndividual anInd = getIndividual(indName);
		return dataFactory.getOWLClassAssertionAxiom(aClass, anInd);
	}

	public OWLClassAssertionAxiom getConceptAssertion(OWLClass conceptName, OWLNamedIndividual indName) {

		return dataFactory.getOWLClassAssertionAxiom(conceptName, indName);
	}

	public OWLObjectPropertyAssertionAxiom getRoleAssertion(OWLNamedIndividual subject, OWLObjectProperty property,
			OWLNamedIndividual object) {

		return dataFactory.getOWLObjectPropertyAssertionAxiom(property, subject, object);
	}

	public OWLObjectPropertyAssertionAxiom getRoleAssertion(String subject, String property, String object) {

		OWLNamedIndividual subjectInd = getIndividual(subject);
		OWLNamedIndividual objectInd = getIndividual(object);
		OWLObjectProperty objectProperty = getRole(property);
		return dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectInd, objectInd);
	}

	public OWLObjectIntersectionOf getConjunctionOfConcepts(String concept1, String concept2) {
		OWLClass owlConcept1 = getConcept(concept1);
		OWLClass owlConcept2 = getConcept(concept2);
		Set<OWLClass> c12Set = new HashSet<OWLClass>();
		c12Set.add(owlConcept1);
		c12Set.add(owlConcept2);
		return dataFactory.getOWLObjectIntersectionOf(c12Set);
	}

	public OWLObjectIntersectionOf getConjunctionOfConcepts(String... concepts) {
		Set<OWLClass> setOfConjuncts = new HashSet<OWLClass>();
		for (String concept : concepts) {
			OWLClass owlConcept = getConcept(concept);
			setOfConjuncts.add(owlConcept);
		}

		return dataFactory.getOWLObjectIntersectionOf(setOfConjuncts);
	}

	public OWLObjectIntersectionOf getConjunctionOfConcepts(OWLClass... concepts) {
		Set<OWLClass> setOfConjuncts = new HashSet<OWLClass>();
		for (OWLClass concept : concepts) {

			setOfConjuncts.add(concept);
		}

		return dataFactory.getOWLObjectIntersectionOf(setOfConjuncts);
	}

	public OWLObjectIntersectionOf getConjunctionOfConcepts(OWLClass owlClass1, OWLClass owlClass2) {

		Set<OWLClass> c12Set = new HashSet<OWLClass>();
		c12Set.add(owlClass1);
		c12Set.add(owlClass2);
		return dataFactory.getOWLObjectIntersectionOf(c12Set);
	}

	public OWLObjectUnionOf getDisjunctionOfConcepts(OWLClass owlClass1, OWLClass owlClass2) {

		Set<OWLClass> c12Set = new HashSet<OWLClass>();
		c12Set.add(owlClass1);
		c12Set.add(owlClass2);
		return dataFactory.getOWLObjectUnionOf(c12Set);
	}

	public OWLObjectUnionOf getDisjunctionOfConcepts(String owlClass1, String owlClass2) {

		Set<OWLClass> c12Set = new HashSet<OWLClass>();
		c12Set.add(getConcept(owlClass1));
		c12Set.add(getConcept(owlClass2));
		return dataFactory.getOWLObjectUnionOf(c12Set);
	}

	public Set<OWLClass> getSetOfConcepts(String... conceptNames) {
		Set<OWLClass> setOfConcepts = new HashSet<OWLClass>();
		for (String name : conceptNames) {
			OWLClass concept = getConcept(name);
			setOfConcepts.add(concept);
		}
		return setOfConcepts;
	}

	public Set<OWLClass> getSetOfConcepts(OWLClass... concepts) {
		Set<OWLClass> setOfConcepts = new HashSet<OWLClass>();
		for (OWLClass concept : concepts) {
			setOfConcepts.add(concept);
		}
		return setOfConcepts;
	}

	public Set<OWLNamedIndividual> getSetOfIndividuals(String... individualNames) {
		Set<OWLNamedIndividual> setOfIndividuals = new HashSet<OWLNamedIndividual>();
		for (String name : individualNames) {
			OWLNamedIndividual individual = getIndividual(name);
			setOfIndividuals.add(individual);
		}
		return setOfIndividuals;
	}

	public Set<OWLNamedIndividual> getSetOfIndividuals(OWLNamedIndividual... individuals) {
		Set<OWLNamedIndividual> setOfIndividuals = new HashSet<OWLNamedIndividual>();
		for (OWLNamedIndividual ind : individuals) {
			setOfIndividuals.add(ind);
		}
		return setOfIndividuals;
	}

	public Set<OWLObjectProperty> getSetOfRoles(String... roleNames) {
		Set<OWLObjectProperty> setOfRoles = new HashSet<OWLObjectProperty>();
		for (String name : roleNames) {
			OWLObjectProperty role = getRole(name);
			setOfRoles.add(role);
		}
		return setOfRoles;
	}

	public Set<OWLObjectProperty> getSetOfRoles(OWLObjectProperty... roles) {
		Set<OWLObjectProperty> setOfRoles = new HashSet<OWLObjectProperty>();
		for (OWLObjectProperty role : roles) {
			setOfRoles.add(role);
		}
		return setOfRoles;
	}

	public OWLClass getOWLThing() {
		return dataFactory.getOWLThing();
	}

	public OWLClass getOWLNothing() {
		return dataFactory.getOWLNothing();
	}

	public OWLNamedIndividual getAbstractIndividual(String name) {
		return dataFactory.getOWLNamedIndividual(IRI.create(abstractIRI + name));
	}

	public OWLClass getNormTBoxConcept(int i) {
		return dataFactory.getOWLClass(IRI.create(normIRIC + i));
	}

	public OWLObjectSomeValuesFrom getExistentialAxiom(String role, String concept) {
		OWLObjectProperty R = getRole(role);
		OWLClass A = getConcept(concept);
		return dataFactory.getOWLObjectSomeValuesFrom(R, A);
	}

	public OWLObjectAllValuesFrom getForAllAxiom(String role, String concept) {
		OWLObjectProperty R = getRole(role);
		OWLClass A = getConcept(concept);
		return dataFactory.getOWLObjectAllValuesFrom(R, A);
	}

	public OWLObjectAllValuesFrom getForAllAxiom(OWLObjectProperty role, OWLClass concept) {
		return dataFactory.getOWLObjectAllValuesFrom(role, concept);
	}

	public OWLObjectAllValuesFrom getForAllAxiom(OWLObjectProperty role, OWLClassExpression conceptExpression) {
		return dataFactory.getOWLObjectAllValuesFrom(role, conceptExpression);
	}

	// public IndividualType get_HornSHOIF_IndividualType(Set<String> concepts,
	// Set<String> preRoles, Set<String> sucRoles) {
	// Set<OWLClass> owlConcepts = new HashSet<>();
	// for (String concept : concepts) {
	// owlConcepts.add(getConcept(concept));
	// }
	//
	// Set<OWLObjectProperty> preOWLRoles = new HashSet<>();
	// for (String preRole : preRoles) {
	// preOWLRoles.add(getRole(preRole));
	// }
	//
	// Set<OWLObjectProperty> succOWLRoles = new HashSet<>();
	// for (String sucRole : sucRoles) {
	// succOWLRoles.add(getRole(sucRole));
	// }
	//
	// return new ALCHOIF_NaiveIndividualType(owlConcepts, preOWLRoles,
	// succOWLRoles);
	// }

	public Map<OWLObjectProperty, Set<OWLClass>> getMapFromRoleToConcepts(String role, String... concepts) {
		OWLObjectProperty owlRole = getRole(role);
		Set<OWLClass> owlConcepts = getSetOfConcepts(concepts);
		Map<OWLObjectProperty, Set<OWLClass>> map = new HashMap<OWLObjectProperty, Set<OWLClass>>();
		map.put(owlRole, owlConcepts);
		return map;
	}

	public OWLOntology getOntology() {
		OWLOntology owlOntology;
		try {
			owlOntology = owlManager.createOntology();
			return owlOntology;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void addAxiomsTo(OWLOntology ontology, OWLAxiom... axioms) {
		for (OWLAxiom axiom : axioms) {
			this.owlManager.addAxiom(ontology, axiom);
		}
	}

	public OWLSubObjectPropertyOfAxiom getSubRoleAxiom(String subRole, String superRole) {
		OWLObjectProperty owlSubRole = getRole(superRole);
		OWLObjectProperty owlSuperRole = getRole(superRole);
		return dataFactory.getOWLSubObjectPropertyOfAxiom(owlSubRole, owlSuperRole);
	}

	public OWLSubObjectPropertyOfAxiom getSubRoleAxiom(OWLObjectProperty subRole, OWLObjectProperty superRole) {

		return dataFactory.getOWLSubObjectPropertyOfAxiom(subRole, superRole);
	}

	public OWLSubClassOfAxiom getSubClassAxiom(OWLClassExpression subClass, OWLClassExpression superClass) {
		return dataFactory.getOWLSubClassOfAxiom(subClass, superClass);
	}

	public OWLTransitiveObjectPropertyAxiom getTransitivityAxiom(String roleName) {
		OWLObjectProperty role = getRole(roleName);
		return dataFactory.getOWLTransitiveObjectPropertyAxiom(role);
	}

	public OWLTransitiveObjectPropertyAxiom getTransitivityAxiom(OWLObjectProperty role) {
		return dataFactory.getOWLTransitiveObjectPropertyAxiom(role);
	}

	public Map<OWLObjectProperty, Set<OWLNamedIndividual>> getMapFromRole2Individuals(OWLObjectProperty role,
			Set<OWLNamedIndividual> individuals) {
		HashMap<OWLObjectProperty, Set<OWLNamedIndividual>> map = new HashMap<OWLObjectProperty, Set<OWLNamedIndividual>>();
		map.put(role, individuals);
		return map;
	}
}
