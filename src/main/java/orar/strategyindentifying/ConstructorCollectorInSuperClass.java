package orar.strategyindentifying;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
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

public class ConstructorCollectorInSuperClass implements OWLClassExpressionVisitorEx<DLConstructor> {
	private OWLClassExpression subClass;
	public ConstructorCollectorInSuperClass(OWLClassExpression subClass) {
	 this.subClass=subClass;
	}
	@Override
	public DLConstructor visit(OWLClass ce) {
		
		return null;
	}

	@Override
	public DLConstructor visit(OWLObjectIntersectionOf ce) {
		
		return DLConstructor.CONJUNCTION;
	}

	@Override
	public DLConstructor visit(OWLObjectUnionOf ce) {
	
		return null;//should not be in horn
	}

	@Override
	public DLConstructor visit(OWLObjectComplementOf ce) {
	
		return DLConstructor.NEGATION;
	}

	@Override
	public DLConstructor visit(OWLObjectSomeValuesFrom ce) {
		 OWLClassExpression filler = ce.getFiller();
		if (filler.isOWLThing()){
			return DLConstructor.UNQUALIFIED_EXISTENTIAL_RESTRICTION;
		} else
		return DLConstructor.QUALIFIED_EXISTENTIAL_RESTRICTION;

	}

	@Override
	public DLConstructor visit(OWLObjectAllValuesFrom ce) {
		if (this.subClass.isOWLThing()) {
			return DLConstructor.EQUIVALENT_TO_RANGE_CONSTRAIN;
		} else 
		return DLConstructor.UNIVERSAL_RESTRICTION;
	}

	@Override
	public DLConstructor visit(OWLObjectHasValue ce) {
	
		return DLConstructor.HASVALUE;
	}

	@Override
	public DLConstructor visit(OWLObjectMinCardinality ce) {
		int card = ce.getCardinality();
		if (card==1){
			 OWLClassExpression filler = ce.getFiller();
				if (filler.isOWLThing()){
					return DLConstructor.UNQUALIFIED_EXISTENTIAL_RESTRICTION;
				} else
				return DLConstructor.QUALIFIED_EXISTENTIAL_RESTRICTION;
		}
		return null;
	}

	@Override
	public DLConstructor visit(OWLObjectExactCardinality ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLObjectMaxCardinality ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLObjectHasSelf ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLObjectOneOf ce) {
	
		return DLConstructor.NOMINAL;
	}

	@Override
	public DLConstructor visit(OWLDataSomeValuesFrom ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLDataAllValuesFrom ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLDataHasValue ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLDataMinCardinality ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLDataExactCardinality ce) {
	
		return null;
	}

	@Override
	public DLConstructor visit(OWLDataMaxCardinality ce) {
	
		return null;
	}

}
