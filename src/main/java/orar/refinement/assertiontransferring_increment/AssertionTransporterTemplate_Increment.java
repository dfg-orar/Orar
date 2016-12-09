package orar.refinement.assertiontransferring_increment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.metrics.NumberOfClassesWithMultipleInheritance;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.abstraction.PairOfSubjectAndObject;
import orar.abstraction.TypeComputor;
import orar.abstraction2.BasicTypeComputor_Increment;
import orar.config.Configuration;
import orar.config.DebugLevel;
import orar.config.LogInfo;
import orar.data.DataForTransferingEntailments;
import orar.data.DataForTransferingEntailments_Increment;
import orar.data.DataForTransferringEntailmentInterface;
import orar.indexing.IndividualIndexer;
import orar.modeling.conceptassertion2.ConceptAssertionBox2;
import orar.modeling.ontology2.OrarOntology2;
import orar.modeling.roleassertion2.IndexedRoleAssertionList;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.abstractroleassertion.RoleAssertionList;
import orar.refinement.assertiontransferring.AssertionTransporter;
import orar.type.BasicIndividualType;
import orar.type.IndividualType;
import orar.type.IndividualTypeFactory;
import orar.type.IndividualTypeFactory_UsingWeakHashMap;
import orar.util.PrintingHelper;

public abstract class AssertionTransporterTemplate_Increment implements AssertionTransporter {
	// original ontology
	protected final OrarOntology2 orarOntology;
	// entailments of the abstraction
	protected Map<OWLNamedIndividual, Set<OWLClass>> xAbstractConceptAssertionsAsMap;
	protected Map<OWLNamedIndividual, Set<OWLClass>> yAbstractConceptAssertionsAsMap;
	protected Map<OWLNamedIndividual, Set<OWLClass>> zAbstractConceptAssertionsAsMap;
	protected AbstractRoleAssertionBox abstractRoleAssertionBox;
	protected Map<OWLNamedIndividual, Set<OWLNamedIndividual>> abstractSameasMap;
	// flag for abox updating
	protected boolean isABoxExtended;
	protected boolean isABoxExtendedViaX;
	protected boolean isABoxExtendedViaY;
	protected boolean isABoxExtendedViaZ;
	protected boolean isABoxExtendedWithNewSpecialRoleAssertions;
	protected boolean isABoxExtendedWithNewSameasAssertions;
	// debugging
	protected final Configuration config;
	private static final Logger logger = Logger.getLogger(AssertionTransporterTemplate_Increment.class);
	// map/data for transferring assertions
	protected final DataForTransferringEntailmentInterface dataForTransferingEntailments;
	protected Map<OWLNamedIndividual, IndividualType> mapX2Type;
	protected Map<IndividualType, Set<Integer>> mapType2Individuals;
	protected ConceptAssertionBox2 conceptAssertionBox;
	// typecomputor for incrementally recompute type
	protected TypeComputor typeComputor;
	protected IndividualTypeFactory typeFactory;
	// output
	protected final IndexedRoleAssertionList newRoleAssertions;
	protected final Set<Set<Integer>> newSameasAssertions;
	protected final Set<Integer> individualsHavingNewTypes;

	public AssertionTransporterTemplate_Increment(OrarOntology2 orarOntoloy) {
		this.orarOntology = orarOntoloy;
		this.conceptAssertionBox = this.orarOntology.getConceptAssertionBox();
		this.typeComputor = new BasicTypeComputor_Increment(this.orarOntology);
		this.individualsHavingNewTypes = new HashSet<>();
		// this.abstractConceptAssertionsAsMap = new HashMap<>();
		// this.abstractRoleAssertionBox = new AbstractRoleAssertionBox();
		// this.abstractSameasMap = new HashMap<>();
		/*
		 * initialize flags of updating
		 */
		this.isABoxExtended = false;
		this.isABoxExtendedViaX = false;
		this.isABoxExtendedViaY = false;
		this.isABoxExtendedViaZ = false;
		this.isABoxExtendedWithNewSpecialRoleAssertions = false;
		this.isABoxExtendedWithNewSameasAssertions = false;
		/*
		 * others
		 */
		this.config = Configuration.getInstance();
		this.dataForTransferingEntailments = DataForTransferingEntailments_Increment.getInstance();
		this.newRoleAssertions = new IndexedRoleAssertionList();
		this.newSameasAssertions = new HashSet<Set<Integer>>();
		/*
		 * maps for transferring assertions
		 */
		mapX2Type = this.dataForTransferingEntailments.getMap_XAbstractIndiv_2_Type();
		mapType2Individuals = this.dataForTransferingEntailments.getMapType_2_Individuals();

	}

