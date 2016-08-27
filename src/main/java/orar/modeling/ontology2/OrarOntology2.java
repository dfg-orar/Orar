package orar.modeling.ontology2;

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
import orar.modeling.conceptassertion2.ConceptAssertionBox2;
import orar.modeling.roleassertion2.RoleAssertionBox2;
import orar.modeling.sameas2.SameAsBox2;

/**
 * Internal representation of an ontology.
 * 
 * @author kien
 * 
 * 
 */
public interface OrarOntology2 {

	/*
	 * Signature:getters
	 */
	public Set<Integer> getIndividualsInSignature();

	public Set<OWLClass> getConceptNamesInSignature();

	public Set<OWLObjectProperty> getRoleNamesInSignature();

	/*
	 * Signagure:setters/adders
	 */
	public void addIndividualToSignature(int individual);

	public void addIndividualsToSignature(Set<Integer> individuals);

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
	public Map<OWLClass, Set<OWLNamedIndividual>> getOWLAPIConcepAssertionMapWITHOUTNormalizationSymbols();
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
	public boolean addConceptAssertion(int individual, OWLClass concept);

	public boolean addManyConceptAssertions(int originalInd, Set<OWLClass> concepts);

	/**
	 * Adding role assertions.
	 * 
	 * @param subject
	 * @param role
	 * @param object
	 * @return true if new assertion has been added, false otherwise.
	 */
	public boolean addRoleAssertion(int subject, OWLObjectProperty role, int object);
	
//	public boolean addManyRoleAssertions(int subject, OWLObjectProperty role, Set<Integer> objects);

	/*
	 * Methods for sameas assertions
	 */
	public boolean addSameAsAssertion(int individual, int equalIndividual);

	public boolean addManySameAsAssertions(int individual, Set<Integer> equalIndividuals);

	/**
	 * @param individual
	 * @return a (possible empty) set of individuals that are equal to
	 *         {@code individual}
	 */
	public Set<Integer> getSameIndividuals(int individual);

	/**
	 * @param individual
	 * @return a (possibly empty) set of asserted concepts for the given
	 *         individual
	 */
	public Set<OWLClass> getAssertedConcepts(int individual);

	/**
	 * @param individual
	 * @return get successor assertions (stored in a map: role --> set of
	 *         objects) of the given individual
	 */
	public Map<OWLObjectProperty, Set<Integer>> getSuccessorRoleAssertionsAsMap(int subjectIndividual);

	/**
	 * @param individual
	 * @return get predecessor assertions (stored in a map: role --> set of
	 *         objects) of the given individual
	 */
	public Map<OWLObjectProperty, Set<Integer>> getPredecessorRoleAssertionsAsMap(int objectIndividual);

	public Set<Integer> getPredecessors(int object, OWLObjectProperty role);

	/**
	 * @param object
	 * @param role
	 * @return get a copy of all Predecessors
	 */
	public Set<Integer> getPredecessorsTakingEqualityIntoAccount(int object, OWLObjectProperty role);

	public Set<Integer> getSuccessors(int subject, OWLObjectProperty role);

	/**
	 * @param subject
	 * @param role
	 * @return get a copy of all Successors
	 */
	public Set<Integer> getSuccessorsTakingEqualityIntoAccount(int subject, OWLObjectProperty role);

	
	/**
	 * @return entailed sameas assertion as a map. Note that this include (a
	 *         equivalent a) for every individuals a. And note that in the
	 *         datastructure we store only sameas assertions in which a has a
	 *         really different equivalent individual, e.g. b.
	 */
	public Map<Integer, Set<Integer>> getEntailedSameasAssertions();

	public Set<OWLAxiom> getOWLAPISameasAssertions();

	/**
	 * @param role
	 * @return a copy of subjects occurring in role assertions of the role
	 *         {@code role}
	 */
	public Set<Integer> getSubjectsInRoleAssertions(OWLObjectProperty role);

	/**
	 * @param role
	 * @return a copy of objects occurring in role assertions of the role
	 *         {@code role}
	 */
	public Set<Integer> getObjectsInRoleAssertions(OWLObjectProperty role);

	public boolean addSameasAssertion(Set<Integer> setOfSameasIndividuals);

	public void increaseNumberOfInputConceptAssertions(int addedNumber);

	public void increaseNumberOfInputRoleAssertions(int addedNumber);
	public ConceptAssertionBox2 getConceptAssertionBox();
	public RoleAssertionBox2 getRoleAssertionBox();
	public SameAsBox2 getSameasBox();
}
