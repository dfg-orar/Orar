package orar.type;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class BasicIndividualTypeFactory_UsingWeakHashMap implements BasicIndividualTypeFactory {

	private final Map<IndividualType, WeakReference<IndividualType>> cache;
	private static BasicIndividualTypeFactory_UsingWeakHashMap instance;

	private BasicIndividualTypeFactory_UsingWeakHashMap() {
		this.cache = new WeakHashMap<IndividualType, WeakReference<IndividualType>>();

	}

	public static BasicIndividualTypeFactory_UsingWeakHashMap getInstance() {
		if (instance == null) {
			instance = new BasicIndividualTypeFactory_UsingWeakHashMap();
		}
		return instance;

	}

	@Override
	public IndividualType getIndividualType(Set<OWLClass> atomicConcepts, Set<OWLObjectProperty> preRoles,
			Set<OWLObjectProperty> sucRoles) {
		IndividualType newType = new BasicIndividualType(atomicConcepts, preRoles, sucRoles);

		WeakReference<IndividualType> valueOfNewType = this.cache.get(newType);
		if (valueOfNewType == null) {
			this.cache.put(newType, new WeakReference<IndividualType>(newType));
			return newType;
		}

		return valueOfNewType.get();

	}

	@Override
	public void clear() {
		this.cache.clear();
	}

}