	@Override
	public void updateOriginalABox() {
		long startUpdateConceptAssertion = System.currentTimeMillis();

		transferConceptAssertions();// not change

		long endUpdateConceptAssertion = System.currentTimeMillis();
		if (this.config.getLogInfos().contains(LogInfo.TIME_STAMP_FOR_EACH_STEP)) {
			long updateingConceptAsesrtionTime = (endUpdateConceptAssertion - startUpdateConceptAssertion) / 1000;
			logger.info("Time for updating concep assertions in the original ABox in seconds: "
					+ updateingConceptAsesrtionTime);
		}

		transferRoleAssertions();// not change

		transferSameasAssertions();// varies
	}

	protected abstract void transferSameasAssertions();

	private void transferRoleAssertions() {
		transferRoleAssertionsForLoopConcepts();// not change
		tranferRoleAssertionsBetweenUX();// varies
		transferRoleAssertionsForXYHavingFunctionalRoles();// not change
		transferRoleAssertionsForZXHavingInverseFunctionalRoles();// not change
	}

	/**
	 * add role assertions by the rule R^2_<: M(a), F(a,b) --> H(a,b). Case: F
	 * is atomic.
	 */
	private void transferRoleAssertionsForZXHavingInverseFunctionalRoles() {
		RoleAssertionList roleAssertionList = this.abstractRoleAssertionBox.getZxRoleAssertionsForType();
		int size = roleAssertionList.getSize();
		for (int index = 0; index < size; index++) {
			/*
			 * get role(z,x)
			 */
			OWLNamedIndividual zAbstractIndiv = roleAssertionList.getSubject(index);
			OWLNamedIndividual xAbstractIndiv = roleAssertionList.getObject(index);
			OWLObjectProperty roleInEntailedAssertion = roleAssertionList.getRole(index);
			/*
			 * each pair of (z,x) is connected by the ONLY one role in the
			 * abstraction. We use this role and original individuals
			 * corresponding to x to find the correct pair of individuals for
			 * adding role assertions.
			 * 
			 */

			PairOfSubjectAndObject pairOfZX = new PairOfSubjectAndObject(zAbstractIndiv, xAbstractIndiv);
			OWLObjectProperty roleConnectsZandX = this.dataForTransferingEntailments.getMap_ZX_2_Role().get(pairOfZX);
			if (roleConnectsZandX == null)
				continue;
			/*
			 * add role assertions to the original ABox.
			 */
			Set<Integer> originalIndsCorrespondingToX = new HashSet<>(this.dataForTransferingEntailments
					.getOriginalIndividuals(xAbstractIndiv));
			for (Integer eachOriginalIndiv : originalIndsCorrespondingToX) {
				Set<Integer> allSubjects = this.orarOntology.getPredecessors(eachOriginalIndiv, roleConnectsZandX);
				for (Integer eachSubject : allSubjects) {
					
					IndividualType oldTypeOfSubject = this.typeComputor.computeType(eachSubject);
					Set<Integer> odlSetOfSubjectIndividuals = this.mapType2Individuals.get(oldTypeOfSubject);
					
					IndividualType oldTypeOfObject = this.typeComputor.computeType(eachOriginalIndiv);
					Set<Integer> odlSetOfObjectIndividuals = this.mapType2Individuals.get(oldTypeOfObject);
					
					if (this.orarOntology.addRoleAssertion(eachSubject, roleInEntailedAssertion, eachOriginalIndiv)) {
						this.isABoxExtended = true;

						removeIndividuslFromOldMapType2Individuals(oldTypeOfSubject,odlSetOfSubjectIndividuals,eachSubject);
						removeIndividuslFromOldMapType2Individuals(oldTypeOfObject,odlSetOfObjectIndividuals,eachOriginalIndiv);
						
						
						this.typeComputor.computeTypeIncrementally(eachSubject);
						this.typeComputor.computeTypeIncrementally(eachOriginalIndiv);
						
						this.isABoxExtendedWithNewSpecialRoleAssertions = true;
						this.newRoleAssertions.addRoleAssertion(eachSubject, roleInEntailedAssertion,
								eachOriginalIndiv);
					}
				}
			}
		}

	}

