package x.io.ontologyreader;

import org.semanticweb.owlapi.model.OWLOntology;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;

/**
 * A collections of methods for loading ontology.
 * 
 * @author kien
 *
 */
public interface OntologyReader {
	/**
	 * @param ontologyFileName
	 *            a file that contains both TBox and ABox axioms
	 * @return an OrarOntology in the target DL Fragment with TBox axioms are
	 *         normalized
	 */
	public OrarOntology2 getNormalizedOrarOntology(String ontologyFileName);
	
	/**
	 * @param ontologyFileName
	 *            a file that contains both TBox and ABox axioms
	 * @return an OrarOntology in the target DL Fragment with TBox axioms are NOT
	 *         normalized
	 */
	public OrarOntology2 getNOTNormalizedOrarOntology(String ontologyFileName);


	/**
	 * @param tboxFileName
	 *            a file for TBox axioms.
	 * @param aboxListFileName
	 *            a file that contains a list of ABox files.
	 * @return an OrarOntology in the target DL Fragment with TBox axioms are
	 *         normalized
	 */
	public OrarOntology2 getNormalizedOrarOntology(String tboxFileName, String aboxListFileName);
	
	/**
	 * @param tboxFileName
	 *            a file for TBox axioms.
	 * @param aboxListFileName
	 *            a file that contains a list of ABox files.
	 * @return an OrarOntology in the target DL Fragment with TBox axioms are NOT
	 *         normalized
	 */
	public OrarOntology2 getNOTNormalizedOrarOntology(String tboxFileName, String aboxListFileName);


	/**
	 * @param ontologyFileName
	 *            a file that contains both TBox and ABox axioms
	 * @return a non-normalized OWLAPI ontology in the target DL fragment.
	 */
	public OWLOntology getOWLAPIOntology(String ontologyFileName);

	/**
	 * @param tboxFileName
	 *            a file for TBox axioms.
	 * @param aboxListFileName
	 *            a file that contains a list of ABox files.
	 * @return a non-normalized OWLAPI ontology in the target DL fragment.
	 */
	public OWLOntology getOWLAPIOntology(String tboxFile, String aboxListFile);
	
	public long getLoadingTime();

}
