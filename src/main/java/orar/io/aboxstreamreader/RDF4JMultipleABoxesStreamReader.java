package orar.io.aboxstreamreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import orar.modeling.ontology2.OrarOntology2;

public class RDF4JMultipleABoxesStreamReader implements ABoxStreamReader {

	private final AbstractRDFHandler streamRDFReader;
	private final Set<String> aboxList;
	private boolean aboxesAreParsed = false;
	private final static Logger logger = Logger.getLogger(RDF4JMultipleABoxesStreamReader.class);

	public RDF4JMultipleABoxesStreamReader(Set<OWLObjectProperty> definedObjectProperties, Set<OWLClass> definedClasses,
			String aboxListFile, OrarOntology2 internalOntology) {

		// this.streamRDFReader = new
		// JenaStreamRDF2InternalModel(definedObjectProperties, definedClasses,
		// internalOntology);

		this.streamRDFReader = new RDF4JStreamRDF2InternalModel(definedObjectProperties, definedClasses,
				internalOntology);
		this.aboxList = getABoxList(aboxListFile);

	}

	public RDF4JMultipleABoxesStreamReader(Set<OWLObjectProperty> definedObjectProperties, Set<OWLClass> definedClasses,
			String aboxListFile, OWLOntology owlOntology) {

		this.streamRDFReader = new RDF4JStreamRDF2OWLAPI(definedObjectProperties, definedClasses, owlOntology);
		this.aboxList = getABoxList(aboxListFile);

	}

	private Set<String> getABoxList(String aboxListFile) {
		Set<String> aboxList = new HashSet<String>();

		File file = new File(aboxListFile);
		try {
			FileInputStream inputStream = new FileInputStream(file);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));

			String eachLine;
			while ((eachLine = buffer.readLine()) != null) {
				/*
				 * ignore comments
				 */
				if (eachLine.startsWith("#"))
					continue;
				String strimLine = eachLine;
				/*
				 * ignore empty lines
				 */
				if (strimLine.trim().equals(""))
					continue;
				aboxList.add(eachLine);

			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return aboxList;
	}

	@Override
	public void readABoxes() {
		int count = 1;

		RDFParser rdfParser =  Rio.createParser(RDFFormat.TURTLE);
		logger.info("OK after creatinG  RDFParse");
		// RDFParser rdfParser = new NTriplesParser();
		rdfParser.setRDFHandler(this.streamRDFReader);
		logger.info("RDF format:" + rdfParser.getRDFFormat().getName());
		
		for (String aboxFile : this.aboxList) {
			logger.info("ABox " + count++ + ":" + aboxFile);
			File file = new File(aboxFile);
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(file);
				rdfParser.parse(inputStream, "");
			} catch (RDFParseException | RDFHandlerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			this.aboxesAreParsed = true;

		}
	}

}
