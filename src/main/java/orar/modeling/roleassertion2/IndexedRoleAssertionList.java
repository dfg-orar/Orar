package orar.modeling.roleassertion2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import orar.indexing.IndividualIndexer;

public class IndexedRoleAssertionList {
	private final List<Integer> subjects;
	private final List<OWLObjectProperty> roles;
	private final List<Integer> objects;
	private final IndividualIndexer indexer;

	public IndexedRoleAssertionList() {

		this.subjects = new ArrayList<Integer>();
		this.roles = new ArrayList<OWLObjectProperty>();
		this.objects = new ArrayList<Integer>();
		this.indexer = IndividualIndexer.getInstance();
	}

	public void addRoleAssertion(Integer subject, OWLObjectProperty role, Integer object) {
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
	public Integer getSubject(int index) {
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
	public Integer getObject(int index) {
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
			String subjectString = indexer.getIndividualString(getSubject(i));
			OWLNamedIndividual subject = dataFactory.getOWLNamedIndividual(IRI.create(subjectString));

			String objectString = indexer.getIndividualString(getObject(i));
			OWLNamedIndividual object = dataFactory.getOWLNamedIndividual(IRI.create(objectString));

			OWLObjectPropertyAssertionAxiom newAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(getRole(i),
					subject, object);
			roleAssertions.add(newAssertion);
		}
		return roleAssertions;
	}
	
	/**
	 * @return a set of indexed role assertions in this list. 
	 */
	public Set<IndexedRoleAssertion> getSetOfIndexedRoleAssertions() {
	
		Set<IndexedRoleAssertion> roleAssertions = new HashSet<>();
		for (int i = 0; i < getSize(); i++) {
			
			roleAssertions.add(new IndexedRoleAssertion(getSubject(i), getRole(i), getObject(i)));
		}
		return roleAssertions;
	}

	public List<Integer> getSubjects() {
		return subjects;
	}

	public List<OWLObjectProperty> getRoles() {
		return roles;
	}

	public List<Integer> getObjects() {
		return objects;
	}

	public void addAll(IndexedRoleAssertionList roleAsesrtionList) {
		this.subjects.addAll(roleAsesrtionList.getSubjects());
		this.roles.addAll(roleAsesrtionList.getRoles());
		this.objects.addAll(roleAsesrtionList.getObjects());
	}
}
