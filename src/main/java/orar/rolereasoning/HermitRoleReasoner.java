package orar.rolereasoning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;

import orar.util.Pause;
import orar.util.PrintingHelper;

public class HermitRoleReasoner implements RoleReasoner {
	private final Logger logger = Logger.getLogger(HermitRoleReasoner.class);
	// output data
	private Reasoner hermit;
	private final Set<OWLObjectProperty> functionalRoles;
	private final Set<OWLObjectProperty> inverseFunctionalRoles;
	private final Set<OWLObjectProperty> transitiveRoles;
	private final Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> subRoleMaps;
	private final Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> inverseRoleMap;
	// Others
	private final Set<OWLAxiom> tboxRboxAxioms;
	private OWLOntology owlOntologyWithRoleAxiomsOnly;
	private OWLDataFactory owlDataFactory;

	public HermitRoleReasoner(Set<OWLAxiom> tboxRboxAxioms) {
		this.functionalRoles = new HashSet<OWLObjectProperty>();
		this.inverseFunctionalRoles = new HashSet<OWLObjectProperty>();
		this.subRoleMaps = new HashMap<OWLObjectProperty, Set<OWLObjectPropertyExpression>>();
		this.transitiveRoles = new HashSet<OWLObjectProperty>();

		this.tboxRboxAxioms = tboxRboxAxioms;
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		this.inverseRoleMap = new HashMap<OWLObjectProperty, Set<OWLObjectPropertyExpression>>();

	}

