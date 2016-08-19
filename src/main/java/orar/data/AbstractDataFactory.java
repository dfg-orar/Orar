package orar.data;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Singleton factory for: abstract individuals, fresh concept names
 * 
 * @author T.Kien Tran
 */
public class AbstractDataFactory {
	private final String IRI_BASE_X = "http://www.af#abstractX";
	private final String IRI_BASE_Y = "http://www.af#abstractY";
	private final String IRI_BASE_Z = "http://www.af#abstractZ";
	private final String IRI_BASE_U = "http://www.af#abstractU";
	private final String IRI_BASE_FRESHCONCEPT = "http://www.af#freshC";
	private static long xCounter;
	private static long yCounter;
	private static long zCounter;
	private static long uCounter;

	private static AbstractDataFactory instance;

	private final Set<OWLNamedIndividual> yAbstractIndividuals;
	private final Set<OWLNamedIndividual> zAbstractIndividuals;
	private final Set<OWLNamedIndividual> xAbstractIndividuals;
	private final Set<OWLNamedIndividual> uAbstractIndividuals;

	OWLOntologyManager manager;
	OWLDataFactory factory;

	private AbstractDataFactory() {
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		yAbstractIndividuals = new HashSet<OWLNamedIndividual>();
		xAbstractIndividuals = new HashSet<OWLNamedIndividual>();
		zAbstractIndividuals = new HashSet<OWLNamedIndividual>();
		uAbstractIndividuals = new HashSet<OWLNamedIndividual>();
		xCounter = 0;
		yCounter = 0;
		zCounter = 0;
		uCounter = 0;

	}

	/**
	 * @return Singleton instance of the factory
	 */
	public static AbstractDataFactory getInstance() {
		if (instance == null)
			instance = new AbstractDataFactory();
		return instance;
	}

	/**
	 * Create abstract an individual representing individual type
	 * 
	 * @return a fresh abstract individual with x-prefix
	 */
	public OWLNamedIndividual createAbstractIndividualX() {
		OWLNamedIndividual x = factory.getOWLNamedIndividual(IRI.create(IRI_BASE_X + ++xCounter));
		xAbstractIndividuals.add(x);

		return x;

	}
	/**
	 * Create abstract an individual representing concept type
	 * 
	 * @return a fresh abstract individual with x-prefix
	 */
	public OWLNamedIndividual createAbstractIndividualU() {
		OWLNamedIndividual u = factory.getOWLNamedIndividual(IRI.create(IRI_BASE_U + ++uCounter));
		uAbstractIndividuals.add(u);

		return u;

	}

	/**
	 * Create abstract an individual representing successors
	 * 
	 * @return a fresh abstract individual with y-prefix
	 */
	public OWLNamedIndividual createAbstractIndividualY() {
		OWLNamedIndividual y = factory.getOWLNamedIndividual(IRI.create(IRI_BASE_Y + ++yCounter));
		yAbstractIndividuals.add(y);

		return y;
	}

	/**
	 * Create abstract an individual representing predecessors
	 * 
	 * @return a fresh abstract individual with z-prefix
	 */
	public OWLNamedIndividual createAbstractIndividualZ() {
		OWLNamedIndividual z = factory.getOWLNamedIndividual(IRI.create(IRI_BASE_Z + ++zCounter));
		zAbstractIndividuals.add(z);

		return z;

	}

	/**
	 * @return the iRI_BASE_X
	 */
	public String getIRI_BASE_X() {
		return IRI_BASE_X;
	}

	/**
	 * @return the iRI_BASE_Y
	 */
	public String getIRI_BASE_Y() {
		return IRI_BASE_Y;
	}

	/**
	 * @return the iRI_BASE_Z
	 */
	public String getIRI_BASE_Z() {
		return IRI_BASE_Z;
	}
	
	/**
	 * @return the iRI_BASE_U
	 */
	public String getIRI_BASE_U() {
		return IRI_BASE_U;
	}

	/**
	 * @return the iRI_BASE_CONCEPT
	 */
	public String getIRI_BASE_FRESHCONCEPT() {
		return IRI_BASE_FRESHCONCEPT;
	}

	public  long getxCounter() {
		return xCounter;
	}

	public  long getyCounter() {
		return yCounter;
	}

	public  long getzCounter() {
		return zCounter;
	}
	
	public  long getuCounter() {
		return uCounter;
	}

	/**
	 * @return A set of predecessors and successors, e.g. y,z
	 */
	public Set<OWLNamedIndividual> getYAbstractIndividuals() {
		return yAbstractIndividuals;
	}

	/**
	 * @return A set of predecessors and successors, e.g. y,z
	 */
	public Set<OWLNamedIndividual> getZAbstractIndividuals() {
		return zAbstractIndividuals;
	}

	// /**
	// * @return A set of predecessors and successors, e.g. y,z
	// */
	// public Set<OWLNamedIndividual> getYZAbstractIndividuals() {
	// return yzAbstractIndividuals;
	// }

	/**
	 * @return A set of abstract individual X
	 */
	public Set<OWLNamedIndividual> getXAbstractIndividuals() {
		return xAbstractIndividuals;
	}

	/**
	 * @return A set of abstract individual U
	 */
	public Set<OWLNamedIndividual> getUAbstractIndividuals() {
		return uAbstractIndividuals;
	}

	
	// /**
	// * @return All abstract individuals genearted during abstraction.
	// */
	// public Set<OWLNamedIndividual> getXYZAbstractIndividuals() {
	//
	// return xyzAbstractIndividuals;
	// }

	public void clear() {
		xAbstractIndividuals.clear();
		yAbstractIndividuals.clear();
		zAbstractIndividuals.clear();
		uAbstractIndividuals.clear();
		xCounter = 0;
		yCounter = 0;
		zCounter = 0;
		uCounter = 0;

	}

}
