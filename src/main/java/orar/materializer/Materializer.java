package orar.materializer;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

public interface Materializer {

	/**
	 * Materialize via abstraction and refinement
	 */
	public void materialize();

	/**
	 * @return number of refinement steps.
	 */
	public int getNumberOfRefinements();

	public void dispose();

	/**
	 * @return the orarontology
	 */
	public OrarOntology2 getOrarOntology();

	public long getReasoningTimeInSeconds();
	
	public boolean isOntologyConsistent();
}
