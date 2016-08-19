package orar.modeling.ontology;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.dlfragmentvalidator.DLConstructor;
import orar.dlfragmentvalidator.DLFragment;
import orar.modeling.sameas.SameAsBox;

/**
 * Internal representation of an ontology.
 * 
 * @author kien
 * 
 * 
 */
public interface OrarOntology {

	/*
	 * Signature:getters
	 */
	public Set<OWLNamedIndividual> getIndividualsInSignature();

	public Set<OWLClass> getConceptNamesInSignature();

	public Set<OWLObjectProperty> getRoleNamesInSignature();

	/*
	 * Signagure:setters/adders
	 */
	public void addIndividualToSignature(OWLNamedIndividual individual);

	public void addIndividualsToSignature(Set<OWLNamedIndividual> individuals);

	public void addConceptNameToSignature(OWLClass atomicClass);

	public void addConceptNamesToSignature(Set<OWLClass> conceptNames);

	public void addRoleNameToSignature(OWLObjectProperty atomicRole);

	public void addRoleNamesToSignature(Set<OWLObjectProperty> atomicRoles);

	/*
	 * Number of assertions when the ontology was first created: getters
	 */
	public int getNumberOfInputConceptAssertions();

	public int getNumberOfConceptAssertions();
	
	public int getNumberOfEqualityAssertions();

	public int getNumberOfInputRoleAssertions();

	public int getNumberOfRoleAssertions();
	
	public Set<OWLAxiom> getOWLAPIMaterializedAssertions();

	/*
	 * Number of assertions when the ontology was first created: setters
	 */
	public void setNumberOfInputRoleAssertions(int numberOfInputRoleAssertions);

	public void setNumberOfInputConceptAssertions(int numberOfInputConceptAssertions);

	/*
	 * Methods for testing correctness
	 */
	/**
	 * Get all OWLAPI concept assertions, <b> INCLUDING </b> those for
	 * individuals/concepts generated during NORMALIZATION and DL-PROFILE
	 * Extraction.<br>
	 * This method is not efficient and only used for checking
	 * correctness/comparing results with other tools via OWLAPI.
	 * 
	 * @return a set of all OWLAPI concept assertions of the ontology. Note that
	 *         this include those for concepts/individuals generated during the
	 *         normalization and profile validation phase.
	 */
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWithNormalizationSymbols();

	/**
	 * Get all OWLAPI concept assertions, <b> EXCLUDING </b> those for
	 * individuals/concepts generated during NORMALIZATION and DL-PROFILE
	 * Extraction.<br>
	 * This method is not efficient and only used for checking
	 * correctness/comparing results with other tools via OWLAPI.
	 * 
	 * @return a set of OWLAPI concept assertions (taking sameas into account)
	 *         after removing those of concepts/individuals generated during
	 *         normalization and profile validation phase.
	 */
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWHITOUTNormalizationSymbols();

	/**
	 * Get all OWLAPI role assertions, <b> INCLUDING </b> those for
	 * individuals/concepts generated during NORMALIZATION and DL-PROFILE
	 * Extraction.<br>
	 * This method is not efficient and only used for checking
	 * correctness/comparing results with other tools via OWLAPI.
	 * 
	 * @return a set of all OWLAPI role assertions of the ontology.
	 */
	public Set<OWLObjectPropertyAssertionAxiom> getOWLAPIRoleAssertionsWithNormalizationSymbols();

	/**
	 * Get all OWLAPI role assertions, <b> EXCLUDING </b> those for
	 * individuals/concepts generated during NORMALIZATION and DL-PROFILE
	 * Extraction.<br>
	 * This method is not efficient and only used for checking
	 * correctness/comparing results with other tools via OWLAPI.
	 * 
	 * @return a set of OWLAPI concept assertions after removing those of
	 *         concepts/individuals generated during normalization and profile
	 *         validation phase.
	 */
	public Set<OWLObjectPropertyAssertionAxiom> getOWLAPIRoleAssertionsWITHOUTNormalizationSymbols();

	/*
	 * Methods for DL fragments
	 */
	/**
	 * @return set target DL Fragment of this ontology. A Target DL Fragment is
	 *         the DL for which algorithms guarantee soundness and completeness.
	 */
	public void setTargetDLFragment(DLFragment targetDLFragment);

	/**
	 * @return get target DL Fragment of this ontology. A Target DL Fragment is
	 *         the DL for which algorithms guarantee soundness and completeness.
	 */
	public DLFragment getTargetDLFragment();

