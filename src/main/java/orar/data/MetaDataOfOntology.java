package orar.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 * Store some extra information of the ontology:<br>
 * -nominals <br>
 * -nominalconcepts, e.g. concepts generated while normalizing nominals <br>
 * -entailed functional roles <br>
 * -entailed inverse functional roles <br>
 * -entailed role hierarchy.
 * 
 * @author kien
 *
 */
public class MetaDataOfOntology {

	private static MetaDataOfOntology instance;
	private final Set<OWLNamedIndividual> nominals;
	private final Set<OWLClass> nominalConcepts;

	private final Set<OWLObjectProperty> functionalRoles;
	private final Set<OWLObjectProperty> inverseFunctionalRoles;
	private final Set<OWLObjectProperty> transitiveRoles;

	private final Map<OWLObjectProperty, Set<? extends OWLObjectPropertyExpression>> subRoleMap;
	/**
	 * map: role --> its inverses
	 */
	private final Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> inverseRoleMap;

	private MetaDataOfOntology() {
		this.nominalConcepts = new HashSet<OWLClass>();
		this.nominals = new HashSet<OWLNamedIndividual>();
		this.functionalRoles = new HashSet<OWLObjectProperty>();
		this.inverseFunctionalRoles = new HashSet<OWLObjectProperty>();
		this.transitiveRoles = new HashSet<OWLObjectProperty>();
		this.subRoleMap = new HashMap<OWLObjectProperty, Set<? extends OWLObjectPropertyExpression>>();
		this.inverseRoleMap= new HashMap<OWLObjectProperty, Set<OWLObjectPropertyExpression>>();
	}

	public static MetaDataOfOntology getInstance() {
		if (instance == null) {
			instance = new MetaDataOfOntology();
		}
		return instance;
	}

	public Set<OWLNamedIndividual> getNominals() {
		return nominals;
	}

	public Set<OWLClass> getNominalConcepts() {
		return nominalConcepts;
	}

	/**
	 * @return get all (entailed) functional roles (in/by) the ontology.
	 */
	public Set<OWLObjectProperty> getFunctionalRoles() {
		return functionalRoles;
	}

	public Map<OWLObjectProperty, Set<? extends OWLObjectPropertyExpression>> getSubRoleMap() {
		return subRoleMap;
	}

	public Set<OWLObjectProperty> getInverseFunctionalRoles() {
		return inverseFunctionalRoles;
	}

	public Set<OWLObjectProperty> getTransitiveRoles() {
		return transitiveRoles;
	}

	public void clear() {
		this.nominalConcepts.clear();
		this.nominals.clear();
		this.functionalRoles.clear();
		this.inverseFunctionalRoles.clear();
		this.subRoleMap.clear();
		this.transitiveRoles.clear();
		this.inverseRoleMap.clear();
	}

	public Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> getInverseRoleMap() {
		return inverseRoleMap;
	}
}
