package orar.normalization;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;

public interface SuperClassNormalizer extends
		OWLClassExpressionVisitorEx<OWLClassExpression> {

	public void setSubClassNormalizer(SubClassNormalizer subClassNormalizer);

}
