package orar.io.aboxstreamreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;


import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDFBase;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

public class JenaMultipleABoxesStreamReader implements ABoxStreamReader {

	private final StreamRDFBase streamRDFReader;
	private final Set<String> aboxList;
	private boolean aboxesAreParsed = false;
	private final static Logger logger = Logger.getLogger(JenaMultipleABoxesStreamReader.class);

	public JenaMultipleABoxesStreamReader(Set<OWLObjectProperty> definedObjectProperties, Set<OWLClass> definedClasses,
			String aboxListFile, OrarOntology2 internalOntology) {

		this.streamRDFReader = new JenaStreamRDF2InternalModel(definedObjectProperties, definedClasses,
				internalOntology);
		this.aboxList = getABoxList(aboxListFile);

	}

	public JenaMultipleABoxesStreamReader(Set<OWLObjectProperty> definedObjectProperties, Set<OWLClass> definedClasses,
			String aboxListFile, OWLOntology owlOntology) {

		this.streamRDFReader = new JenaStreamRDF2OWLAPI(definedObjectProperties, definedClasses, owlOntology);
		this.aboxList = getABoxList(aboxListFile);

	}

	private Set<String> getABoxList(String aboxListFile) {
		Set<String> aboxList = new HashSet<String>();
//		
//		logger.info("Get ABoxe files from: "+aboxListFile);
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
		for (String aboxFile : this.aboxList) {
//			logger.info("ABox " + count++ + ":" + aboxFile);
			RDFDataMgr.parse(this.streamRDFReader, aboxFile);
		}

		this.aboxesAreParsed = true;

	}

}
