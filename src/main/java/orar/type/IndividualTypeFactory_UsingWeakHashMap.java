package orar.type;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class IndividualTypeFactory_UsingWeakHashMap implements IndividualTypeFactory {

	private final Map<IndividualType, WeakReference<IndividualType>> cache;
	private static IndividualTypeFactory_UsingWeakHashMap instance;

	private IndividualTypeFactory_UsingWeakHashMap() {
		this.cache = new WeakHashMap<IndividualType, WeakReference<IndividualType>>();

	}

	public static IndividualTypeFactory_UsingWeakHashMap getInstance() {
		if (instance == null) {
			instance = new IndividualTypeFactory_UsingWeakHashMap();
		}
		return instance;

	}

	@Override
	public IndividualType getIndividualType(Set<OWLClass> atomicConcepts, Set<OWLObjectProperty> preRoles,
			Set<OWLObjectProperty> sucRoles) {
		IndividualType newType = new BasicIndividualType(new HashSet<>(atomicConcepts), preRoles, sucRoles);

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

	@Override
	public void remove(IndividualType indivType) {
		this.cache.remove(indivType);	
	}

}
