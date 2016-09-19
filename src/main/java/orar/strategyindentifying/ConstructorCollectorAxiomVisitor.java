package orar.strategyindentifying;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
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
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
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

import orar.dlfragmentvalidator.DLConstructor;

public class ConstructorCollectorAxiomVisitor implements OWLAxiomVisitorEx<Set<DLConstructor>> {

	@Override
	public Set<DLConstructor> visit(OWLSubAnnotationPropertyOfAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLAnnotationPropertyDomainAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLAnnotationPropertyRangeAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLSubClassOfAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		OWLClassExpression subClass = axiom.getSubClass();
		ConstructorCollectorInSubClass collectorInSubClass = new ConstructorCollectorInSubClass();
		DLConstructor constructorInSubClass = subClass.accept(collectorInSubClass);
		if (constructorInSubClass != null) {
			constructors.add(subClass.accept(collectorInSubClass));
		}

		OWLClassExpression superClass = axiom.getSuperClass();
		ConstructorCollectorInSuperClass collectorInSuperClass = new ConstructorCollectorInSuperClass(subClass);
		DLConstructor constructorInSuperClass = superClass.accept(collectorInSuperClass);
		if (constructorInSuperClass != null) {
			constructors.add(superClass.accept(collectorInSuperClass));
		}

		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLAsymmetricObjectPropertyAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLReflexiveObjectPropertyAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDisjointClassesAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDataPropertyDomainAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLObjectPropertyDomainAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.DOMAIN_CONSTRAIN_OF_ROLE);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.EQUIVALENT_ROLE);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDifferentIndividualsAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDisjointDataPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDisjointObjectPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLObjectPropertyRangeAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.RANGE_CONSTRAIN_OF_ROLE);
		return constructors;

	}

	@Override
	public Set<DLConstructor> visit(OWLObjectPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLFunctionalObjectPropertyAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.FUNCTIONALITY);
		return constructors;

	}

	@Override
	public Set<DLConstructor> visit(OWLSubObjectPropertyOfAxiom axiom) {

		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.SUB_ROLE);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLDisjointUnionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDeclarationAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLAnnotationAssertionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLSymmetricObjectPropertyAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.SYMMETRIC_ROLE);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLDataPropertyRangeAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLFunctionalDataPropertyAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLEquivalentDataPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLClassAssertionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLEquivalentClassesAxiom axiom) {

		return null;// should not have in the normal form
	}

	@Override
	public Set<DLConstructor> visit(OWLDataPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLTransitiveObjectPropertyAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.TRANSITIVITY);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLSubDataPropertyOfAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.INVERSE_FUNCTIONALITY);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLSameIndividualAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLSubPropertyChainOfAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLInverseObjectPropertiesAxiom axiom) {
		Set<DLConstructor> constructors = new HashSet<DLConstructor>();
		constructors.add(DLConstructor.INVERSE_ROLE);
		return constructors;
	}

	@Override
	public Set<DLConstructor> visit(OWLHasKeyAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(OWLDatatypeDefinitionAxiom axiom) {

		return null;
	}

	@Override
	public Set<DLConstructor> visit(SWRLRule rule) {

		return null;
	}

}
