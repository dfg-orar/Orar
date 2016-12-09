package orar.abstraction.DLLiteH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import orar.abstraction.AbstractionGenerator;
import orar.abstraction.RoleAssertionBetweenRepresentativesXGenerator;
import orar.data.AbstractDataFactory;
import orar.data.DataForTransferingEntailments;
import orar.data.DataForTransferringEntailmentInterface;
import orar.data.MetaDataOfOntology;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.type.IndividualType;

public class DLLiteR_AbstractionGenerator implements AbstractionGenerator {
	// private final Logger logger =
	// Logger.getLogger(DLiteR_AbstractionGenerator.class);
	protected final DataForTransferringEntailmentInterface sharedMap;
	protected final MetaDataOfOntology metaDataOfOntology;

	protected final OWLOntologyManager manager;
	protected final OWLDataFactory owlDataFactory;
	protected final AbstractDataFactory abstractDataFactory;

	// private final Map<OWLNamedIndividual, OWLNamedIndividual>
	// mapIndividual2XAbstract;
	/*
	 * 
	 */
	protected final OrarOntology2 orarOntology;
	protected final Map<IndividualType, Set<Integer>> typeMap2Individuals;

	public DLLiteR_AbstractionGenerator(OrarOntology2 orarOntology,
			Map<IndividualType, Set<Integer>> typeMap2Individuals) {
		this.sharedMap = DataForTransferingEntailments.getInstance();
		this.metaDataOfOntology = MetaDataOfOntology.getInstance();
		this.manager = OWLManager.createOWLOntologyManager();
		this.owlDataFactory = OWLManager.getOWLDataFactory();
		this.abstractDataFactory = AbstractDataFactory.getInstance();

		this.orarOntology = orarOntology;
		this.typeMap2Individuals = typeMap2Individuals;

		// this.mapIndividual2XAbstract= new HashMap<>();

	}

	@Override
	public List<OWLOntology> getAbstractOntologies(int numberOfTypesPerAbstraction) {
		/*
		 * In case of DL-LiteR, we connect representatives and thus we don't
		 * split the abstract ABox.
		 */
		List<OWLOntology> singletonList = new ArrayList<OWLOntology>();
		singletonList.add(getAbstractOntology());
		return singletonList;
	}

	@Override
	public OWLOntology getAbstractOntology() {
		try {
			OWLOntology abstractOntology = manager.createOntology();
			Set<OWLAxiom> aBoxAssertions = new HashSet<OWLAxiom>();
			Set<IndividualType> types = this.typeMap2Individuals.keySet();
			for (IndividualType type : types) {
				/*
				 * get abstraction assertions
				 */
				aBoxAssertions.addAll(generateAssertions(type));

			}

			RoleAssertionBetweenRepresentativesXGenerator roleGenerator = new RoleAssertionBetweenRepresentativesXGenerator(
					this.orarOntology);
			aBoxAssertions.addAll(roleGenerator.getRoleAssertionBetweenRepresentativesX());
			manager.addAxioms(abstractOntology, aBoxAssertions);
			manager.addAxioms(abstractOntology, orarOntology.getTBoxAxioms());
			/*
			 * clear map from original individuals --> abstractX
			 */
			this.sharedMap.getMapIndividual2XAbstract().clear();
			return abstractOntology;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		}

	}

	private Set<OWLAxiom> generateAssertions(IndividualType type) {
		Set<OWLAxiom> abstractAssertions = new HashSet<OWLAxiom>();
		/*
		 * create x
		 */
		OWLNamedIndividual x = abstractDataFactory.createAbstractIndividualX();
		/*
		 * map X to original individuals.
		 */
		Set<Integer> originalIndsForThisType = this.typeMap2Individuals.get(type);
		sharedMap.getMap_XAbstractIndiv_2_OriginalIndivs().put(x, originalIndsForThisType);
		/*
		 * map original individual to x; for computing role assertions between
		 * x-individuals
		 */
		for (Integer originIndiv : originalIndsForThisType) {
			this.sharedMap.getMapIndividual2XAbstract().put(originIndiv, x);
		}

		/*
		 * create abstract class assertions for x
		 */
		abstractAssertions.addAll(getConceptAssertions(x, type));

		// /*
		// * create succRole assertions for x, we dont need create preRole
		// * assertions for x because predecessor of x will do the job. For
		// * example R(x1,x) will be added when we create succRole assertions
		// for
		// * x1.
		// */
		// abstractAssertions.addAll(getSuccessorRoleAssertions(x, type));

		return abstractAssertions;
	}

	
	/**
	 * @param x
	 * @param type
	 * @return concept assertions x according to its type
	 */
	protected Set<OWLAxiom> getConceptAssertions(OWLNamedIndividual x, IndividualType type) {

		Set<OWLAxiom> classAssertions = new HashSet<OWLAxiom>();

		for (OWLClass conceptName : type.getConcepts()) {
			OWLClassAssertionAxiom assertion = owlDataFactory.getOWLClassAssertionAxiom(conceptName, x);
			classAssertions.add(assertion);
		}

		return classAssertions;
	}
}
