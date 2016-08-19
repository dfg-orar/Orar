package orar.dlfragmentvalidator.HornALCHOIF;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

import orar.dlfragmentvalidator.AxiomValidator;
import orar.dlfragmentvalidator.DLConstructor;
import orar.dlfragmentvalidator.ValidatorDataFactory;

/**
 * 
 * @author kien
 *
 */
public class HornALCHOIF_AxiomValidator implements AxiomValidator {
	/*
	 * store violated axioms that are of interest like disjunction, role chain,
	 * transitivity
	 */
	protected final Set<OWLAxiom> violatedAxioms; // TODO: could be better by a
													// map between type of axiom
													// and
													// axioms
	protected final Set<DLConstructor> constructorsInInputOntology;
	protected final Set<DLConstructor> constructorsInValidatedOntology;
	protected final HornALCHOIF_SubClass_Validator subClassValidator;
	protected final HornALCHOIF_SuperClass_Validator superClassValidator;
	protected final OWLDataFactory owlDataFact;
	protected final ValidatorDataFactory validatorDataFactory;
	/*
	 * store generated axioms, which are part of violated axioms, e.g. C equiv
	 * D, where C subClassof D is not in the profile but D subClassOf C is in
	 * the profile.
	 */
	private Set<OWLAxiom> generatedAxioms;

	public HornALCHOIF_AxiomValidator() {
		this.violatedAxioms = new HashSet<OWLAxiom>();
		this.constructorsInInputOntology = new HashSet<DLConstructor>();
		this.constructorsInValidatedOntology = new HashSet<DLConstructor>();
		this.subClassValidator = new HornALCHOIF_SubClass_Validator();
		this.superClassValidator = new HornALCHOIF_SuperClass_Validator();
		this.generatedAxioms = new HashSet<OWLAxiom>();
		this.owlDataFact = OWLManager.getOWLDataFactory();
		this.validatorDataFactory = ValidatorDataFactory.getInstance();
	}

	@Override
	public Set<DLConstructor> getDLConstructorsInInputOntology() {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.addAll(this.constructorsInInputOntology);
		constructors.addAll(this.subClassValidator.getDlConstructorsInInputOntology());
		constructors.addAll(this.superClassValidator.getDlConstructorsInInputOntology());
		return constructors;
	}

	@Override
	public Set<OWLAxiom> getViolatedAxioms() {
		return violatedAxioms;
	}

	@Override
	public Set<OWLAxiom> getGeneratedAxioms() {
		return generatedAxioms;
	}

	@Override
	public boolean isNotViolatedProfile() {
		return (this.violatedAxioms.size() == 0);
	}

	@Override
	public OWLAxiom visit(OWLSubAnnotationPropertyOfAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLAnnotationPropertyDomainAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLAnnotationPropertyRangeAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLSubClassOfAxiom axiom) {
		OWLClassExpression subClass = axiom.getSubClass();
		OWLClassExpression superClass = axiom.getSuperClass();
		OWLClassExpression profiledSupClass = subClass.accept(this.subClassValidator);
		OWLClassExpression profiledSuperClass = superClass.accept(this.superClassValidator);
		if (profiledSupClass == null || profiledSuperClass == null) {
			this.violatedAxioms.add(axiom);
			return null;
		}
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		/*
		 * normalize to atomic role assertion
		 */
		OWLObjectPropertyExpression role = axiom.getProperty();
		OWLIndividual subject = axiom.getSubject();
		OWLIndividual object = axiom.getObject();
		OWLObjectProperty atomicRole = ValidatorDataFactory.getInstance().getFreshProperty();
		OWLDisjointObjectPropertiesAxiom newAxiom = owlDataFact.getOWLDisjointObjectPropertiesAxiom(atomicRole, role);
		this.generatedAxioms.add(newAxiom);
		OWLObjectPropertyAssertionAxiom atomicRoleAsertion = owlDataFact.getOWLObjectPropertyAssertionAxiom(atomicRole,
				subject, object);
		return atomicRoleAsertion;
	}

