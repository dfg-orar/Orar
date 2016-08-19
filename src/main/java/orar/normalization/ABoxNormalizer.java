package orar.normalization;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

import orar.data.NormalizationDataFactory;

/**
 * Normalize SameIndividualAxioms, DifferentIndividualAxioms Since they contain
 * nominals. This should be applied on ABox Axioms only.
 * 
 * @author kien
 *
 */
public class ABoxNormalizer implements OWLAxiomVisitorEx<OWLAxiom> {

	/*
	 * Axioms generated during the normalization of ABox
	 */
	private final Set<OWLAxiom> additionalAxioms;
	private final OWLDataFactory owlDataFactory;
	private final NormalizationDataFactory normFactory;

	private static Logger logger = Logger.getLogger(ABoxNormalizer.class);

	public ABoxNormalizer() {

		additionalAxioms = new HashSet<OWLAxiom>();
		owlDataFactory = OWLManager.getOWLDataFactory();
		normFactory = NormalizationDataFactory.getInstance();
	}

	@Override
	public OWLAxiom visit(OWLSubAnnotationPropertyOfAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLAnnotationPropertyDomainAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLAnnotationPropertyRangeAxiom axiom) {
		return null;
	}

	@Override
	public OWLAxiom visit(OWLSubClassOfAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLAsymmetricObjectPropertyAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLReflexiveObjectPropertyAxiom axiom) {
		logger.info("Removed OWLReflexiveObjectPropertyAxiom");
		return axiom;

	}

	@Override
	public OWLAxiom visit(OWLDisjointClassesAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDataPropertyDomainAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLObjectPropertyDomainAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDifferentIndividualsAxiom axiom) {
		Set<OWLIndividual> inds = axiom.getIndividuals();
		Set<OWLClass> newSetOfClassesForNom = new HashSet<OWLClass>();

		for (OWLIndividual ind : inds) {
			OWLNamedIndividual namedInd = ind.asOWLNamedIndividual();
			OWLClass newClassForNom = normFactory
					.getFreshOWLClassForNominal(namedInd);
			newSetOfClassesForNom.add(newClassForNom);

			OWLClassAssertionAxiom newAssertion = owlDataFactory
					.getOWLClassAssertionAxiom(newClassForNom, namedInd);
			this.additionalAxioms.add(newAssertion);

			OWLObjectOneOf nomInd = owlDataFactory.getOWLObjectOneOf(namedInd);
			// OWLSubClassOfAxiom newSubClassForNom = owlDataFactory
			// .getOWLSubClassOfAxiom(newClassForNom, nomInd);
			// this.additionalAxioms.add(newSubClassForNom);
			OWLEquivalentClassesAxiom newEquivalentClassAxioms = owlDataFactory
					.getOWLEquivalentClassesAxiom(newClassForNom, nomInd);
			this.additionalAxioms.add(newEquivalentClassAxioms);

		}

		OWLDisjointClassesAxiom disjointClassesAxiom = owlDataFactory
				.getOWLDisjointClassesAxiom(newSetOfClassesForNom);
		this.additionalAxioms.add(disjointClassesAxiom);
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDisjointDataPropertiesAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDisjointObjectPropertiesAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLObjectPropertyRangeAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLObjectPropertyAssertionAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLFunctionalObjectPropertyAxiom axiom) {
		logger.info("Removed OWLFunctionalObjectPropertyAxiom");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSubObjectPropertyOfAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDisjointUnionAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDeclarationAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLAnnotationAssertionAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSymmetricObjectPropertyAxiom axiom) {

		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDataPropertyRangeAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLFunctionalDataPropertyAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLEquivalentDataPropertiesAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLClassAssertionAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLEquivalentClassesAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDataPropertyAssertionAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLTransitiveObjectPropertyAxiom axiom) {
		logger.info("Removed OWLTransitiveObjectPropertyAxiom");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		logger.info("Removed OWLIrreflexiveObjectPropertyAxiom");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSubDataPropertyOfAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");

		return axiom;
	}

	// @Override
	// public OWLAxiom visit(OWLSameIndividualAxiom axiom) {
	// Set<OWLIndividual> inds = axiom.getIndividuals();
	// Set<OWLNamedIndividual> namedInds = new HashSet<>();
	// for (OWLIndividual ind : inds) {
	// namedInds.add(ind.asOWLNamedIndividual());
	// }
	//
	// OWLClass newClassForNom = normFactory
	// .getFreshOWLClassForNominal(namedInds);
	//
	// for (OWLIndividual ind : inds) {
	// OWLNamedIndividual namedInd = ind.asOWLNamedIndividual();
	// OWLClassAssertionAxiom newAssertion = owlDataFactory
	// .getOWLClassAssertionAxiom(newClassForNom, namedInd);
	// this.additionalAxioms.add(newAssertion);
	//
	// OWLObjectOneOf nomInd = owlDataFactory.getOWLObjectOneOf(namedInd);
	// // OWLSubClassOfAxiom newSubClassForNom = owlDataFactory
	// // .getOWLSubClassOfAxiom(newClassForNom, nomInd);
	// // this.additionalAxioms.add(newSubClassForNom);
	// OWLEquivalentClassesAxiom newEquivalentClassAxiom = owlDataFactory
	// .getOWLEquivalentClassesAxiom(newClassForNom, nomInd);
	// this.additionalAxioms.add(newEquivalentClassAxiom);
	// }
	//
	// return axiom;
	// }
	@Override
	public OWLAxiom visit(OWLSameIndividualAxiom axiom) {
		Set<OWLIndividual> inds = axiom.getIndividuals();

		Set<OWLClass> newSetOfClassesForNom = new HashSet<OWLClass>();
		
		Set<OWLClassExpression> newSetOfNominalClasses= new HashSet<OWLClassExpression>();

		for (OWLIndividual ind : inds) {
			OWLNamedIndividual namedInd = ind.asOWLNamedIndividual();
			OWLClass newClassForNom = normFactory
					.getFreshOWLClassForNominal(namedInd);

			newSetOfClassesForNom.add(newClassForNom);

			OWLClassAssertionAxiom newAssertion = owlDataFactory
					.getOWLClassAssertionAxiom(newClassForNom, namedInd);
			this.additionalAxioms.add(newAssertion);

			OWLObjectOneOf nomIndClass = owlDataFactory.getOWLObjectOneOf(namedInd);
			newSetOfNominalClasses.add(nomIndClass);
			// OWLSubClassOfAxiom newSubClassForNom = owlDataFactory
			// .getOWLSubClassOfAxiom(newClassForNom, nomInd);
			// this.additionalAxioms.add(newSubClassForNom);
			OWLEquivalentClassesAxiom newEquivalentClassAxiom = owlDataFactory
					.getOWLEquivalentClassesAxiom(newClassForNom, nomIndClass);
			this.additionalAxioms.add(newEquivalentClassAxiom);
		}
		this.additionalAxioms.add(owlDataFactory
				.getOWLEquivalentClassesAxiom(newSetOfClassesForNom));
		
		this.additionalAxioms.add(owlDataFactory
				.getOWLEquivalentClassesAxiom(newSetOfNominalClasses));
		
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLSubPropertyChainOfAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLInverseObjectPropertiesAxiom axiom) {
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLHasKeyAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(OWLDatatypeDefinitionAxiom axiom) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return axiom;
	}

	@Override
	public OWLAxiom visit(SWRLRule rule) {
		logger.info("Should not occur! ProfileValidator must do the job.");
		return null;
	}

	public Set<OWLAxiom> getAdditionalAxioms() {
		return additionalAxioms;
	}

}
