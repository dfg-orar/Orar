package orar.refinement.abstractroleassertion;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

/**
 * Data structure for entailed abstract role assertions. It contains several
 * kinds of role assertions, for which transferring assertions is different.
 * 
 * @author kien
 *
 */
public class AbstractRoleAssertionBox {

	/**
	 * role assertions of the form R(x,y) , where x is the representative for
	 * combined-type, y its successor.
	 */
	private final RoleAssertionList xyRoleAssertionsForType;

	/**
	 * role assertions of the form R(z,x), where x is the representative for
	 * combined-type, z its predecessor.
	 */
	private final RoleAssertionList zxRoleAssertionsForType;
	/**
	 * role assertions of the form T(u,u), (or T(x,x) in case of Horn-SHIF,
	 * where T is transitive, and u is the representative individual for
	 * concept-type
	 */
	private final RoleAssertionList loopRoleAssertions;
	/**
	 * role assertions of the form R(u,x), where u is the representative for the
	 * concept-type and x is the representative for the combined-type.
	 */
	private final RoleAssertionList uxRoleAssertionsForCTypeAndType;

	private final RoleAssertionList uNominal_RoleAssertions;
	private final RoleAssertionList nominalU_RoleAssertions;

	public AbstractRoleAssertionBox() {
		this.xyRoleAssertionsForType = new RoleAssertionList();
		this.zxRoleAssertionsForType = new RoleAssertionList();
		this.loopRoleAssertions = new RoleAssertionList();
		this.uxRoleAssertionsForCTypeAndType = new RoleAssertionList();

		this.uNominal_RoleAssertions = new RoleAssertionList();
		this.nominalU_RoleAssertions = new RoleAssertionList();
	}

	/**
	 * add a role assertion whose individuals come from a (combine) type, e.g.
	 * assertions of the form R(x,y)
	 * 
	 * @param subject
	 *            any representative of type
	 * @param role
	 *            any role
	 * @param object
	 *            any successor of the representative of type
	 */
	public void addXY_RoleAssertionForType(OWLNamedIndividual subject, OWLObjectProperty role,
			OWLNamedIndividual object) {
		this.xyRoleAssertionsForType.addRoleAssertion(subject, role, object);
	}

	/**
	 * add a role assertion whose individuals come from a (combine) type, e.g.
	 * assertions of the form R(z,x)
	 * 
	 * @param subject
	 *            any predecessor
	 * @param role
	 *            any role
	 * @param object
	 *            any representative of type
	 */
	public void addZX_RoleAssertionForType(OWLNamedIndividual subject, OWLObjectProperty role,
			OWLNamedIndividual object) {
		this.zxRoleAssertionsForType.addRoleAssertion(subject, role, object);
	}

	/**
	 * add a loop role assertion whose individuals come from concept type, e.g.
	 * T(u,u)
	 * 
	 * @param subjectEqualToObject
	 * @param role
	 *            a transitive role
	 */
	public void addLoopRoleAssertion(OWLNamedIndividual subjectEqualToObject, OWLObjectProperty role) {
		this.loopRoleAssertions.addRoleAssertion(subjectEqualToObject, role, subjectEqualToObject);
	}

	/**
	 * add a role assertion in which individuals come from ConceptType and Type,
	 * e.g. R(u,x)
	 * 
	 * @param subject
	 *            representative of ConceptType
	 * @param role
	 *            any role
	 * @param object
	 *            representative of Type
	 */
	public void addUX_RoleAssertionForCTypeAndType(OWLNamedIndividual subject, OWLObjectProperty role,
			OWLNamedIndividual object) {
		this.uxRoleAssertionsForCTypeAndType.addRoleAssertion(subject, role, object);
	}

	public void addUNominal_RoleAssertion(OWLNamedIndividual subject_U, OWLObjectProperty role,
			OWLNamedIndividual nominal) {
		this.uNominal_RoleAssertions.addRoleAssertion(subject_U, role, nominal);
	}

	public void addNominalandU_RoleAssertion(OWLNamedIndividual nominal, OWLObjectProperty role,
			OWLNamedIndividual object_b) {
		this.nominalU_RoleAssertions.addRoleAssertion(nominal, role, object_b);
	}

	public RoleAssertionList getXyRoleAssertionsForType() {
		return xyRoleAssertionsForType;
	}

	public RoleAssertionList getZxRoleAssertionsForType() {
		return zxRoleAssertionsForType;
	}

	public RoleAssertionList getLoopRoleAssertions() {
		return loopRoleAssertions;
	}

	public RoleAssertionList getUxRoleAssertionsForCTypeAndType() {
		return uxRoleAssertionsForCTypeAndType;
	}

	public Set<OWLObjectPropertyAssertionAxiom> getSetOfRoleAssertions() {
		Set<OWLObjectPropertyAssertionAxiom> roleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		roleAssertions.addAll(this.loopRoleAssertions.getSetOfRoleAssertions());
		roleAssertions.addAll(this.uxRoleAssertionsForCTypeAndType.getSetOfRoleAssertions());
		roleAssertions.addAll(this.xyRoleAssertionsForType.getSetOfRoleAssertions());
		roleAssertions.addAll(this.zxRoleAssertionsForType.getSetOfRoleAssertions());
		roleAssertions.addAll(this.uNominal_RoleAssertions.getSetOfRoleAssertions());
		roleAssertions.addAll(this.nominalU_RoleAssertions.getSetOfRoleAssertions());
		return roleAssertions;
	}

	public void addAll(AbstractRoleAssertionBox abstractRoleAssertionBox) {
		this.loopRoleAssertions.addAll(abstractRoleAssertionBox.getLoopRoleAssertions());
		this.uxRoleAssertionsForCTypeAndType.addAll(abstractRoleAssertionBox.getUxRoleAssertionsForCTypeAndType());
		this.xyRoleAssertionsForType.addAll(abstractRoleAssertionBox.getXyRoleAssertionsForType());
		this.zxRoleAssertionsForType.addAll(abstractRoleAssertionBox.getZxRoleAssertionsForType());
		this.uNominal_RoleAssertions.addAll(abstractRoleAssertionBox.get_UandNominal_RoleAssertions());
		this.nominalU_RoleAssertions.addAll(abstractRoleAssertionBox.get_NominalAndU_RoleAssertions());
	}

	@Override
	public String toString() {
		return "AbstractRoleAssertionBox [xyRoleAssertionsForType=" + xyRoleAssertionsForType
				+ ", zxRoleAssertionsForType=" + zxRoleAssertionsForType + ", loopRoleAssertions=" + loopRoleAssertions
				+ ", uxRoleAssertionsForCTypeAndType=" + uxRoleAssertionsForCTypeAndType
				+ ", uNominal_RoleAssertionsForCTypeAndType=" + uNominal_RoleAssertions
				+ ", nominalU_RoleAssertionsForCTypeAndType=" + nominalU_RoleAssertions + "]";
	}

	public int getSize() {
		return this.loopRoleAssertions.getSize() + this.uxRoleAssertionsForCTypeAndType.getSize()
				+ this.xyRoleAssertionsForType.getSize() + this.zxRoleAssertionsForType.getSize()
				+ this.uNominal_RoleAssertions.getSize() + this.nominalU_RoleAssertions.getSize();
	}

	public RoleAssertionList get_UandNominal_RoleAssertions() {
		return uNominal_RoleAssertions;
	}

	public RoleAssertionList get_NominalAndU_RoleAssertions() {
		return nominalU_RoleAssertions;
	}

}
