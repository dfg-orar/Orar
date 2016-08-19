package orar.normalization;



import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;

public interface SubClassNormalizer extends
		OWLClassExpressionVisitorEx<OWLClassExpression> {

	public void setSuperClassNormalizer(
			SuperClassNormalizer superClassNormalizer);
}
