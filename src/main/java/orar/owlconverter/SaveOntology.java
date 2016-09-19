package orar.owlconverter;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SaveOntology {
public static void main(String[] args) throws OWLOntologyCreationException{
	OWLOntologyManager ontoManager = OWLManager.createOWLOntologyManager();
	String fileName="";
	Object owlOnto = ontoManager.loadOntologyFromOntologyDocument(new File (fileName));
}
}
