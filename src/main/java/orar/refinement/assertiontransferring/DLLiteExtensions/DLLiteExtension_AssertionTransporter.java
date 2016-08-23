package orar.refinement.assertiontransferring.DLLiteExtensions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import orar.config.DebugLevel;
import orar.data.AbstractDataFactory;
import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.assertiontransferring.HornSHOIF.HornSHOIF_AssertionTransporter;
import orar.util.PrintingHelper;

public class DLLiteExtension_AssertionTransporter extends HornSHOIF_AssertionTransporter {
	private final Logger logger = Logger.getLogger(DLLiteExtension_AssertionTransporter.class);

	public DLLiteExtension_AssertionTransporter(OrarOntology2 orarOntoloy,
			Map<OWLNamedIndividual, Set<OWLClass>> abstractConceptAssertionsAsMap,
			AbstractRoleAssertionBox abstractRoleAssertionBox,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> abstractSameasMap) {
		super(orarOntoloy, abstractConceptAssertionsAsMap, abstractRoleAssertionBox, abstractSameasMap);

	}

	/**
	 * add concept assertions based on concept assertions of representatives (X)
	 * for combined-types. For DLLite's extensions, we only need to care about
	 * concept assertions of X
	 */
	protected void transferConceptAssertions() {
		Set<OWLNamedIndividual> xAbstractIndividuals = AbstractDataFactory.getInstance().getXAbstractIndividuals();
		Iterator<Entry<OWLNamedIndividual, Set<OWLClass>>> iterator = this.abstractConceptAssertionsAsMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLClass>> entry = iterator.next();
			OWLNamedIndividual abstractInd = entry.getKey();
			if (xAbstractIndividuals.contains(abstractInd)) {
				Set<OWLClass> concepts = entry.getValue();
				if (concepts != null) {
					Set<Integer> originalIndividuals = this.dataForTransferingEntailments
							.getOriginalIndividuals(abstractInd);
					for (Integer originalInd : originalIndividuals) {
						// /*
						// * Debug
						// */
						// if (originalInd.equals(bug_individual)){
						// logger.info("***something wrong might come from here
						// ***");
						// logger.info("individual:"+bug_individual);
						// logger.info("Concepts:"+concepts);
						// logger.info("Reason:");
						// logger.info("By assertion from bstract
						// individual:"+abstractInd);
						// //if (sharedData.get)
						//
						// }
						if (config.getDebuglevels().contains(DebugLevel.UPDATING_CONCEPT_ASSERTION)) {
							logger.info("***DEBUG Update concept assertions in the original ABox ***");
							logger.info("Individual:" + originalInd);
							logger.info("has new concepts:" + concepts);
							logger.info("Reason: get from concept assertion of the abstract individual:" + abstractInd);
							logger.info("*=====================================================*");
						}
						/*
						 * end of debug
						 */
						Set<OWLClass> existingAssertedConcept = new HashSet<OWLClass>();
						if (this.config.getDebuglevels().contains(DebugLevel.TRANSFER_CONCEPTASSERTION)) {
							existingAssertedConcept.addAll(this.orarOntology.getAssertedConcepts(originalInd));
						}
						if (this.orarOntology.addManyConceptAssertions(originalInd, concepts)) {
							this.isABoxExtended = true;
							if (this.config.getDebuglevels().contains(DebugLevel.TRANSFER_CONCEPTASSERTION)) {
								logger.info("***DEBUG***TRANSFER_CONCEPTASSERTION:");
								logger.info("For individual:" + originalInd);
								logger.info("Existing asserted concepts:");
								PrintingHelper.printSet(existingAssertedConcept);
								logger.info("Newly added asserted concepts:" + concepts);
								logger.info("updated=true");
							}
						}
					}
				}
			}
		}

	}

}
