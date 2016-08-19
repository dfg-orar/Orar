package orar.owlconverter;

/**
 * Convert ontologies, whose assertions are stored in turtle syntax, to
 * OWLFunctionalSyntax
 * 
 * @author kien
 *
 */
public interface OWLOntologyConverter {
	/**
	 * convert an ontology <tboxFile,aboxListFile> to an all-in-one ontology in functional syntax.
	 * @param tboxFile a file containing the TBox
	 * @param aboxListFile a text file containing a list of ABoxes files
	 * @param owlFunctionalSyntaxFile a file containing both TBox and ABoxes in OWLFunctionalSyntax
	 */
	public void convertToAllInOneOWLFunctionalSynxtax(String tboxFile, String aboxListFile, String owlFunctionalSyntaxFile);
	/**
	 * convert an ontology in <allinoneOntologyFile>, which contains both TBox and the ABox, to a separated TBox file (in FunctionalSyntax) and an ABox file(in RDF/XML syntax)
	 * @param allinoneOntologyFile
	 * @param tboxFile
	 * @param aboxFileInRDFXML
	 */
	public void convertToSeparatedFiles(String allinoneOntologyFile, String tboxFile, String aboxFileInRDFXML);
}
