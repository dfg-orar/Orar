package orar.normalization.ALCHOI;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
//import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import orar.data.MetaDataOfOntology;
import orar.data.NormalizationDataFactory;
import orar.normalization.SubClassNormalizer;
import orar.normalization.SuperClassNormalizer;

/**
 * Normalize SubClass of SubClassOfAxiom. Nominals will be collected during
 * normalization.
 * 
 * @author kien
 *
 */
public class ALCHOI_SubClassNormalizer implements SubClassNormalizer {
	protected final OWLDataFactory owlDataFactory;
	protected final Stack<OWLSubClassOfAxiom> stack;
	protected final NormalizationDataFactory normalizerDataFactory;

	private SuperClassNormalizer superClassNormalizer;
	private final MetaDataOfOntology sharedData;
	private static Logger logger = Logger.getLogger(ALCHOI_SubClassNormalizer.class);

	// protected final Set<OWLSubClassOfAxiom> normalizedAxioms;

	// private OWLClassExpression superClass;

	public ALCHOI_SubClassNormalizer(Stack<OWLSubClassOfAxiom> stack) {
		this.stack = stack;
		// normalizedAxioms = new HashSet<>();
		owlDataFactory = OWLManager.getOWLDataFactory();
		normalizerDataFactory = NormalizationDataFactory.getInstance();
		sharedData = MetaDataOfOntology.getInstance();
	}

	@Override
	public OWLClassExpression visit(OWLClass ce) {
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLObjectIntersectionOf ce) {
		Set<OWLClassExpression> normalizedOperands = new HashSet<OWLClassExpression>();

		Set<OWLClassExpression> operands = ce.getOperands();
		for (OWLClassExpression operand : operands) {
			OWLClassExpression normalizedOperand = operand.accept(this);
			normalizedOperands.add(normalizedOperand);

		}
		return owlDataFactory.getOWLObjectIntersectionOf(normalizedOperands);
	}

	@Override
	public OWLClassExpression visit(OWLObjectUnionOf ce) {
		Set<OWLClassExpression> normalizedOperands = new HashSet<OWLClassExpression>();

		Set<OWLClassExpression> operands = ce.getOperands();
		for (OWLClassExpression operand : operands) {
			OWLClassExpression normalizedOperand = operand.accept(this);
			normalizedOperands.add(normalizedOperand);

		}
		return owlDataFactory.getOWLObjectUnionOf(normalizedOperands);
	}

	@Override
	public OWLClassExpression visit(OWLObjectComplementOf ce) {
		OWLClassExpression classEx = ce.getComplementNNF();

		OWLClassExpression normalizedClassEx = classEx.accept(superClassNormalizer);

		return owlDataFactory.getOWLObjectComplementOf(normalizedClassEx);

	}

	@Override
	public OWLClassExpression visit(OWLObjectSomeValuesFrom ce) {

		OWLClass freshClass = normalizerDataFactory.getFreshConcept();

		// logger.info("***DEBUG***"+ce);
		OWLClassExpression filler = ce.getFiller();
		if (filler instanceof OWLObjectOneOf) {
			Set<OWLIndividual> inds = ((OWLObjectOneOf) filler).getIndividuals();

			for (OWLIndividual ind : inds) {
				sharedData.getNominals().add(ind.asOWLNamedIndividual());
			}

		}

		OWLSubClassOfAxiom newSubClass = owlDataFactory.getOWLSubClassOfAxiom(ce, freshClass);
		stack.push(newSubClass);
		return freshClass;
	}

	@Override
	public OWLClassExpression visit(OWLObjectAllValuesFrom ce) {
		OWLClassExpression filler = ce.getFiller();
		OWLObjectPropertyExpression p = ce.getProperty();

		OWLClassExpression normalizedFiller = filler.accept(superClassNormalizer);
		return owlDataFactory.getOWLObjectAllValuesFrom(p, normalizedFiller);
	}

	@Override
	public OWLClassExpression visit(OWLObjectHasValue ce) {
		sharedData.getNominals().add(ce.getValue().asOWLNamedIndividual());

		OWLClass freshClass = normalizerDataFactory.getFreshConcept();

		OWLSubClassOfAxiom newSubClass = owlDataFactory.getOWLSubClassOfAxiom(ce, freshClass);
		stack.push(newSubClass);
		return freshClass;

	}

	@Override
	public OWLClassExpression visit(OWLObjectMinCardinality ce) {
		int card = ce.getCardinality();
		if (card == 1) {
			/*
			 * rewrite to SomeValueFrom
			 */
			OWLObjectSomeValuesFrom someValueFromAxiom = owlDataFactory.getOWLObjectSomeValuesFrom(ce.getProperty(),
					ce.getFiller());
			return someValueFromAxiom;

		} else {
			logger.warn("OWLObjectMinCardinality" + ce + "is not allowed in our profile");

			return null;
		}
	}

	@Override
	public OWLClassExpression visit(OWLObjectExactCardinality ce) {
		logger.warn("OWLObjectExactCardinality" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectMaxCardinality ce) {
		logger.warn("OWLObjectMaxCardinality" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectHasSelf ce) {
		logger.warn("OWLObjectHasSelf" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectOneOf ce) {
		// logger.info("***DEBUG*** OWLObjectOneOf"+ce);
		// Set<OWLIndividual> inds = ce.getIndividuals();
		// for (OWLIndividual ind : inds) {
		// sharedData.getNominals().add(ind.asOWLNamedIndividual());
		// }
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLDataSomeValuesFrom ce) {
		logger.warn("OWLDataSomeValuesFrom" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataAllValuesFrom ce) {
		logger.warn("OWLDataAllValuesFrom" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataHasValue ce) {
		logger.warn("OWLDataHasValue" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataMinCardinality ce) {
		logger.warn("OWLDataMinCardinality" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataExactCardinality ce) {
		logger.warn("OWLDataExactCardinality" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataMaxCardinality ce) {
		logger.warn("OWLDataMaxCardinality" + ce + "is not allowed in our profile");
		return null;
	}

	@Override
	public void setSuperClassNormalizer(SuperClassNormalizer superClassNormalizer) {
		this.superClassNormalizer = superClassNormalizer;
	}

}
