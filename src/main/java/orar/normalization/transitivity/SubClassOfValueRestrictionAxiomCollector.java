package orar.normalization.transitivity;

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
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
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

/**
 * Visit axiom of the form A1 and A2 and ... An SubClassOf forall R. B
 * 
 * @author kien
 *
 */
public class SubClassOfValueRestrictionAxiomCollector implements
		OWLAxiomVisitorEx<OWLSubClassOfAxiom> {

	@Override
	public OWLSubClassOfAxiom visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLAnnotationPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLAnnotationPropertyRangeAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLSubClassOfAxiom axiom) {
		OWLClassExpression superClass = axiom.getSuperClass();
		if (superClass instanceof OWLObjectAllValuesFrom) {
			return axiom;
		}
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(
			OWLNegativeObjectPropertyAssertionAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLReflexiveObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDisjointClassesAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDataPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLObjectPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLNegativeDataPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDifferentIndividualsAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDisjointDataPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDisjointObjectPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLObjectPropertyRangeAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLFunctionalObjectPropertyAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLSubObjectPropertyOfAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDisjointUnionAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDeclarationAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLAnnotationAssertionAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLSymmetricObjectPropertyAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDataPropertyRangeAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLFunctionalDataPropertyAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLEquivalentDataPropertiesAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLClassAssertionAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLEquivalentClassesAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDataPropertyAssertionAxiom axiom) {

		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLTransitiveObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLSubDataPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(
			OWLInverseFunctionalObjectPropertyAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLSameIndividualAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLSubPropertyChainOfAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLInverseObjectPropertiesAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLHasKeyAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(OWLDatatypeDefinitionAxiom axiom) {
		return null;
	}

	@Override
	public OWLSubClassOfAxiom visit(SWRLRule rule) {
		return null;
	}

}
