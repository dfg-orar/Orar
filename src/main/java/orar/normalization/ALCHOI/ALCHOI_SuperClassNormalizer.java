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
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import orar.data.NormalizationDataFactory;
import orar.data.MetaDataOfOntology;
import orar.normalization.SubClassNormalizer;
import orar.normalization.SuperClassNormalizer;

/**
 * Normalize SuperClass of SubClassOfAxiom. Nominals also are collected.
 * 
 * @author kien
 *
 */
public class ALCHOI_SuperClassNormalizer implements SuperClassNormalizer {

	protected final OWLDataFactory owlDataFactory;
	protected final Stack<OWLSubClassOfAxiom> stack;
	protected final NormalizationDataFactory normalizerDataFactory;
	private static Logger logger = Logger.getLogger(ALCHOI_SuperClassNormalizer.class);
	private SubClassNormalizer subClassNormalizer;
	protected final MetaDataOfOntology metaDataOfOntology;

	public ALCHOI_SuperClassNormalizer(Stack<OWLSubClassOfAxiom> stack) {
		this.stack = stack;
		owlDataFactory = OWLManager.getOWLDataFactory();
		normalizerDataFactory = NormalizationDataFactory.getInstance();
		metaDataOfOntology = MetaDataOfOntology.getInstance();

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
			// if (normalizedOperand==null){
			// logger.info(operand);
			// }
			if (normalizedOperand != null) {
				normalizedOperands.add(normalizedOperand);
			}

		}
		return owlDataFactory.getOWLObjectIntersectionOf(normalizedOperands);
	}

	/**
	 * Add subFresshOWLClass SubClassOf expression to stack.
	 * 
	 * @param expression
	 * @return subFresshOWLClass.
	 */
	protected OWLClass replaceBySubFreshClass(OWLClassExpression expression) {
		OWLClass subFreschClass = normalizerDataFactory.getFreshConcept();
		OWLSubClassOfAxiom newSubClassAxiom = owlDataFactory.getOWLSubClassOfAxiom(subFreschClass, expression);
		this.stack.push(newSubClassAxiom);
		return subFreschClass;
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
		OWLClassExpression normalizedClassEx = classEx.accept(subClassNormalizer);

		return owlDataFactory.getOWLObjectComplementOf(normalizedClassEx);

	}

	@Override
	public OWLClassExpression visit(OWLObjectSomeValuesFrom ce) {
		OWLClassExpression filler = ce.getFiller();
		OWLClassExpression normalizedFiller = filler.accept(this);

		return owlDataFactory.getOWLObjectSomeValuesFrom(ce.getProperty(), normalizedFiller);
	}

	@Override
	public OWLClassExpression visit(OWLObjectAllValuesFrom ce) {
		OWLClassExpression filler = ce.getFiller();

		/*
		 * not normalize if filler is atomic concept or conjunction of atomic
		 * concepts
		 */
		if (filler instanceof OWLClass)
			return ce;
		if (filler instanceof OWLObjectIntersectionOf) {
			boolean allAtomic = true;
			OWLObjectIntersectionOf conjunction = (OWLObjectIntersectionOf) filler;
			for (OWLClassExpression operand : conjunction.getOperands()) {
				if (!(operand instanceof OWLClass)) {
					allAtomic = false;
					break;
				}
			}
			if (allAtomic)
				return ce;

		}

		OWLObjectPropertyExpression propertyExp = ce.getProperty();
		OWLClass subFreshClass = replaceBySubFreshClass(filler);
		return owlDataFactory.getOWLObjectAllValuesFrom(propertyExp, subFreshClass);

	}

	@Override
	public OWLClassExpression visit(OWLObjectHasValue ce) {
		metaDataOfOntology.getNominals().add(ce.getValue().asOWLNamedIndividual());
		return ce;

	}

	@Override
	public OWLClassExpression visit(OWLObjectMinCardinality ce) {
		int card = ce.getCardinality();
		if (card == 1) {
			OWLClassExpression filler = ce.getFiller();
			OWLClassExpression normalizedFiller = filler.accept(this);
			return owlDataFactory.getOWLObjectSomeValuesFrom(ce.getProperty(), normalizedFiller);
		} else {
			logger.warn("Irgnored OWLObjectMinCardinality" + ce);
			return null;
		}

	}

	@Override
	public OWLClassExpression visit(OWLObjectExactCardinality ce) {
		logger.warn("Irgnored OWLObjectExactCardinality" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectMaxCardinality ce) {
		logger.warn("Irgnored OWLObjectMaxCardinality" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectHasSelf ce) {
		logger.warn("Irgnored OWLObjectHasSelf" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLObjectOneOf ce) {
		Set<OWLIndividual> inds = ce.getIndividuals();
		for (OWLIndividual ind : inds) {
			metaDataOfOntology.getNominals().add(ind.asOWLNamedIndividual());
		}
		return ce;
	}

	@Override
	public OWLClassExpression visit(OWLDataSomeValuesFrom ce) {
		logger.warn("Irgnored OWLDataSomeValuesFrom" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataAllValuesFrom ce) {
		logger.warn("Irgnored OWLDataAllValuesFrom" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataHasValue ce) {
		logger.warn("Irgnored OWLDataHasValue" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataMinCardinality ce) {
		logger.warn("Irgnored OWLDataMinCardinality" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataExactCardinality ce) {
		logger.warn("ignored OWLDataExactCardinality" + ce);
		return null;
	}

	@Override
	public OWLClassExpression visit(OWLDataMaxCardinality ce) {
		logger.warn("Irgnored OWLDataMaxCardinality" + ce);
		return null;
	}

	@Override
	public void setSubClassNormalizer(SubClassNormalizer subClassNormalizer) {
		this.subClassNormalizer = subClassNormalizer;

	}

}