	protected void removeIndividuslFromOldMapType2Individuals(IndividualType oldTYpe,Set<Integer> currentIndividuals, Integer individualToBeRemoved){
		if (currentIndividuals==null){
			logger.info("Indiv:"+individualToBeRemoved);
			logger.info("type:"+oldTYpe);
			logger.info("current map Type-->Individuals:");
			PrintingHelper.printMap(this.mapType2Individuals);
			return;
		}
		currentIndividuals.remove(individualToBeRemoved);
		if (currentIndividuals.isEmpty()){
			this.mapType2Individuals.remove(oldTYpe);
		}
	}
	/**
	 * add role assertions by the rule R^2_<: M(a), F(a,b) --> H(a,b). Case: F
	 * is the inverse of an atomic role.
	 */
	private void transferRoleAssertionsForXYHavingFunctionalRoles() {
		RoleAssertionList roleAssertionList = this.abstractRoleAssertionBox.getXyRoleAssertionsForType();
		int size = roleAssertionList.getSize();
		for (int index = 0; index < size; index++) {
			/*
			 * get role(x,y)
			 */
			OWLNamedIndividual xAbstractIndiv = roleAssertionList.getSubject(index);
			OWLObjectProperty roleInEntailedAssertion = roleAssertionList.getRole(index);
			OWLNamedIndividual yAbstractIndiv = roleAssertionList.getObject(index);
			/*
			 * each pair of (x,y) is connected by the ONLY one role in the
			 * abstraction. We use this role and original individuals
			 * corresponding to x to find the correct pair of individuals for
			 * adding role assertions.
			 * 
			 */
			if(Configuration.getInstance().getDebuglevels().contains(DebugLevel.TRANSFER_ROLEASSERTION)){
				logger.info("considerd abstract role assertion:");
				logger.info(xAbstractIndiv+", "+roleInEntailedAssertion+", "+yAbstractIndiv);
			}
			PairOfSubjectAndObject pairOfXY = new PairOfSubjectAndObject(xAbstractIndiv, yAbstractIndiv);
			OWLObjectProperty roleConnectsXandY = this.dataForTransferingEntailments.getMap_XY_2_Role().get(pairOfXY);
			if (roleConnectsXandY == null){
				if(Configuration.getInstance().getDebuglevels().contains(DebugLevel.TRANSFER_ROLEASSERTION)){
				logger.info("role that connects"+ xAbstractIndiv+" and "+yAbstractIndiv+" is Null");}
				continue;
			}
			/*
			 * add role assertions
			 */
			Set<Integer> originalIndsCorrespondingToX = this.dataForTransferingEntailments
					.getOriginalIndividuals(xAbstractIndiv);
			for (Integer eachOriginalIndiv : originalIndsCorrespondingToX) {
				Set<Integer> allObjects = this.orarOntology.getSuccessors(eachOriginalIndiv, roleConnectsXandY);
				for (Integer eachObject : allObjects) {
					
					IndividualType oldTypeOfSubject = this.typeComputor.computeType(eachOriginalIndiv);
					Set<Integer> odlSetOfSubjectIndividuals = this.mapType2Individuals.get(oldTypeOfSubject);
					
					IndividualType oldTypeOfObject = this.typeComputor.computeType(eachObject);
					Set<Integer> odlSetOfObjectIndividuals = this.mapType2Individuals.get(oldTypeOfObject);
					if(Configuration.getInstance().getDebuglevels().contains(DebugLevel.TRANSFER_ROLEASSERTION)){
						logger.info("trying to add:"+eachOriginalIndiv+", "+roleInEntailedAssertion+", "+eachObject);
					}
					if (this.orarOntology.addRoleAssertion(eachOriginalIndiv, roleInEntailedAssertion, eachObject)) {
						this.isABoxExtended = true;
//						this.individualsHavingNewTypes.add(eachOriginalIndiv);
//						this.individualsHavingNewTypes.add(eachObject);
						logger.info("added role assertions:"+eachOriginalIndiv+","+ roleInEntailedAssertion+","+ eachObject);
						removeIndividuslFromOldMapType2Individuals(oldTypeOfSubject,odlSetOfSubjectIndividuals,eachOriginalIndiv);
						removeIndividuslFromOldMapType2Individuals(oldTypeOfObject,odlSetOfObjectIndividuals,eachObject);
						
						this.typeComputor.computeTypeIncrementally(eachOriginalIndiv);
						this.typeComputor.computeTypeIncrementally(eachObject);
						
						this.isABoxExtendedWithNewSpecialRoleAssertions = true;
						this.newRoleAssertions.addRoleAssertion(eachOriginalIndiv, roleInEntailedAssertion, eachObject);
					}
				}
			}
		}

	}

