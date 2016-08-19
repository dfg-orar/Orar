package orar.dlfragmentvalidator.ALCHOIF;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

import orar.dlfragmentvalidator.DLConstructor;
import orar.dlfragmentvalidator.ValidatorDataFactory;

public class ALCHOIF_SubClass_Validator implements OWLClassExpressionVisitorEx<OWLClassExpression> {
	protected final OWLDataFactory owlDataFactory;
	protected final ValidatorDataFactory profilingFactory;
	private final Set<DLConstructor> dlConstructorsInInputOntology;
	private final Set<DLConstructor> dlConstructorsInValidatedOntology;
	private int cardinalityCount = 0;

	public ALCHOIF_SubClass_Validator() {
		owlDataFactory = OWLManager.getOWLDataFactory();
		profilingFactory = ValidatorDataFactory.getInstance();
		this.dlConstructorsInInputOntology = new HashSet<DLConstructor>();
		this.dlConstructorsInValidatedOntology=new HashSet<DLConstructor>();
	}

	public Set<DLConstructor> getDlConstructorsInInputOntology() {
		return dlConstructorsInInputOntology;
	}
	public Set<DLConstructor> getDlConstructorsInValidatedOntology() {
		return dlConstructorsInValidatedOntology;
	}

	public int getNumberOfCardinalityAxioms() {
		return this.cardinalityCount;
	}

	@Override
	public OWLClassExpression visit(OWLClass ce) {
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLObjectIntersectionOf ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.CONJUNCTION);
		this.dlConstructorsInValidatedOntology.add(DLConstructor.CONJUNCTION);
		Set<OWLClassExpression> operands = ce.getOperands();
		boolean violated = false;
		for (OWLClassExpression operand : operands) {
			OWLClassExpression profiledOperand = operand.accept(this);
			if (profiledOperand == null) {
				violated = true;
				break;
			}

		}

		if (!violated) {
			return ce;
		} else
			return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectUnionOf ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.Horn_DISJUNCTION);
		Set<OWLClassExpression> operands = ce.getOperands();
		boolean violated = false;
		for (OWLClassExpression operand : operands) {
			OWLClassExpression profiledOperand = operand.accept(this);
			if (profiledOperand == null) {
				violated = true;
				break;
			}
		}

		if (!violated) {
			this.dlConstructorsInValidatedOntology.add(DLConstructor.Horn_DISJUNCTION);
			return ce;
		}

		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectComplementOf ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.NonHorn_DISJUNCTION);
		this.dlConstructorsInValidatedOntology.add(DLConstructor.NonHorn_DISJUNCTION);
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLObjectSomeValuesFrom ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.EXISTENTIAL_RESTRICTION);
		OWLClassExpression filler = ce.getFiller();
		OWLClassExpression profiledFiller = filler.accept(this);
		if (profiledFiller != null) {
			this.dlConstructorsInValidatedOntology.add(DLConstructor.EXISTENTIAL_RESTRICTION);
			return ce;
		} else {
			return null;
		}
	}

	@Override
	public OWLClassExpression visit(OWLObjectAllValuesFrom ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.NonHorn_UNIVERSAL_RESTRICTION);
		this.dlConstructorsInValidatedOntology.add(DLConstructor.NonHorn_UNIVERSAL_RESTRICTION);
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLObjectHasValue ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.HASVALUE);
		this.dlConstructorsInInputOntology.add(DLConstructor.NOMINAL);
		
		this.dlConstructorsInValidatedOntology.add(DLConstructor.HASVALUE);
		this.dlConstructorsInValidatedOntology.add(DLConstructor.NOMINAL);
		/*
		 * change anonymous individual to named individual
		 */
		OWLIndividual individual = ce.getValue();
		if (individual instanceof OWLNamedIndividual) {
			return ce;
		} else {
			OWLNamedIndividual namedIndividual = profilingFactory.getNamedIndividual(individual);
			return owlDataFactory.getOWLObjectHasValue(ce.getProperty(), namedIndividual);
		}

	}

	@Override
	public OWLClassExpression visit(OWLObjectMinCardinality ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.MIN_CARDINALITY);
		int card = ce.getCardinality();
		OWLClassExpression filler = ce.getFiller();
		if (card == 1) {
			OWLClassExpression profiledFiller = filler.accept(this);
			if (profiledFiller != null) {
				/*
				 * rewritten as ObjectSomeValueFrom
				 */
				this.dlConstructorsInValidatedOntology.add(DLConstructor.MIN_CARDINALITY);
				return owlDataFactory.getOWLObjectSomeValuesFrom(ce.getProperty(), profiledFiller);
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	public OWLClassExpression visit(OWLObjectExactCardinality ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectMaxCardinality ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.MAX_CARDINALITY_LEFT);
		this.cardinalityCount++;
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectHasSelf ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectOneOf ce) {
		this.dlConstructorsInInputOntology.add(DLConstructor.NOMINAL);
//		this.dlConstructorsInValidatedOntology.add(DLConstructor.NOMINAL);
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLDataSomeValuesFrom ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataAllValuesFrom ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataHasValue ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataMinCardinality ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataExactCardinality ce) {
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataMaxCardinality ce) {
		return null;
	}

}
