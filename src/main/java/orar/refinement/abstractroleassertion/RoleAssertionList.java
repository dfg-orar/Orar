package orar.refinement.abstractroleassertion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

/**
 * A list of role assertions.
 * 
 * @author kien
 *
 */
public class RoleAssertionList {

	private final List<OWLNamedIndividual> subjects;
	private final List<OWLObjectProperty> roles;
	private final List<OWLNamedIndividual> objects;

	public RoleAssertionList() {

		this.subjects = new ArrayList<OWLNamedIndividual>();
		this.roles = new ArrayList<OWLObjectProperty>();
		this.objects = new ArrayList<OWLNamedIndividual>();
	}

	public void addRoleAssertion(OWLNamedIndividual subject, OWLObjectProperty role, OWLNamedIndividual object) {
		this.subjects.add(subject);
		this.roles.add(role);
		this.objects.add(object);

	}

	public int getSize() {

		return this.subjects.size();
	}

	/**
	 * @param index
	 * @return the subject individual in the role assertion at the position
	 *         {@code index}
	 */
	public OWLNamedIndividual getSubject(int index) {
		return this.subjects.get(index);
	}

	/**
	 * @param index
	 * @return the role in the role assertion at the position {@code index}
	 */
	public OWLObjectProperty getRole(int index) {
		return this.roles.get(index);
	}

	/**
	 * @param index
	 * @return the object individual in the role assertion at the position
	 *         {@code index}
	 */
	public OWLNamedIndividual getObject(int index) {
		return this.objects.get(index);
	}

	/**
	 * @return a set of OWLAPI role assertions in this list. This method is used
	 *         only for tetsing
	 */
	public Set<OWLObjectPropertyAssertionAxiom> getSetOfRoleAssertions() {
		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		Set<OWLObjectPropertyAssertionAxiom> roleAssertions = new HashSet<OWLObjectPropertyAssertionAxiom>();
		for (int i = 0; i < getSize(); i++) {
			OWLObjectPropertyAssertionAxiom newAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(getRole(i),
					getSubject(i), getObject(i));
			roleAssertions.add(newAssertion);
		}
		return roleAssertions;
	}

	public List<OWLNamedIndividual> getSubjects() {
		return subjects;
	}

	public List<OWLObjectProperty> getRoles() {
		return roles;
	}

	public List<OWLNamedIndividual> getObjects() {
		return objects;
	}

	public void addAll(RoleAssertionList roleAsesrtionList) {
		this.subjects.addAll(roleAsesrtionList.getSubjects());
		this.roles.addAll(roleAsesrtionList.getRoles());
		this.objects.addAll(roleAsesrtionList.getObjects());
	}
}
