package orar.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.abstraction.PairOfSubjectAndObject;
import orar.type.IndividualType;
import orar.util.PrintingHelper;

/**
 * Data using to transfer entailments from the abstraction to the original ABox.
 * This data is created while generating the abstraction from types.
 * 
 * @author kien
 *
 */
public class DataForTransferingEntailments_Increment implements DataForTransferringEntailmentInterface {
	private static DataForTransferingEntailments_Increment instance;
	private static Logger logger;
	private final Map<IndividualType, Set<Integer>> mapType2Individuals;

	private final Map<Integer, OWLNamedIndividual> mapIndividual2XAbstract;
	/*
	 * Maps: x/y/z --> original individuals. x,y,z are abstract individuals for
	 * combined-type.
	 */
	private final Map<OWLNamedIndividual, IndividualType> xAbstract2TypeMap;
	private final Map<OWLNamedIndividual, Set<Integer>> xAbstract2OriginalIndividualsMap;
	private final Map<OWLNamedIndividual, Set<Integer>> yAbstract2OriginalIndividualsMap;
	private final Map<OWLNamedIndividual, Set<Integer>> zAbstract2OriginalIndividualsMap;

	/*
	 * Map: u --> original individuals. u is the abstract individual for
	 * concept-type)
	 */
	private final Map<OWLNamedIndividual, Set<Integer>> uAbstract2OriginalIndividualsMap;
	/*
	 * Map: (x,y) --> r (functional role) in the abstract abox
	 */
	private final Map<PairOfSubjectAndObject, OWLObjectProperty> xyMap2Role;
	/*
	 * Map: (z,x) ---> r (inverse functional role) in the abstract abox
	 */
	private final Map<PairOfSubjectAndObject, OWLObjectProperty> zxMap2Role;

	/*
	 * A set of x whose type contains functional roles
	 */
	private final Set<OWLNamedIndividual> xAbstractHavingFunctionalRole;

	/*
	 * A set of z whose type contains inverse functional roles.
	 */
	private final Set<OWLNamedIndividual> zAbstractHavingInverseFunctionalRole;

	private DataForTransferingEntailments_Increment() {

		this.logger = Logger.getLogger(DataForTransferingEntailments_Increment.class);

		this.xAbstract2TypeMap = new HashMap<>();
		this.xAbstract2OriginalIndividualsMap = new HashMap<OWLNamedIndividual, Set<Integer>>();
		this.yAbstract2OriginalIndividualsMap = new HashMap<OWLNamedIndividual, Set<Integer>>();
		this.zAbstract2OriginalIndividualsMap = new HashMap<OWLNamedIndividual, Set<Integer>>();

		this.uAbstract2OriginalIndividualsMap = new HashMap<OWLNamedIndividual, Set<Integer>>();

		this.xyMap2Role = new HashMap<PairOfSubjectAndObject, OWLObjectProperty>();
		this.zxMap2Role = new HashMap<PairOfSubjectAndObject, OWLObjectProperty>();

		this.xAbstractHavingFunctionalRole = new HashSet<OWLNamedIndividual>();
		this.zAbstractHavingInverseFunctionalRole = new HashSet<OWLNamedIndividual>();

		this.mapIndividual2XAbstract = new HashMap<Integer, OWLNamedIndividual>();

		this.mapType2Individuals = new HashMap<>();

	}

	public static DataForTransferingEntailments_Increment getInstance() {
		if (instance == null) {
			instance = new DataForTransferingEntailments_Increment();
		}
		return instance;
	}

	public Map<OWLNamedIndividual, IndividualType> getMap_XAbstractIndiv_2_Type() {
		return this.xAbstract2TypeMap;
	}

	public Map<OWLNamedIndividual, Set<Integer>> getMap_XAbstractIndiv_2_OriginalIndivs() {
//		logger.error("Unsupported method");
//		System.exit(1);
//		return null;
		return this.xAbstract2OriginalIndividualsMap;
	}

	public Map<OWLNamedIndividual, Set<Integer>> getMap_YAbstractIndiv_2_OriginalIndivs() {
		return yAbstract2OriginalIndividualsMap;
	}

