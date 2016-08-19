package orar.normalization;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import orar.config.Configuration;
import orar.config.LogInfo;
import orar.data.NormalizationDataFactory;
import orar.data.MetaDataOfOntology;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public abstract class NormalizerTemplate implements Normalizer {
	protected Stack<OWLSubClassOfAxiom> subClassAxiomStack;
	protected final Set<OWLAxiom> normalizedSubClassAxioms;
	protected final OWLOntology inputOntology;
	protected OWLOntology normalizedOntology;

	protected final Set<OWLAxiom> tboxAxiomsByNormalizingNominals;
	protected final Set<OWLAxiom> aboxAxiomsByNomalizingNominals;

	protected final Set<OWLAxiom> axiomsByNormalizingNominals;
	protected static Logger logger = Logger.getLogger(NormalizerTemplate.class);
	protected int numberOfNormalizedAxioms;
	protected SubClassNormalizer subClassNormalizer;
	protected SuperClassNormalizer superClassNormalizer;

	protected final OWLDataFactory owlDataFactory;

	protected boolean isNormalized;

	protected final Configuration config;

	public NormalizerTemplate(OWLOntology inputOntology) {
		this.config = Configuration.getInstance();
		this.inputOntology = inputOntology;
		this.subClassAxiomStack = new Stack<OWLSubClassOfAxiom>();
		this.normalizedSubClassAxioms = new HashSet<OWLAxiom>();

		this.tboxAxiomsByNormalizingNominals = new HashSet<OWLAxiom>();
		this.aboxAxiomsByNomalizingNominals = new HashSet<OWLAxiom>();
		this.numberOfNormalizedAxioms = 0;
		this.axiomsByNormalizingNominals = new HashSet<OWLAxiom>();
		// this.subClassNormalizer = new ALCHOISubClassNormalizer(
		// subClassAxiomStack);
		// this.superClassNormalizer = new ALCHOISuperClassNormalizer(
		// subClassAxiomStack);
		//
		// this.superClassNormalizer.setSubClassNormalizer(subClassNormalizer);
		// this.subClassNormalizer.setSuperClassNormalizer(superClassNormalizer);
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		isNormalized = false;
		initSubClassAndSuperClassNormalizers();
	}

	/**
	 * Initialize Sub/SuperClassNormalizer depending on Dl language
	 */
	public abstract void initSubClassAndSuperClassNormalizers();

	private void printSize(OWLOntology ont) {
		logger.info("Number of individuals: " + ont.getIndividualsInSignature(true).size());
		logger.info("TBox size: " + ont.getTBoxAxioms(true).size());
		logger.info("RBox size: " + ont.getRBoxAxioms(true).size());
		logger.info("ABox size: " + ont.getABoxAxioms(true).size());
		logger.info("ABox class assertions: " + ont.getAxioms(AxiomType.CLASS_ASSERTION, true).size());
		logger.info("ABox property assertions: " + ont.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, true).size());
	}

	/**
	 * Put all SubClassOfAxioms to the stack
	 */
	private void putAllSubClassAxiomsInToStack() {
		Set<OWLAxiom> inputTBoxAxioms = inputOntology.getTBoxAxioms(true);
		for (OWLAxiom axiom : inputTBoxAxioms) {
			if (axiom instanceof OWLSubClassOfAxiom) {
				this.subClassAxiomStack.push((OWLSubClassOfAxiom) axiom);
			} else if (axiom instanceof OWLEquivalentClassesAxiom) {
				OWLEquivalentClassesAxiom equivAxiom = (OWLEquivalentClassesAxiom) axiom;
				for (OWLSubClassOfAxiom subAxiom : equivAxiom.asOWLSubClassOfAxioms()) {
					this.subClassAxiomStack.push(subAxiom);
				}
			} else {
				this.normalizedSubClassAxioms.add(axiom);
			}
		}

	}

	private void normalizeSubClassAxioms() {
		while (!subClassAxiomStack.isEmpty()) {
			OWLSubClassOfAxiom subClassAxiom = subClassAxiomStack.pop();

			// logger.info("Next axiom:" + subClassAxiom);
			/*
			 * quick check
			 */
			if (isInNormalForm(subClassAxiom)) {
				this.normalizedSubClassAxioms.add(subClassAxiom);
			} else {

				OWLClassExpression subClass = subClassAxiom.getSubClass();

				OWLClassExpression superClass = subClassAxiom.getSuperClass();
				if (subClass instanceof OWLObjectOneOf && superClass instanceof OWLClass) {
					Set<OWLNamedIndividual> individuals = ((OWLObjectOneOf) subClass).getIndividualsInSignature();
					for (OWLNamedIndividual ind : individuals) {
						OWLClassAssertionAxiom conceptAssertion = this.owlDataFactory
								.getOWLClassAssertionAxiom(superClass, ind);
						this.aboxAxiomsByNomalizingNominals.add(conceptAssertion);
					}

				} else if (subClass instanceof OWLObjectHasValue && superClass instanceof OWLClass) {
					this.normalizedSubClassAxioms.add(subClassAxiom);
				} else

				if (superClass instanceof OWLClass && subClass instanceof OWLObjectSomeValuesFrom) {
					OWLObjectSomeValuesFrom someValueFrom = (OWLObjectSomeValuesFrom) subClass;
					OWLClassExpression filler = someValueFrom.getFiller();
					// get nominals if there are some
					if (filler instanceof OWLObjectOneOf) {
						Set<OWLIndividual> inds = ((OWLObjectOneOf) filler).getIndividuals();
						for (OWLIndividual ind : inds) {
							MetaDataOfOntology.getInstance().getNominals().add(ind.asOWLNamedIndividual());
						}

					}

					OWLClassExpression normalizedFiller = filler.accept(subClassNormalizer);

					if (config.getLogInfos().contains(LogInfo.NORMALIZATION_INFO)) {
						if (!normalizedFiller.equals(filler)) {
							logger.info("Axiom has been nomalized:" + subClassAxiom);
						}
					}

					OWLObjectSomeValuesFrom normalizedSomeValueFrom = owlDataFactory
							.getOWLObjectSomeValuesFrom(someValueFrom.getProperty(), normalizedFiller);

					this.normalizedSubClassAxioms
							.add(owlDataFactory.getOWLSubClassOfAxiom(normalizedSomeValueFrom, superClass));
				} else {

					OWLClassExpression normSubClass = subClass.accept(subClassNormalizer);
					OWLClassExpression normSuperClass = superClass.accept(superClassNormalizer);
					if (normSubClass != null && normSuperClass != null) {
						if (!subClass.equals(normSubClass) || !superClass.equals(normSuperClass)) {
							numberOfNormalizedAxioms++;

							if (config.getLogInfos().contains(LogInfo.NORMALIZATION_INFO)) {

								logger.info("Axiom has been nomalized:" + subClassAxiom);
							}
						}
						// if (numberOfNormalizedAxioms < 10) {
						OWLSubClassOfAxiom normSubClassAxiom = OWLManager.getOWLDataFactory()
								.getOWLSubClassOfAxiom(normSubClass, normSuperClass);
						this.normalizedSubClassAxioms.add(normSubClassAxiom);
					}
				}
			}
		}
	}

	/*
	 * quick check for: A SubClassOf B; \exists R.A SubClassOf B, where A, B are
	 * atomic concepts.
	 */
	private boolean isInNormalForm(OWLSubClassOfAxiom subClassAxiom) {
		OWLClassExpression subClass = subClassAxiom.getSubClass();
		OWLClassExpression superClass = subClassAxiom.getSuperClass();
		boolean isInNormalForm = false;

		if (subClass instanceof OWLClass && superClass instanceof OWLClass) {
			isInNormalForm = true;
		} else if (superClass instanceof OWLClass && subClass instanceof OWLObjectSomeValuesFrom) {
			OWLObjectSomeValuesFrom subClassSomeValueFrom = (OWLObjectSomeValuesFrom) subClass;
			if (subClassSomeValueFrom.getFiller() instanceof OWLClass) {
				isInNormalForm = true;
			}
		}
		return isInNormalForm;
	}

	/**
	 * Normalize TBox Axioms. Nominals are collected during nomalization. If the
	 * TBox and ABox are not mixed, we could collect nominals by reading all
	 * individuals from TBox.
	 */
	private void normalizeOntology() {
		if (config.getLogInfos().contains(LogInfo.NORMALIZATION_INFO)) {
			logger.info("Input ontology size:");
			printSize(inputOntology);
		}
		logger.info("Normalizing the input ontology...");
		putAllSubClassAxiomsInToStack();
		normalizeSubClassAxioms();
		/*
		 * Since nominals are collected during normalization of SubClass axioms.
		 * We should normalize SubClass axioms before normalizing nominals
		 */
		normalizeNominalsInTBox();
		nomalizeNominalsInABox();
		createNormalizedOntology();
		logger.info("Finished normalizing TBox of the input ontology.");

		if (config.getLogInfos().contains(LogInfo.NORMALIZATION_INFO)) {
			logger.info("Normalized Ontology size:");
			printSize(normalizedOntology);
			logger.info("Number of normalized axioms:" + numberOfNormalizedAxioms);
		}

		this.isNormalized = true;
	}

	/**
	 * For each nominal {o}, generate: 1) generate No(o) and add it to the ABox
	 * of the normalized ontology 2) generate No SubClassOf {o} and add it to
	 * the TBox of the normalized ontology.
	 * 
	 */
	private void normalizeNominalsInTBox() {
		Set<OWLNamedIndividual> nominals = MetaDataOfOntology.getInstance().getNominals();

		for (OWLNamedIndividual nominal : nominals) {
			OWLClass nominalConcept = NormalizationDataFactory.getInstance().getFreshOWLClassForNominal(nominal);
			MetaDataOfOntology.getInstance().getNominalConcepts().add(nominalConcept);
			OWLClassAssertionAxiom newAssertion = owlDataFactory.getOWLClassAssertionAxiom(nominalConcept, nominal);
			this.aboxAxiomsByNomalizingNominals.add(newAssertion);

			/*
			 * create {nominal}
			 */
			Set<OWLNamedIndividual> nominalSet = new HashSet<OWLNamedIndividual>();
			nominalSet.add(nominal);
			OWLObjectOneOf nominalClass = owlDataFactory.getOWLObjectOneOf(nominalSet);
			OWLEquivalentClassesAxiom newSubClass = owlDataFactory.getOWLEquivalentClassesAxiom(nominalConcept,
					nominalClass);
			// OWLSubClassOfAxiom newSubClass =
			// dataFactory.getOWLSubClassOfAxiom(
			// newClass, nominalClass);
			this.tboxAxiomsByNormalizingNominals.add(newSubClass);
			// this.axiomsByNormalizingNominals.add(newSubClass);

		}
	}

	private void nomalizeNominalsInABox() {
		Set<OWLAxiom> individualAxioms = new HashSet<OWLAxiom>();
		individualAxioms.addAll(inputOntology.getAxioms(AxiomType.SAME_INDIVIDUAL, true));

		individualAxioms.addAll(inputOntology.getAxioms(AxiomType.DIFFERENT_INDIVIDUALS, true));

		ABoxNormalizer aboxNormalizer = new ABoxNormalizer();

		for (OWLAxiom axiom : individualAxioms) {
			axiom.accept(aboxNormalizer);
		}
		this.tboxAxiomsByNormalizingNominals.addAll(aboxNormalizer.getAdditionalAxioms());

	}

	private void createNormalizedOntology() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntologyID newID = new OWLOntologyID(IRI.create("http://www.uniulm.ki/ontology/normalizedalchoi"));

			this.normalizedOntology = OWLManager.createOWLOntologyManager().createOntology(newID);
			// add declarations
			manager.addAxioms(normalizedOntology, inputOntology.getAxioms(AxiomType.DECLARATION));
			// add TBOx
			manager.addAxioms(normalizedOntology, normalizedSubClassAxioms);
			manager.addAxioms(normalizedOntology, tboxAxiomsByNormalizingNominals);

			// add RBox
			Set<OWLAxiom> RBoxAxiom = this.inputOntology.getRBoxAxioms(true);
			manager.addAxioms(normalizedOntology, RBoxAxiom);

			// add ABox
			Set<OWLAxiom> ABoxAxiom = this.inputOntology.getABoxAxioms(true);
			manager.addAxioms(normalizedOntology, ABoxAxiom);
			manager.addAxioms(normalizedOntology, aboxAxiomsByNomalizingNominals);

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		if (config.getLogInfos().contains(LogInfo.NORMALIZATION_INFO)) {
			logger.info("Number of new ConceptNames generated during the normalizaton:"
					+ NormalizationDataFactory.getInstance().getCount());
			logger.info("Normalized TBox size: " + normalizedOntology.getTBoxAxioms(true).size());
			logger.info("Normalized RBox size: " + normalizedOntology.getRBoxAxioms(true).size());
			logger.info("Normalized ABox size: " + normalizedOntology.getABoxAxioms(true).size());
		}
	}

	protected OWLClassExpression getNormalizedSuperConcept(OWLClassExpression superClass) {
		return null;
	}

	protected OWLClassExpression getNormalizedSubConcept(OWLClassExpression subClass) {
		return null;
	}

	public Set<OWLAxiom> getNormalizedTBoxAxioms() {
		return normalizedSubClassAxioms;
	}

	public OWLOntology getInputOntology() {
		return inputOntology;
	}

	public OWLOntology getNormalizedOntology() {
		if (!isNormalized) {
			normalizeOntology();
		}
		return normalizedOntology;
	}

	public int getNumberOfNormalizedAxioms() {
		return numberOfNormalizedAxioms;
	}

}