	/**
	 * create OWLOntology contain only role axioms
	 */
	private void createOWLOntologyWithRoleAxiomsForComputingRoleHierarchy() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			owlOntologyWithRoleAxiomsOnly = manager.createOntology();
			Set<OWLAxiom> axioms = AxiomOfSpecificTypeGetter
					.getObjectPropertyAxiomsForComputingRoleHierarchy(this.tboxRboxAxioms);
			manager.addAxioms(owlOntologyWithRoleAxiomsOnly, axioms);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doReasoning() {
		createOWLOntologyWithRoleAxiomsForComputingRoleHierarchy();
		hermit = new Reasoner(owlOntologyWithRoleAxiomsOnly);
		hermit.classifyObjectProperties();
		hermit.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);

//		createMapFromRoleToItsInverses();
		computRoleHierarchy();
		computeTransitiveRoles();
		computeSubRolesOfDefinedFunctionalRoles();
		computeSubRolesOfDefinedInverseFunctionalRoles();
		computeFunctionalityTakingIntoAccountInverseAxioms();
		computeFunctionalityTakingIntoAccountSymetricRoleAxioms();
		this.functionalRoles.remove(owlDataFactory.getOWLBottomObjectProperty());
		this.inverseFunctionalRoles.remove(owlDataFactory.getOWLBottomObjectProperty());
//		logger.info("***DEBUG*** rolehierarchy: ");
//		PrintingHelper.printMap(this.subRoleMaps);
//
//		logger.info("***DEBUG*** functionalRoles: ");
//		PrintingHelper.printSet(this.functionalRoles);
//		
//		logger.info("***DEBUG*** inverseFunctionalRoles: ");
//		PrintingHelper.printSet(this.inverseFunctionalRoles);
	}

	private void computRoleHierarchy() {
		Set<OWLObjectProperty> allRoles = owlOntologyWithRoleAxiomsOnly.getObjectPropertiesInSignature(true);
		// logger.info("***DEBUG***:");
		// PrintingHelper.printSet(this.owlOntologyWithRoleAxiomsOnly.getAxioms());
		// Pause.pause();
		allRoles.remove(owlDataFactory.getOWLTopObjectProperty());
		for (OWLObjectProperty role : allRoles) {
			/*
			 * get super roles
			 */
			Set<OWLObjectPropertyExpression> superRoles = hermit.getSuperObjectProperties(role, false).getFlattened();
			superRoles.remove(owlDataFactory.getOWLTopObjectProperty());
			superRoles.remove(role);
			if (!superRoles.isEmpty()) {
				addEntryToAMap(role, superRoles, this.subRoleMaps);
				
			}
			/*
			 * get from equivalents roles
			 */
			Set<OWLObjectPropertyExpression> equivRoles = hermit.getEquivalentObjectProperties(role)
					.getEntitiesMinusTop();
			equivRoles.remove(role);
			if (!equivRoles.isEmpty()) {
				addEntryToAMap(role, equivRoles, this.subRoleMaps);
			}
			/*
			 * get from symetric assertions? No, don't know why hermit taking
			 * symetric but not equivalents into account when computing role
			 * hierarchy
			 */
		}
	}

	private void computeTransitiveRoles() {
		Set<OWLTransitiveObjectPropertyAxiom> tranRoleAxioms = AxiomOfSpecificTypeGetter
				.getTranRoleAxioms(tboxRboxAxioms);
		for (OWLTransitiveObjectPropertyAxiom axiom : tranRoleAxioms) {
			Set<OWLObjectProperty> roles = axiom.getObjectPropertiesInSignature();
			this.transitiveRoles.addAll(roles);
		}

	}

	/**
	 * compute sub-roles of defined Functional roles.
	 */
	private void computeSubRolesOfDefinedFunctionalRoles() {
		Set<OWLFunctionalObjectPropertyAxiom> definedFuncAxioms = AxiomOfSpecificTypeGetter
				.getFunctionalAxioms(this.tboxRboxAxioms);
		for (OWLFunctionalObjectPropertyAxiom axiom : definedFuncAxioms) {
			OWLObjectProperty functProperty = axiom.getProperty().asOWLObjectProperty();
			this.functionalRoles.add(functProperty);
			Set<OWLObjectPropertyExpression> subPropertyExpressions = hermit
					.getSubObjectProperties(functProperty, false).getFlattened();
			for (OWLObjectPropertyExpression exp : subPropertyExpressions) {

				OWLObjectProperty subRole;
				if (exp instanceof OWLObjectInverseOf) {
					OWLObjectInverseOf expInverserOf = (OWLObjectInverseOf) exp;
					subRole = expInverserOf.getSimplified().getInverseProperty().getSimplified().asOWLObjectProperty();
					this.inverseFunctionalRoles.add(subRole);
				} else {
					subRole = exp.asOWLObjectProperty();
					this.functionalRoles.add(subRole);
				}

			}
		}
	}

	/**
	 * compute sub-roles of defined InverseFunctional roles.
	 */
	private void computeSubRolesOfDefinedInverseFunctionalRoles() {
		Set<OWLInverseFunctionalObjectPropertyAxiom> definedInverseFuncAxioms = AxiomOfSpecificTypeGetter
				.getInverseInverseFunctionalPropertyAxioms(this.tboxRboxAxioms);
		for (OWLInverseFunctionalObjectPropertyAxiom axiom : definedInverseFuncAxioms) {
			OWLObjectProperty inverseFunctProperty = axiom.getProperty().asOWLObjectProperty();
			this.inverseFunctionalRoles.add(inverseFunctProperty);
			Set<OWLObjectPropertyExpression> subPropertyExpressions = hermit
					.getSubObjectProperties(inverseFunctProperty, false).getFlattened();
			for (OWLObjectPropertyExpression exp : subPropertyExpressions) {

				OWLObjectProperty subRole;
				if (exp instanceof OWLObjectInverseOf) {
					OWLObjectInverseOf expInverserOf = (OWLObjectInverseOf) exp;
					subRole = expInverserOf.getSimplified().getInverseProperty().getSimplified().asOWLObjectProperty();
					this.functionalRoles.add(subRole);
				} else {
					subRole = exp.asOWLObjectProperty();
					this.inverseFunctionalRoles.add(subRole);
				}

			}
		}
	}

	/**
	 * Compute (inverse)functional roles taking InverseAxioms into account. This
	 * method should be called after
	 * {@link #computeSubRolesOfDefinedFunctionalRoles() and
	 * #computeSubRolesOfDefinedInverseFunctionalRoles()}
	 */
	private void computeFunctionalityTakingIntoAccountInverseAxioms() {
		Set<OWLInverseObjectPropertiesAxiom> inverseRoleAxioms = AxiomOfSpecificTypeGetter
				.getInverseObjectPropertyAxioms(this.tboxRboxAxioms);

		for (OWLInverseObjectPropertiesAxiom axiom : inverseRoleAxioms) {
			OWLObjectProperty firstRole = axiom.getFirstProperty().asOWLObjectProperty();
			OWLObjectProperty secondRole = axiom.getSecondProperty().asOWLObjectProperty();
			if (this.functionalRoles.contains(firstRole)) {
				this.inverseFunctionalRoles.add(secondRole);
			}

			if (this.inverseFunctionalRoles.contains(firstRole)) {
				this.functionalRoles.add(secondRole);
			}

			if (this.functionalRoles.contains(secondRole)) {
				this.inverseFunctionalRoles.add(firstRole);
			}

			if (this.inverseFunctionalRoles.contains(secondRole)) {
				this.functionalRoles.add(firstRole);
			}

		}

	}

	/**
	 * Compute (inverse)functional roles taking SymetricPropertyAxiom into
	 * account. This method should be called after
	 * {@link #computeSubRolesOfDefinedFunctionalRoles() and
	 * #computeSubRolesOfDefinedInverseFunctionalRoles()}
	 */
	private void computeFunctionalityTakingIntoAccountSymetricRoleAxioms() {
		Set<OWLSymmetricObjectPropertyAxiom> symetricAxioms = AxiomOfSpecificTypeGetter
				.getSymetricPropertyAxioms(this.tboxRboxAxioms);

		for (OWLSymmetricObjectPropertyAxiom axiom : symetricAxioms) {
			OWLObjectProperty role = axiom.getProperty().asOWLObjectProperty();

			if (this.functionalRoles.contains(role)) {
				this.inverseFunctionalRoles.add(role);
			}

			if (this.inverseFunctionalRoles.contains(role)) {
				this.functionalRoles.add(role);
			}

		}

	}

	@Override
	public Set<OWLObjectProperty> getFunctionalRoles() {

		return this.functionalRoles;
	}

	@Override
	public Set<OWLObjectProperty> getInverseFunctionalRoles() {

		return this.inverseFunctionalRoles;
	}

	@Override
	public Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> getRoleHierarchyAsMap() {
		return this.subRoleMaps;
	}

	@Override
	public Set<OWLObjectProperty> getTransitiveRoles() {

		return this.transitiveRoles;
	}

