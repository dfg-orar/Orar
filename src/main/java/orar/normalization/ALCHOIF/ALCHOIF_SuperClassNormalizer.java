package orar.normalization.ALCHOIF;

import java.util.Stack;

import orar.normalization.ALCHOI.ALCHOI_SuperClassNormalizer;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * Normalize SuperClass of SubClassOfAxiom. Nominals also are collected.
 * 
 * @author kien
 *
 */
public class ALCHOIF_SuperClassNormalizer extends ALCHOI_SuperClassNormalizer {

	public ALCHOIF_SuperClassNormalizer(Stack<OWLSubClassOfAxiom> stack) {
		super(stack);

	}

	@Override
	public OWLClassExpression visit(OWLObjectSomeValuesFrom ce) {
		OWLClassExpression filler = ce.getFiller();
		/*
		 * not normalize if filler is atomic concept or conjunction of atomic
		 * concepts
		 */
		if (filler instanceof OWLClass)
			return ce;
		if (filler instanceof OWLObjectIntersectionOf) {
			boolean allAtomic = true;
			OWLObjectIntersectionOf conjunction = (OWLObjectIntersectionOf) filler;
			for (OWLClassExpression operand : conjunction.getOperands()) {
				if (!(operand instanceof OWLClass)) {
					allAtomic = false;
					break;
				}
			}
			if (allAtomic)
				return ce;

		}

		if (filler instanceof OWLObjectOneOf) {
			OWLObjectOneOf objectOneOf = (OWLObjectOneOf) filler;
			for (OWLIndividual ind : objectOneOf.getIndividuals()) {
				if (ind instanceof OWLNamedIndividual) {
					this.metaDataOfOntology.getNominals().add(ind.asOWLNamedIndividual());
				}
			}
			return ce;
		}

		OWLObjectPropertyExpression propertyExp = ce.getProperty();
		OWLClass subFreshClass = replaceBySubFreshClass(filler);
		return owlDataFactory.getOWLObjectSomeValuesFrom(propertyExp, subFreshClass);
	}

}
