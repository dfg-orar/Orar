package orar.util;

import java.io.File;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OntologyComparator {
	public static void main(String[] args) throws OWLOntologyCreationException {
		String ontologyPath1 = "/Users/kien/Downloads/Orar01/tutorial/amaterizliedConcept.owl.xml";
		String ontologyPath2 = "/Users/kien/Downloads/Orar01/tutorial/resultHermit3.owl.xml";
		Set<OWLClassAssertionAxiom> set1 = getABox(ontologyPath1);
		Set<OWLClassAssertionAxiom> set2 = getABox(ontologyPath2);

		set2.removeAll(set1);
		PrintingHelper.printSet(set2);

	}

	private static Set<OWLClassAssertionAxiom> getABox(String ontoPath) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ont1 = manager.loadOntologyFromOntologyDocument(new File(ontoPath));
		Set<OWLClassAssertionAxiom> abox = ont1.getAxioms(AxiomType.CLASS_ASSERTION);
		return abox;
	}
}
