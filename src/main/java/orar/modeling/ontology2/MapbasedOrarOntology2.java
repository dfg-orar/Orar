package orar.modeling.ontology2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.data.NormalizationDataFactory;
import orar.dlfragmentvalidator.DLConstructor;
import orar.dlfragmentvalidator.DLFragment;
import orar.dlfragmentvalidator.ValidatorDataFactory;
import orar.indexing.IndividualIndexer;
import orar.modeling.conceptassertion2.ConceptAssertionBox2;
import orar.modeling.conceptassertion2.MapbasedConceptAssertionBox2;
import orar.modeling.roleassertion2.MapbasedRoleAssertionBox2;
import orar.modeling.roleassertion2.RoleAssertionBox2;
import orar.modeling.sameas2.MapbasedSameAsBox2;
import orar.modeling.sameas2.SameAsBox2;

public class MapbasedOrarOntology2 implements OrarOntology2 {
	private static final Logger logger = Logger.getLogger(MapbasedOrarOntology2.class);
	/*
	 * TBox axioms
	 */
	private final Set<OWLAxiom> tboxAxioms;

	/*
	 * Assertions
	 */
	private final ConceptAssertionBox2 conceptAssertionBox;
	private final RoleAssertionBox2 roleAssertionBox;
	private final SameAsBox2 sameasBox;

	/*
	 * Signature
	 */
	private final Set<Integer> individualsInSignature;
	private final Set<OWLClass> conceptNamesInSignature;
	private final Set<OWLObjectProperty> roleNamesInSignature;

	private Integer numberOfInputConceptAssertions = 0;
	private Integer numberOfInputRoleAssertions = 0;

	/*
	 * Individual Indexer
	 */
	private IndividualIndexer indexer;
	private OWLDataFactory owlDataFactory;
	/*
	 * Constructors
	 */
	/**
	 * constructors actually occurring in the ontology.
	 */
	private Set<DLConstructor> actualDLConstructors;
	/**
	 * the DL logic that the algorithm is designed for.
	 */
	private DLFragment targetDLFragment;

	public MapbasedOrarOntology2() {
		this.tboxAxioms = new HashSet<OWLAxiom>();
		this.conceptAssertionBox = new MapbasedConceptAssertionBox2();
		this.roleAssertionBox = new MapbasedRoleAssertionBox2();
		this.sameasBox = new MapbasedSameAsBox2();

		this.individualsInSignature = new HashSet<Integer>();
		this.conceptNamesInSignature = new HashSet<OWLClass>();
		this.roleNamesInSignature = new HashSet<OWLObjectProperty>();
		this.actualDLConstructors = new HashSet<DLConstructor>();

		this.indexer = IndividualIndexer.getInstance();
		this.owlDataFactory = OWLManager.getOWLDataFactory();

	}

	/*
	 * Methods
	 */
	@Override
	public Set<Integer> getIndividualsInSignature() {
		return this.individualsInSignature;
	}

	@Override
	public Set<OWLClass> getConceptNamesInSignature() {

		return this.conceptNamesInSignature;
	}

	@Override
	public Set<OWLObjectProperty> getRoleNamesInSignature() {

		return this.roleNamesInSignature;
	}

	@Override
	public Set<OWLAxiom> getTBoxAxioms() {

		return this.tboxAxioms;
	}

	@Override
	public void addIndividualToSignature(int individual) {
		this.individualsInSignature.add(individual);

	}

	@Override
	public void addConceptNameToSignature(OWLClass atomicClass) {
		this.conceptNamesInSignature.add(atomicClass);

	}

	@Override
	public void addRoleNameToSignature(OWLObjectProperty atomicRole) {
		this.roleNamesInSignature.add(atomicRole);

	}

	@Override
	public void addTBoxAxioms(Set<OWLAxiom> tboxAxioms) {
		this.tboxAxioms.addAll(tboxAxioms);

	}

	@Override
	public void addTBoxAxiom(OWLAxiom tboxAxiom) {
		this.tboxAxioms.add(tboxAxiom);

	}

	@Override
	public void addIndividualsToSignature(Set<Integer> individuals) {
		this.individualsInSignature.addAll(individuals);

	}

