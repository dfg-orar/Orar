package orar.normalization.ALCHOIF;

import java.util.Stack;

import orar.normalization.ALCHOI.ALCHOI_SubClassNormalizer;

import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * Normalize SubClass of SubClassOfAxiom.
 * Nominals will be collected during normalization.
 * 
 * @author kien
 *
 */
public class ALCHOIF_SubClassNormalizer extends ALCHOI_SubClassNormalizer {

	public ALCHOIF_SubClassNormalizer(Stack<OWLSubClassOfAxiom> stack) {
		super(stack);
	}
}
