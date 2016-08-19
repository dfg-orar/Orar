package orar.rolereasoning;

import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/**
 * This class computes: 1.Role hierarchy 2.(inverse)functional roles.
 * 
 * @author kien
 *
 */
public interface RoleReasoner {
	/**
	 * compute role hierarchy, (inverse)functional roles, and transitive roles
	 */
	public void doReasoning();

	/**
	 * Should be called after calling {@link #doReasoning()}
	 * 
	 * @return a set of atomic functional roles
	 */
	public Set<OWLObjectProperty> getFunctionalRoles();

	/**
	 * Should be called after calling {@link #doReasoning()}
	 * 
	 * @return a set of atomic inverse-functional roles
	 */
	public Set<OWLObjectProperty> getInverseFunctionalRoles();

	/**
	 * @return a role hierarchy as a map: an atomic role ---> its super-roles,<b>Excluding</b> itself.
	 */
	public Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> getRoleHierarchyAsMap();

	/**
	 * @return a set of (entailed) transitive atomic roles
	 */
	public Set<OWLObjectProperty> getTransitiveRoles();
	
//	public Map<OWLObjectProperty, Set<OWLObjectPropertyExpression>> getInverseRoleMap();

}