	@Override
	public void addConceptNamesToSignature(Set<OWLClass> conceptNames) {
		this.conceptNamesInSignature.addAll(conceptNames);

	}

	@Override
	public void addRoleNamesToSignature(Set<OWLObjectProperty> atomicRoles) {
		this.roleNamesInSignature.addAll(atomicRoles);

	}

	@Override
	public int getNumberOfInputConceptAssertions() {

		return this.numberOfInputConceptAssertions;
	}

	@Override
	public int getNumberOfInputRoleAssertions() {

		return this.numberOfInputRoleAssertions;
	}

	@Override
	public void setNumberOfInputRoleAssertions(int ra) {
		this.numberOfInputRoleAssertions = ra;

	}

	@Override
	public void setNumberOfInputConceptAssertions(int ca) {
		this.numberOfInputConceptAssertions = ca;

	}

	@Override
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWithNormalizationSymbols() {

		return this.conceptAssertionBox.getOWLAPIConceptAssertions();
	}

	@Override
	public Set<OWLClassAssertionAxiom> getOWLAPIConceptAssertionsWHITOUTNormalizationSymbols() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLClass thingConcept = OWLManager.getOWLDataFactory().getOWLThing();
		Set<Integer> allIndividuals = this.getIndividualsInSignature();
		Set<OWLClassAssertionAxiom> conceptAssertions = new HashSet<OWLClassAssertionAxiom>();
		for (Integer a : allIndividuals) {
			Set<Integer> sameasOfa = this.getSameIndividuals(a);
			sameasOfa.add(a);
			// logger.info(" ***********sameas a:"+sameasOfa);
			Set<OWLClass> assertedConcepts = new HashSet<OWLClass>();
			for (Integer eachInd : sameasOfa) {
				assertedConcepts.addAll(this.getAssertedConcepts(eachInd));
			}
			// logger.info(" ***********asserted concepts:"+assertedConcepts);
			for (OWLClass eachConcept : assertedConcepts) {
				boolean isNotIndividualByNormalization = !ValidatorDataFactory.getInstance()
						.getNamedIndividualGeneratedDuringValidation().contains(a);
				boolean isNotConceptByNormalization = !NormalizationDataFactory.getInstance()
						.getConceptsByNormalization().contains(eachConcept);
				boolean isNotThingConcept = !eachConcept.equals(thingConcept);
				if (isNotConceptByNormalization && isNotIndividualByNormalization && isNotThingConcept) {

					// OWLClassAssertionAxiom classAssertion =
					// owlDataFactory.getOWLClassAssertionAxiom(eachConcept, a);
					OWLClassAssertionAxiom classAssertion = getOWLAPIConceptAssertoin(eachConcept, a);
					conceptAssertions.add(classAssertion);
				}
			}

		}
		return conceptAssertions;
	}

	/**
	 * get OWLAPI concept assertion from OWLAPIClass and an index of an
	 * OWLNamedIndividual
	 * 
	 * @param owlClass
	 * @param individualLong
	 * @return corresponding OWLAPI concept assertion.
	 */
	private OWLClassAssertionAxiom getOWLAPIConceptAssertoin(OWLClass owlClass, Integer individualLong) {
		String individualString = indexer.getIndividualString(individualLong);
		OWLNamedIndividual owlapiIndividual = this.owlDataFactory.getOWLNamedIndividual(IRI.create(individualString));
		OWLClassAssertionAxiom classAssertion = this.owlDataFactory.getOWLClassAssertionAxiom(owlClass,
				owlapiIndividual);
		return classAssertion;
	}

	@Override
	public Set<OWLObjectPropertyAssertionAxiom> getOWLAPIRoleAssertionsWithNormalizationSymbols() {
		return this.roleAssertionBox.getOWLAPIRoleAssertions();
	}

	@Override
	public Set<OWLObjectPropertyAssertionAxiom> getOWLAPIRoleAssertionsWITHOUTNormalizationSymbols() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory owlDataFactory = manager.getOWLDataFactory();
		Set<Integer> allIndividuals = this.getIndividualsInSignature();
		Set<OWLObjectPropertyAssertionAxiom> resultingRoleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();

		for (Integer ind_a : allIndividuals) {
			Set<Integer> sameasOf_a = getSameIndividuals(ind_a);
			sameasOf_a.add(ind_a);
			for (Integer ind_b : sameasOf_a) {
				Map<OWLObjectProperty, Set<Integer>> succesorAssertionAsMap = this.roleAssertionBox
						.getSuccesorRoleAssertionsAsMap(ind_b);
				if (succesorAssertionAsMap == null) {
					continue;
				}

				Iterator<Entry<OWLObjectProperty, Set<Integer>>> iterator = succesorAssertionAsMap.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<OWLObjectProperty, Set<Integer>> entry = iterator.next();
					OWLObjectProperty role = entry.getKey();
					Set<Integer> objects_c = entry.getValue();
					for (Integer ind_c : objects_c) {
						Set<Integer> sameasOf_c = this.getSameIndividuals(ind_c);
						sameasOf_c.add(ind_c);
						for (Integer eachSameOfc : sameasOf_c) {
							// OWLObjectPropertyAssertionAxiom newAssertion =
							// owlDataFactory
							// .getOWLObjectPropertyAssertionAxiom(role, ind_a,
							// eachSameOfc);
							OWLObjectPropertyAssertionAxiom newAssertion = AssertionDecoder.getOWLAPIRoleAssertion(role,
									ind_a, eachSameOfc);

							resultingRoleAssertions.add(newAssertion);
						}
					}
				}
			}
		}
		return resultingRoleAssertions;
	}

	@Override
	public void setTargetDLFragment(DLFragment targetDLFragment) {
		this.targetDLFragment = targetDLFragment;
	}

	@Override
	public DLFragment getTargetDLFragment() {

		return this.targetDLFragment;
	}

	@Override
	public Set<DLConstructor> getActualDLConstructors() {

		return this.actualDLConstructors;
	}

	@Override
	public void setActualDLConstructors(Set<DLConstructor> constructorsOccuringInOntology) {
		this.actualDLConstructors = constructorsOccuringInOntology;

	}

	@Override
	public boolean addConceptAssertion(int individual, OWLClass concept) {
		return this.conceptAssertionBox.addConceptAssertion(individual, concept);

	}

	@Override
	public boolean addManyConceptAssertions(int individual, Set<OWLClass> concepts) {

		return this.conceptAssertionBox.addManyConceptAssertions(individual, concepts);
	}

	@Override
	public boolean addRoleAssertion(int subject, OWLObjectProperty role, int object) {
		return this.roleAssertionBox.addRoleAssertion(subject, role, object);

	}

	@Override
	public boolean addSameAsAssertion(int individual, int equalIndividual) {
		return this.sameasBox.addSameAsAssertion(individual, equalIndividual);

	}

	@Override
	public boolean addManySameAsAssertions(int individual, Set<Integer> manyEqualIndividuals) {
		return this.sameasBox.addManySameAsAssertions(individual, manyEqualIndividuals);

	}

	@Override
	public Set<Integer> getSameIndividuals(int individual) {
		Set<Integer> equalIndividuals = this.sameasBox.getSameIndividuals(individual);
		if (equalIndividuals == null) {
			equalIndividuals = new HashSet<Integer>();
		}
		return equalIndividuals;
	}

	@Override
	public Set<OWLClass> getAssertedConcepts(int individual) {
		return this.conceptAssertionBox.getAssertedConcepts(individual);
	}

	@Override
	public Map<OWLObjectProperty, Set<Integer>> getSuccessorRoleAssertionsAsMap(int individual) {
		return this.roleAssertionBox.getSuccesorRoleAssertionsAsMap(individual);
	}

	@Override
	public Map<OWLObjectProperty, Set<Integer>> getPredecessorRoleAssertionsAsMap(int objectIndividual) {
		return this.roleAssertionBox.getPredecessorRoleAssertionsAsMap(objectIndividual);
	}

	@Override
	public Set<Integer> getPredecessors(int object, OWLObjectProperty role) {
		Map<OWLObjectProperty, Set<Integer>> predecessorAssertionAsMap = this.roleAssertionBox
				.getPredecessorRoleAssertionsAsMap(object);
		Set<Integer> setOfPredecessors = predecessorAssertionAsMap.get(role);
		if (setOfPredecessors == null) {
			setOfPredecessors = new HashSet<Integer>();
		}
		return setOfPredecessors;
	}

	@Override
	public Set<Integer> getSuccessors(int subject, OWLObjectProperty role) {
		Map<OWLObjectProperty, Set<Integer>> successorAssertionAsMap = this.roleAssertionBox
				.getSuccesorRoleAssertionsAsMap(subject);
		Set<Integer> setOfSuccessors = successorAssertionAsMap.get(role);
		if (setOfSuccessors == null) {
			setOfSuccessors = new HashSet<Integer>();
		}
		return setOfSuccessors;
	}

	@Override
	public SameAsBox2 getSameasBox() {

		return this.sameasBox;
	}

	@Override
	public Set<Integer> getSubjectsInRoleAssertions(OWLObjectProperty role) {

		return this.roleAssertionBox.getSubjectsInRoleAssertions(role);
	}

	@Override
	public Set<Integer> getObjectsInRoleAssertions(OWLObjectProperty role) {

		return this.roleAssertionBox.getObjectsInRoleAssertions(role);
	}

	@Override
	public Set<Integer> getPredecessorsTakingEqualityIntoAccount(int object, OWLObjectProperty role) {
		Set<Integer> subjects = new HashSet<Integer>();
		Set<Integer> equivalentIndsOf_object = this.getSameIndividuals(object);
		equivalentIndsOf_object.add(object);
		for (Integer eachobject : equivalentIndsOf_object) {
			subjects.addAll(getPredecessors(eachobject, role));
		}
		return subjects;
	}

	@Override
	public Set<Integer> getSuccessorsTakingEqualityIntoAccount(int subject, OWLObjectProperty role) {
		Set<Integer> objects = new HashSet<Integer>();
		Set<Integer> equivalentIndsOf_subject = getSameIndividuals(subject);
		equivalentIndsOf_subject.add(subject);
		for (Integer eachSubject : equivalentIndsOf_subject) {
			objects.addAll(getSuccessors(eachSubject, role));
		}
		return objects;
	}

	@Override
	public boolean addSameasAssertion(Set<Integer> setOfSameasIndividuals) {

		return this.sameasBox.addSameasAssertions(setOfSameasIndividuals);
	}

	@Override
	public int getNumberOfConceptAssertions() {

		return this.conceptAssertionBox.getNumberOfConceptAssertions();
	}

	@Override
	public int getNumberOfRoleAssertions() {

		return this.roleAssertionBox.getNumberOfRoleAssertions();
	}

	@Override
	public Map<Integer, Set<Integer>> getEntailedSameasAssertions() {
		Set<Integer> allIndividuals = getIndividualsInSignature();
		// logger.info("***DEBUG*** all Individuals:"+allIndividuals);
		Map<Integer, Set<Integer>> allEntailedSameasMap = new HashMap<Integer, Set<Integer>>(
				getSameasBox().getSameasMap());
		for (Integer ind : allIndividuals) {
			Set<Integer> existingInds = allEntailedSameasMap.get(ind);
			if (existingInds == null) {
				existingInds = new HashSet<Integer>();
				existingInds.add(ind);
			}
			existingInds.add(ind);
			allEntailedSameasMap.put(ind, existingInds);
		}
		return allEntailedSameasMap;
	}

	@Override
	public void increaseNumberOfInputConceptAssertions(int addedNumber) {
		this.numberOfInputConceptAssertions += addedNumber;

	}

	@Override
	public void increaseNumberOfInputRoleAssertions(int addedNumber) {
		this.numberOfInputRoleAssertions += addedNumber;

	}

	@Override
	public int getNumberOfEqualityAssertions() {
		return this.getEntailedSameasAssertions().size();
	}

	@Override
	public Set<OWLAxiom> getOWLAPIMaterializedAssertions() {
		Set<OWLAxiom> materializedABox = new HashSet<>();
		materializedABox.addAll(this.getOWLAPIConceptAssertionsWHITOUTNormalizationSymbols());
		materializedABox.addAll(this.getOWLAPIRoleAssertionsWITHOUTNormalizationSymbols());
		materializedABox.addAll(this.getOWLAPISameasAssertions());
		return materializedABox;
	}

	@Override
	public Set<OWLAxiom> getOWLAPISameasAssertions() {
		return this.sameasBox.getEntailedSameasOWLAxioms();
	}

}