//	private void createMapFromRoleToItsInverses() {
//		Set<OWLInverseObjectPropertiesAxiom> allInverseRoleAxioms = AxiomOfSpecificTypeGetter
//				.getInverseObjectPropertyAxioms(this.tboxRboxAxioms);
//
//		// logger.info("***DEBUG***
//		// allInverseRoleAxioms:"+allInverseRoleAxioms);
//		// Pause.pause();
//
//		for (OWLInverseObjectPropertiesAxiom invroleAxiom : allInverseRoleAxioms) {
//			OWLObjectPropertyExpression left = invroleAxiom.getFirstProperty().getSimplified();
//			OWLObjectPropertyExpression right = invroleAxiom.getSecondProperty().getSimplified();
//
//			// add entry for left
//			if (left instanceof OWLObjectProperty) {
//				addEntryToAMap(left.asOWLObjectProperty(), right);
//			} else if (left instanceof OWLObjectInverseOf) {
//				addEntryToAMap(left.getNamedProperty(), this.owlDataFactory.getOWLObjectInverseOf(right));
//			}
//
//			// add entry for right
//			if (right instanceof OWLObjectProperty) {
//				addEntryToAMap(right.asOWLObjectProperty(), left);
//			} else if (right instanceof OWLObjectInverseOf) {
//				addEntryToAMap(right.getNamedProperty(), this.owlDataFactory.getOWLObjectInverseOf(left));
//			}
//
//		}
//	}

	/**
	 * add value to the existing value of the key.
	 * 
	 * @param key
	 * @param value
	 */
	private void addEntryToAMap(OWLObjectProperty key, Set<OWLObjectPropertyExpression> values, Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> map) {
		Set<OWLObjectPropertyExpression> existingValues = map.get(key);
		if (existingValues == null) {
			existingValues = new HashSet<OWLObjectPropertyExpression>();
		}
		existingValues.addAll(values);
		map.put(key, existingValues);
	}
	
//	/**
//	 * add value to the existing value of the key.
//	 * 
//	 * @param key
//	 * @param value
//	 */
//	private void addEntryToAMap(OWLObjectProperty key, OWLObjectPropertyExpression value, Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> map) {
//		Set<OWLObjectPropertyExpression> existingValues = map.get(key);
//		if (existingValues == null) {
//			existingValues = new HashSet<>();
//		}
//		existingValues.add(value);
//		map.put(key, existingValues);
//	}

//	@Override
//	public Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> getInverseRoleMap() {
//
//		return this.inverseRoleMap;
//	}

}