	/**
	 * @return get constructors really occurring in this ontology. This will be
	 *         used to design suitable optimization, e.g. optimizations for
	 *         ontology without nominals will be different from the ones with
	 *         nominals.
	 */
	public Set<DLConstructor> getActualDLConstructors();

	public void setActualDLConstructors(Set<DLConstructor> constructors);

	/*
	 * TBox
	 */
	/**
	 * @return a set of OWLAPI TBox axioms, including role-axioms
	 */
	public Set<OWLAxiom> getTBoxAxioms();

	public void addTBoxAxioms(Set<OWLAxiom> tboxAxioms);

	public void addTBoxAxiom(OWLAxiom tboxAxiom);

	/*
	 * Adding concept assertions
	 */
	public boolean addConceptAssertion(OWLNamedIndividual individual, OWLClass concept);

	public boolean addManyConceptAssertions(OWLNamedIndividual originalInd, Set<OWLClass> concepts);

	/**
	 * Adding role assertions.
	 * 
	 * @param subject
	 * @param role
	 * @param object
	 * @return true if new assertion has been added, false otherwise.
	 */
	public boolean addRoleAssertion(OWLNamedIndividual subject, OWLObjectProperty role, OWLNamedIndividual object);

	/*
	 * Methods for sameas assertions
	 */
	public boolean addSameAsAssertion(OWLNamedIndividual individual, OWLNamedIndividual equalIndividual);

	public boolean addManySameAsAssertions(OWLNamedIndividual individual, Set<OWLNamedIndividual> equalIndividuals);

	/**
	 * @param individual
	 * @return a (possible empty) set of individuals that are equal to
	 *         {@code individual}
	 */
	public Set<OWLNamedIndividual> getSameIndividuals(OWLNamedIndividual individual);

	/**
	 * @param individual
	 * @return a (possibly empty) set of asserted concepts for the given
	 *         individual
	 */
	public Set<OWLClass> getAssertedConcepts(OWLNamedIndividual individual);

	/**
	 * @param individual
	 * @return get successor assertions (stored in a map: role --> set of
	 *         objects) of the given individual
	 */
	public Map<OWLObjectProperty, Set<OWLNamedIndividual>> getSuccessorRoleAssertionsAsMap(
			OWLNamedIndividual subjectIndividual);

	/**
	 * @param individual
	 * @return get predecessor assertions (stored in a map: role --> set of
	 *         objects) of the given individual
	 */
	public Map<OWLObjectProperty, Set<OWLNamedIndividual>> getPredecessorRoleAssertionsAsMap(
			OWLNamedIndividual objectIndividual);

	public Set<OWLNamedIndividual> getPredecessors(OWLNamedIndividual object, OWLObjectProperty role);

	/**
	 * @param object
	 * @param role
	 * @return get a copy of all Predecessors
	 */
	public Set<OWLNamedIndividual> getPredecessorsTakingEqualityIntoAccount(OWLNamedIndividual object,
			OWLObjectProperty role);

	public Set<OWLNamedIndividual> getSuccessors(OWLNamedIndividual subject, OWLObjectProperty role);

	/**
	 * @param subject
	 * @param role
	 * @return get a copy of all Successors
	 */
	public Set<OWLNamedIndividual> getSuccessorsTakingEqualityIntoAccount(OWLNamedIndividual subject,
			OWLObjectProperty role);

	/*
	 * others
	 */
	public SameAsBox getSameasBox();

	/**
	 * @return entailed sameas assertion as a map. Note that this include (a
	 *         equivalent a) for every individuals a. And note that in the
	 *         datastructure we store only sameas assertions in which a has a
	 *         really different equivalent individual, e.g. b.
	 */
	public Map<OWLNamedIndividual, Set<OWLNamedIndividual>> getEntailedSameasAssertions();
	
	public Set<OWLAxiom> getOWLAPISameasAssertions();

	/**
	 * @param role
	 * @return a copy of subjects occurring in role assertions of the role
	 *         {@code role}
	 */
	public Set<OWLNamedIndividual> getSubjectsInRoleAssertions(OWLObjectProperty role);

	/**
	 * @param role
	 * @return a copy of objects occurring in role assertions of the role
	 *         {@code role}
	 */
	public Set<OWLNamedIndividual> getObjectsInRoleAssertions(OWLObjectProperty role);

	public boolean addSameasAssertion(Set<OWLNamedIndividual> setOfSameasIndividuals);
	public void increaseNumberOfInputConceptAssertions(int addedNumber);
	public void increaseNumberOfInputRoleAssertions(int addedNumber);
}