	public Map<OWLNamedIndividual, Set<Integer>> getMap_ZAbstractIndiv_2_OriginalIndivs() {
		return zAbstract2OriginalIndividualsMap;
	}

	public Map<OWLNamedIndividual, Set<Integer>> getMap_UAbstractIndiv_2_OriginalIndivs() {
		return uAbstract2OriginalIndividualsMap;
	}

	public Map<PairOfSubjectAndObject, OWLObjectProperty> getMap_XY_2_Role() {
		return xyMap2Role;
	}

	public Map<PairOfSubjectAndObject, OWLObjectProperty> getMap_ZX_2_Role() {
		return zxMap2Role;
	}

	public Map<IndividualType, Set<Integer>> getMapType_2_Individuals() {
		return this.mapType2Individuals;
	}

	/**
	 * @return a set of abstract indiv x whose type contains functional roles.
	 */
	public Set<OWLNamedIndividual> getxAbstractHavingFunctionalRole() {
		return xAbstractHavingFunctionalRole;
	}

	/**
	 * @return a set of abstract indiv z whose type contains inverse functional
	 *         roles.
	 */
	public Set<OWLNamedIndividual> getzAbstractHavingInverseFunctionalRole() {
		return zAbstractHavingInverseFunctionalRole;
	}

	public Map<Integer, OWLNamedIndividual> getMapIndividual2XAbstract() {
		return mapIndividual2XAbstract;
	}

	/**
	 * @param abstractInd
	 * @return a set of original individuals for which the abstractInd
	 *         represents.<b> Note </b> that changing in this set will affect
	 *         the mapping.
	 */
	public Set<Integer> getOriginalIndividuals(OWLNamedIndividual abstractInd) {

		AbstractDataFactory abstractDataFactory = AbstractDataFactory.getInstance();
		if (abstractDataFactory.getXAbstractIndividuals().contains(abstractInd)) {
			return this.xAbstract2OriginalIndividualsMap.get(abstractInd);
//		if (abstractDataFactory.getXAbstractIndividuals().contains(abstractInd)) {
//			IndividualType type = this.xAbstract2TypeMap.get(abstractInd);
//			Set<Integer> indivs = this.mapType2Individuals.get(type);
//			if (indivs==null){
//				logger.info("***Debug getting original indivs for:***"+abstractInd);
//				logger.info("Map X--->Type:");
//				PrintingHelper.printMap(this.xAbstract2TypeMap);
//				logger.info("Map Type--->Original Indivs:");
//				PrintingHelper.printMap(this.mapType2Individuals);
//			}
//			return indivs;
		} else if (abstractDataFactory.getUAbstractIndividuals().contains(abstractInd)) {
			return this.uAbstract2OriginalIndividualsMap.get(abstractInd);
		} else if (abstractDataFactory.getYAbstractIndividuals().contains(abstractInd)) {
			return this.yAbstract2OriginalIndividualsMap.get(abstractInd);
		} else if (abstractDataFactory.getZAbstractIndividuals().contains(abstractInd)) {
			return this.zAbstract2OriginalIndividualsMap.get(abstractInd);
		} else
			return new HashSet<Integer>();

	}

	/**
	 * Clear all maps.
	 */
	public void clear() {

		this.xAbstract2OriginalIndividualsMap.clear();
		this.yAbstract2OriginalIndividualsMap.clear();
		this.zAbstract2OriginalIndividualsMap.clear();

		this.uAbstract2OriginalIndividualsMap.clear();

		this.xyMap2Role.clear();
		this.zxMap2Role.clear();

		this.xAbstractHavingFunctionalRole.clear();
		this.zAbstractHavingInverseFunctionalRole.clear();

		this.mapIndividual2XAbstract.clear();

		/*
		 * Note to not clear the map from Type --> set of individuals. and from
		 * abstractX --> type
		 */
		// this.mapType2Individuals.clear();
		// this.xAbstract2TypeMap.clear();
	}

	@Override
	public void clearMapOfTypes() {
		this.mapType2Individuals.clear();
		this.xAbstract2TypeMap.clear();
	}
}
