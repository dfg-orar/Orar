package orar.sandbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

public class WriteRDFWithJena {

	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		

		Resource subject = model.createResource("http://example.org/alice");
		Resource subjectCopy = model.createResource("http://example.org/alice_alias");
		Property predicate = model.createProperty("http://dbpedia/#love");
		Resource object = model.createResource("http://example.org/wonderland");
		Resource concept = model.createResource("http://example.org/Person");
//
//		subject.addProperty(predicate, object);
//		subject.addProperty(RDF.type, concept);

		
		Statement roleassertion=ResourceFactory.createStatement(subject, predicate, object);
		model.add(roleassertion);
		
		Statement conceptAssertion = ResourceFactory.createStatement(subject, RDF.type, concept);
		model.add(conceptAssertion);
		
		Statement sameasStatement=ResourceFactory.createStatement(subject, OWL.sameAs, subjectCopy);
		model.add(sameasStatement);
		
		File sampleTurtleFile = new File("sampleTurtle.ttl");
		FileOutputStream outPutStream= new FileOutputStream(sampleTurtleFile);
		model.write(System.out, "TURTLE");
		model.write(outPutStream, "TURTLE");
	}

}