	/**
	 * transferring from abstract role assertions of the form R(u,x) or R(x,u),
	 * e.g. from rule R_\exists
	 */
	protected abstract void tranferRoleAssertionsBetweenUX();

	private void transferRoleAssertionsForLoopConcepts() {
		RoleAssertionList roleAssertionList = this.abstractRoleAssertionBox.getLoopRoleAssertions();
		int size = roleAssertionList.getSize();
		for (int index = 0; index < size; index++) {
			OWLNamedIndividual xInd = roleAssertionList.getSubject(index);
			OWLObjectProperty role = roleAssertionList.getRole(index);
			// get original individuals
			Set<Integer> allOriginalInds = this.dataForTransferingEntailments.getOriginalIndividuals(xInd);
			// add assertions to the orignal ABox
			for (Integer eachOriginalInd : allOriginalInds) {
				
				IndividualType oldTypeOfSubject = this.typeComputor.computeType(eachOriginalInd);
				Set<Integer> odlSetOfSubjectIndividuals = this.mapType2Individuals.get(oldTypeOfSubject);
				/*
				 * this is a loop role assertions, so we don't need to remove object from its type mapping.
				 */
								
				if (this.orarOntology.addRoleAssertion(eachOriginalInd, role, eachOriginalInd)) {
					this.isABoxExtended = true;
					
//					this.individualsHavingNewTypes.add(eachOriginalInd);
					
					removeIndividuslFromOldMapType2Individuals(oldTypeOfSubject,odlSetOfSubjectIndividuals,eachOriginalInd);
					
					
					this.typeComputor.computeTypeIncrementally(eachOriginalInd);
					
					this.isABoxExtendedWithNewSpecialRoleAssertions = true;
					this.newRoleAssertions.addRoleAssertion(eachOriginalInd, role, eachOriginalInd);
				}
			}
		}
	}

	private void removedFromOldTypeMap(Integer indiv){
		IndividualType oldType = this.typeComputor.computeType(indiv);
		Set<Integer> odlSetOfIndividuals = this.mapType2Individuals.get(oldType);
		if (odlSetOfIndividuals==null){
			logger.info("oldType="+oldType);
			logger.info("Print map: type-->individusl");
			PrintingHelper.printMap(this.mapType2Individuals);
		}
		odlSetOfIndividuals.remove(indiv);
	}
	protected void transferConceptAssertions() {
		transferConceptAssertionsOf_X();
		transferConceptAssertionsOf_YZ();

	}

