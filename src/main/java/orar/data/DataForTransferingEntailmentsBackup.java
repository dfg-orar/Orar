package orar.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.abstraction.PairOfSubjectAndObject;
import orar.type.IndividualType;

/**
 * Data using to transfer entailments from the abstraction to the original ABox.
 * This data is created while generating the abstraction from types.
 * 
 * @author kien
 *
 */
public class DataForTransferingEntailmentsBackup implements DataForTransferringEntailmentInterface {
	private static DataForTransferringEntailmentInterface instance;

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

	private DataForTransferingEntailmentsBackup() {

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

	public static DataForTransferringEntailmentInterface getInstance() {
		if (instance == null) {
			instance = new DataForTransferingEntailmentsBackup();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_XAbstractIndiv_2_Type()
	 */
	@Override
	public Map<OWLNamedIndividual, IndividualType> getMap_XAbstractIndiv_2_Type() {
		return this.xAbstract2TypeMap;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_XAbstractIndiv_2_OriginalIndivs()
	 */
	@Override
	public Map<OWLNamedIndividual, Set<Integer>> getMap_XAbstractIndiv_2_OriginalIndivs() {
		return xAbstract2OriginalIndividualsMap;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_YAbstractIndiv_2_OriginalIndivs()
	 */
	@Override
	public Map<OWLNamedIndividual, Set<Integer>> getMap_YAbstractIndiv_2_OriginalIndivs() {
		return yAbstract2OriginalIndividualsMap;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_ZAbstractIndiv_2_OriginalIndivs()
	 */
	@Override
	public Map<OWLNamedIndividual, Set<Integer>> getMap_ZAbstractIndiv_2_OriginalIndivs() {
		return zAbstract2OriginalIndividualsMap;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_UAbstractIndiv_2_OriginalIndivs()
	 */
	@Override
	public Map<OWLNamedIndividual, Set<Integer>> getMap_UAbstractIndiv_2_OriginalIndivs() {
		return uAbstract2OriginalIndividualsMap;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_XY_2_Role()
	 */
	@Override
	public Map<PairOfSubjectAndObject, OWLObjectProperty> getMap_XY_2_Role() {
		return xyMap2Role;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMap_ZX_2_Role()
	 */
	@Override
	public Map<PairOfSubjectAndObject, OWLObjectProperty> getMap_ZX_2_Role() {
		return zxMap2Role;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMapType_2_Individuals()
	 */
	@Override
	public Map<IndividualType, Set<Integer>> getMapType_2_Individuals() {
		return this.mapType2Individuals;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getxAbstractHavingFunctionalRole()
	 */
	@Override
	public Set<OWLNamedIndividual> getxAbstractHavingFunctionalRole() {
		return xAbstractHavingFunctionalRole;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getzAbstractHavingInverseFunctionalRole()
	 */
	@Override
	public Set<OWLNamedIndividual> getzAbstractHavingInverseFunctionalRole() {
		return zAbstractHavingInverseFunctionalRole;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getMapIndividual2XAbstract()
	 */
	@Override
	public Map<Integer, OWLNamedIndividual> getMapIndividual2XAbstract() {
		return mapIndividual2XAbstract;
	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#getOriginalIndividuals(org.semanticweb.owlapi.model.OWLNamedIndividual)
	 */
	@Override
	public Set<Integer> getOriginalIndividuals(OWLNamedIndividual abstractInd) {

		AbstractDataFactory abstractDataFactory = AbstractDataFactory.getInstance();

		if (abstractDataFactory.getXAbstractIndividuals().contains(abstractInd)) {
			return this.xAbstract2OriginalIndividualsMap.get(abstractInd);
		} else if (abstractDataFactory.getUAbstractIndividuals().contains(abstractInd)) {
			return this.uAbstract2OriginalIndividualsMap.get(abstractInd);
		} else if (abstractDataFactory.getYAbstractIndividuals().contains(abstractInd)) {
			return this.yAbstract2OriginalIndividualsMap.get(abstractInd);
		} else if (abstractDataFactory.getZAbstractIndividuals().contains(abstractInd)) {
			return this.zAbstract2OriginalIndividualsMap.get(abstractInd);
		} else
			return new HashSet<Integer>();

	}

	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#clear()
	 */
	@Override
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
		 * Note to not clear the map from Type --> set of individuals.
		 */
//		this.mapType2Individuals.clear();
//		this.xAbstract2TypeMap.clear();
	}
	/* (non-Javadoc)
	 * @see orar.data.DataForTransferringEntailmentInterface#clearMapOfTypes()
	 */
	@Override
	public void clearMapOfTypes() {
		this.mapType2Individuals.clear();
		this.xAbstract2TypeMap.clear();
	}
}