	@Override
	public OWLAxiom visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLReflexiveObjectPropertyAxiom axiom) {
		this.constructorsInInputOntology.add(DLConstructor.REFLEXSIVEROLE);
		this.violatedAxioms.add(axiom);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLDisjointClassesAxiom axiom) {
		Set<OWLClassExpression> allConcepts = axiom.getClassExpressions();
		boolean violated = false;
		for (OWLClassExpression concept : allConcepts) {
			if (concept.accept(subClassValidator) == null) {
				violated = true;
				break;
			}
		}
		if (violated) {
			this.violatedAxioms.add(axiom);
			return null;
		}
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDataPropertyDomainAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLObjectPropertyDomainAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLEquivalentObjectPropertiesAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLNegativeDataPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLDifferentIndividualsAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDisjointDataPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLDisjointObjectPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public OWLAxiom visit(OWLObjectPropertyRangeAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {
		OWLObjectPropertyExpression role = axiom.getProperty();
		/*
		 * only allows atomic role
		 */
		if (role instanceof OWLObjectProperty) {
			return axiom;
		}

		this.violatedAxioms.add(axiom);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLFunctionalObjectPropertyAxiom axiom) {
		this.constructorsInInputOntology.add(DLConstructor.FUNCTIONALITY);
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSubObjectPropertyOfAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDisjointUnionAxiom axiom) {
		this.constructorsInInputOntology.add(DLConstructor.NonHorn_DISJUNCTION);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLDeclarationAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLAnnotationAssertionAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSymmetricObjectPropertyAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDataPropertyRangeAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLFunctionalDataPropertyAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLEquivalentDataPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLClassAssertionAxiom axiom) {
		OWLIndividual ind = axiom.getIndividual();
		OWLClassExpression concept = axiom.getClassExpression();
		if (concept instanceof OWLClass) {
			return axiom;
		}
		/*
		 * convert to atomic concept assertion if necessary
		 */
		OWLClass atomicConcept = validatorDataFactory.getFreshOWLClass();
		OWLSubClassOfAxiom newSubClassOfAxiom = owlDataFact.getOWLSubClassOfAxiom(atomicConcept, concept);
		this.generatedAxioms.add(newSubClassOfAxiom);
		OWLClassAssertionAxiom atomicConceptAssertion = owlDataFact.getOWLClassAssertionAxiom(atomicConcept, ind);
		return atomicConceptAssertion;
	}

	@Override
	public OWLAxiom visit(OWLEquivalentClassesAxiom axiom) {
		/*
		 * Split into SubClassOf axioms. Take one that is in the profile.
		 */
		List<OWLClassExpression> allConcepts = axiom.getClassExpressionsAsList();
		int size = allConcepts.size();
		for (int i = 0; i < size - 1; i++)
			for (int j = i + 1; j < size; j++) {
				/*
				 * leftSize=A, rightSize=B, and we have: A=B
				 */
				OWLClassExpression leftSize = allConcepts.get(i);
				OWLClassExpression rightSize = allConcepts.get(j);
				/*
				 * Check if A subclassof B is Horn
				 */
				OWLClassExpression leftSizeAsSubClass = leftSize.accept(subClassValidator);
				OWLClassExpression rightSizeAsSuperClass = rightSize.accept(superClassValidator);
				if (leftSizeAsSubClass != null && rightSizeAsSuperClass != null) {
					OWLSubClassOfAxiom subClassOfAxiom1 = owlDataFact.getOWLSubClassOfAxiom(leftSizeAsSubClass,
							rightSizeAsSuperClass);
					this.generatedAxioms.add(subClassOfAxiom1);
				} else
					this.violatedAxioms.add(axiom);

				/*
				 * Check if B subClassOf A is Horn
				 */
				OWLClassExpression rightSizeAsSubClass = rightSize.accept(subClassValidator);
				OWLClassExpression leftSizeAsSuperClass = leftSize.accept(superClassValidator);
				if (rightSizeAsSubClass != null && leftSizeAsSuperClass != null) {
					OWLSubClassOfAxiom subClassOfAxiom2 = owlDataFact.getOWLSubClassOfAxiom(rightSizeAsSubClass,
							leftSizeAsSuperClass);
					this.generatedAxioms.add(subClassOfAxiom2);
				} else
					this.violatedAxioms.add(axiom);

			}
		return null;
	}

	@Override
	public OWLAxiom visit(OWLDataPropertyAssertionAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLTransitiveObjectPropertyAxiom axiom) {
		this.violatedAxioms.add(axiom);
		this.constructorsInInputOntology.add(DLConstructor.TRANSITIVITY);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		this.violatedAxioms.add(axiom);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLSubDataPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSameIndividualAxiom axiom) {
		// TODO: get nominals
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSubPropertyChainOfAxiom axiom) {
		this.violatedAxioms.add(axiom);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLInverseObjectPropertiesAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLHasKeyAxiom axiom) {
		this.violatedAxioms.add(axiom);
		return null;
	}

	@Override
	public OWLAxiom visit(OWLDatatypeDefinitionAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(SWRLRule rule) {
		return null;
	}

	@Override
	public int getNumberOfCardinalityAxioms() {

		return -1; // FIX ME
	}

	@Override
	public Set<DLConstructor> getDLConstructorsInValidatedOntology() {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.addAll(this.constructorsInValidatedOntology);
		constructors.addAll(this.subClassValidator.getDlConstructorsInValidatedOntology());
		constructors.addAll(this.superClassValidator.getDlConstructorsInValidatedOntology());
		return constructors;
	}

}