	/**
	 * update type and consequently update concept assertions for individuals
	 * corresponding to X. Note that we don't care about if update happens for
	 * termination condition. therefore {@code 	this.isABoxExtendedViaX} could
	 * be in the default value False.
	 */
	private void transferConceptAssertionsOf_X() {

		Iterator<Entry<OWLNamedIndividual, Set<OWLClass>>> iterator = this.xAbstractConceptAssertionsAsMap.entrySet()
				.iterator();

		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLClass>> entry = iterator.next();

			OWLNamedIndividual XIndiv = entry.getKey();
			Set<OWLClass> entailedConceptsOfX = entry.getValue();

			IndividualType typeOfX = mapX2Type.get(XIndiv);
			Set<Integer> indivCorrespondingtoTypeOfX = mapType2Individuals.get(typeOfX);
			/*
			 * remove old entry
			 */
			mapType2Individuals.remove(typeOfX, indivCorrespondingtoTypeOfX);
			/*
			 * extend concept type in the type
			 */
			if (typeOfX==null){
				logger.info("Null typeOf: "+XIndiv);
			}
			typeOfX.getConcepts().addAll(entailedConceptsOfX);
			/*
			 * check if the extended type already exists
			 */
			Set<Integer> indivsOfExistingExtendedType = mapType2Individuals.get(typeOfX);
			if (indivsOfExistingExtendedType == null) {
				/*
				 * put entry with extended type back
				 */
				mapType2Individuals.put(typeOfX, indivCorrespondingtoTypeOfX);
			} else {
				/*
				 * merging two types;
				 */
				indivCorrespondingtoTypeOfX.addAll(indivsOfExistingExtendedType);
				mapType2Individuals.put(typeOfX, indivCorrespondingtoTypeOfX);
				/*
				 * update concept assertion map so that individuals of the same
				 * type point to the same concept type
				 */
				Set<OWLClass> newConceptType = typeOfX.getConcepts();
				for (int eachIndiv : indivsOfExistingExtendedType) {
					conceptAssertionBox.setAssertedConcepts(eachIndiv, newConceptType);
				}
			}
		}
	}

	protected boolean transferConceptAssertionsOf_YZ() {
		Map<Integer, Set<OWLClass>> entailedAssertionTransfeeredFromYZ = accumulateAssertionFromYZ();

		Iterator<Entry<Integer, Set<OWLClass>>> iterator = entailedAssertionTransfeeredFromYZ.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Integer, Set<OWLClass>> entry = iterator.next();
			Integer indiv = entry.getKey();
			Set<OWLClass> entailedCocnepts = entry.getValue();
			/*
			 * create a new set including existing asserted concept of indiv
			 */
			Set<OWLClass> newConcepts = new HashSet<OWLClass>(this.orarOntology.getAssertedConcepts(indiv));

			/*
			 * try to add entailed concepts to the existing one. Do nothing
			 * unless there are newly concepts added
			 */
			if (newConcepts.addAll(entailedCocnepts)) {
				this.isABoxExtendedViaY = true;
				this.isABoxExtended=true;
				removedFromOldTypeMap(indiv);
//				logger.info("indiv: "+indiv);
//				logger.info("new concepts:"+newConcepts);
				this.conceptAssertionBox.setAssertedConcepts(indiv, newConcepts);
				this.typeComputor.computeTypeIncrementally(indiv);
//				logger.info("Printing maps type-->indivs");
//				PrintingHelper.printMap(this.mapType2Individuals);
			}

		}

		return this.isABoxExtendedViaY;
	}

	private Map<Integer, Set<OWLClass>> accumulateAssertionFromYZ() {
		Map<Integer, Set<OWLClass>> possiblyNewAssertionsMap = new HashMap<Integer, Set<OWLClass>>();

		groupOriginalConceptAssertions(yAbstractConceptAssertionsAsMap,
				this.dataForTransferingEntailments.getMap_YAbstractIndiv_2_OriginalIndivs(), possiblyNewAssertionsMap);

		groupOriginalConceptAssertions(zAbstractConceptAssertionsAsMap,
				this.dataForTransferingEntailments.getMap_ZAbstractIndiv_2_OriginalIndivs(), possiblyNewAssertionsMap);

		return possiblyNewAssertionsMap;
	}

	/**
	 * collect original concept assertions by Y and Z. The result is store in a
	 * map {@code originalConceptAssertionMap}
	 * 
	 * @param assertionMap
	 * @param mapFromAbstractIndividual2OriginalIndividual
	 * @param originalConceptAssertionMap
	 */
	private void groupOriginalConceptAssertions(Map<OWLNamedIndividual, Set<OWLClass>> assertionMap,
			Map<OWLNamedIndividual, Set<Integer>> mapFromAbstractIndividual2OriginalIndividual,
			Map<Integer, Set<OWLClass>> originalConceptAssertionMap) {

		Iterator<Entry<OWLNamedIndividual, Set<OWLClass>>> iterator = assertionMap.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLClass>> entry = iterator.next();
			OWLNamedIndividual abstractInd = entry.getKey();
			Set<OWLClass> concepts = entry.getValue();
			if (concepts != null) {
				Set<Integer> originalIndividuals = mapFromAbstractIndividual2OriginalIndividual.get(abstractInd);
				for (Integer originalInd : originalIndividuals) {

					// /*
					// * Debug
					// */
					// if (originalInd.equals(bug_individual)){
					// logger.info("***something wrong might come from here
					// ***");
					// logger.info("individual:"+bug_individual);
					// logger.info("Concepts:"+concepts);
					// logger.info("Reason:");
					// logger.info("By assertion from bstract
					// individual:"+abstractInd);
					// //if (sharedData.get)
					//
					// }
					if (config.getDebuglevels().contains(DebugLevel.UPDATING_CONCEPT_ASSERTION)) {
						logger.info("***DEBUG Update concept assertions in the original ABox ***");
						logger.info("Individual:" + IndividualIndexer.getInstance().getIndividualString(originalInd));
						logger.info("has new concepts:" + concepts);
						logger.info("Reason: get from concept assertion of the abstract individual:" + abstractInd);
						logger.info("*=====================================================*");
					}
					/*
					 * end of debug
					 */
					Set<OWLClass> existingAssertedConcept = originalConceptAssertionMap.get(originalInd);
					if (existingAssertedConcept == null) {
						existingAssertedConcept = new HashSet<>();
						originalConceptAssertionMap.put(originalInd, existingAssertedConcept);
					}
					existingAssertedConcept.addAll(concepts);
				}
			}
		}

	}

	@Override
	public boolean isABoxExtended() {

		return this.isABoxExtended;
	}

	public boolean isABoxExtendedViaX() {
		return this.isABoxExtendedViaX;
	}

	public boolean isABoxExtendedViaY() {
		return this.isABoxExtendedViaY;
	}

	public boolean isABoxExtendedViaZ() {
		return this.isABoxExtendedViaZ;
	}

	public boolean isABoxExtendedWithNewSameasAssertions() {
		return this.isABoxExtendedWithNewSameasAssertions;
	}

	public boolean isABoxExtendedWithNewSpecialRoleAssertions() {
		return this.isABoxExtendedWithNewSpecialRoleAssertions;
	}

	@Override
	public IndexedRoleAssertionList getNewlyAddedRoleAssertions() {
		return this.newRoleAssertions;
	}

	@Override
	public Set<Set<Integer>> getNewlyAddedSameasAssertions() {
		return this.newSameasAssertions;
	}
	public Set<Integer> getIndividualsHavingNewAssertions(){
		return this.individualsHavingNewTypes;
	}
}
