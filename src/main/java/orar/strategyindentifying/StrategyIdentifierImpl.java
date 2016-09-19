package orar.strategyindentifying;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

import orar.dlfragmentvalidator.DLConstructor;

public class StrategyIdentifierImpl implements StrategyIdentifier {

	private final Set<OWLAxiom> allAxioms;
	private final Set<DLConstructor> dlConstructors;
	private StrategyName strategyName;
	private boolean strategyHasBenIdentified;

	public StrategyIdentifierImpl(Set<OWLAxiom> allAxiomsIntheNormalizedOntology) {
		this.allAxioms = allAxiomsIntheNormalizedOntology;
		this.strategyHasBenIdentified = false;
		this.dlConstructors = new HashSet<DLConstructor>();
	}

	private void collectConstructos() {
		ConstructorCollectorAxiomVisitor collector = new ConstructorCollectorAxiomVisitor();

		for (OWLAxiom axiom : this.allAxioms) {
			Set<DLConstructor> constructors = axiom.accept(collector);
			if (constructors != null) {
				this.dlConstructors.addAll(axiom.accept(collector));
			}
		}

	}

	private void identifyStrategyName() {
		// collect constructos
		collectConstructos();

		if (isWithinDLLiteExtentions()) {
			this.strategyName = StrategyName.DLLITE_EXTENSION_STRATEGY;
		} else if (isWithinHornSHIF()) {
			this.strategyName = StrategyName.HORN_SHIF_STRATEGY;
		} else
			this.strategyName = StrategyName.HORN_SHOIF_STRATEGY;
		this.strategyHasBenIdentified = true;
	}

	public Set<DLConstructor> getDLConstructors() {
		if (!this.strategyHasBenIdentified) {
			identifyStrategyName();
		}
		return this.dlConstructors;
	}

	public StrategyName getStrategyName() {
		if (!this.strategyHasBenIdentified) {
			identifyStrategyName();
		}
		return this.strategyName;

	}

	private boolean isWithinDLLiteExtentions() {
		if (!this.dlConstructors.contains(DLConstructor.QUALIFIED_EXISTENTIAL_RESTRICTION)
				&& !this.dlConstructors.contains(DLConstructor.UNIVERSAL_RESTRICTION)
				&& !this.dlConstructors.contains(DLConstructor.NOMINAL)
				&& !this.dlConstructors.contains(DLConstructor.HASVALUE)) {
			return true;
		} else
			return false;
	}

	private boolean isWithinHornSHIF() {
		if (!this.dlConstructors.contains(DLConstructor.NOMINAL)
				&& !this.dlConstructors.contains(DLConstructor.HASVALUE)) {
			return true;
		} else
			return false;
	}
}
