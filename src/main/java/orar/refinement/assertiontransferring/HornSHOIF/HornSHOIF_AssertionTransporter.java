package orar.refinement.assertiontransferring.HornSHOIF;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import orar.config.DebugLevel;
import orar.data.MetaDataOfOntology;
import orar.indexing.IndividualIndexer;
import orar.modeling.ontology.OrarOntology;
import orar.modeling.ontology2.OrarOntology2;
import orar.refinement.abstractroleassertion.AbstractRoleAssertionBox;
import orar.refinement.abstractroleassertion.RoleAssertionList;
import orar.refinement.assertiontransferring.AssertionTransporterTemplate;
import orar.util.PrintingHelper;

public class HornSHOIF_AssertionTransporter extends AssertionTransporterTemplate {
	private Logger logger = Logger.getLogger(HornSHOIF_AssertionTransporter.class);
	private final IndividualIndexer indexer;
	private MetaDataOfOntology metaDataOfOntology;
	public HornSHOIF_AssertionTransporter(OrarOntology2 orarOntoloy,
			Map<OWLNamedIndividual, Set<OWLClass>> abstractConceptAssertionsAsMap,
			AbstractRoleAssertionBox abstractRoleAssertionBox,
			Map<OWLNamedIndividual, Set<OWLNamedIndividual>> abstractSameasMap) {
		super(orarOntoloy);
		this.abstractConceptAssertionsAsMap = abstractConceptAssertionsAsMap;
		this.abstractRoleAssertionBox = abstractRoleAssertionBox;
		this.abstractSameasMap = abstractSameasMap;
		this.indexer= IndividualIndexer.getInstance();
		this.metaDataOfOntology=MetaDataOfOntology.getInstance();
	}

	@Override
	protected void transferSameasAssertions() {
		Iterator<Entry<OWLNamedIndividual, Set<OWLNamedIndividual>>> iterator = this.abstractSameasMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<OWLNamedIndividual, Set<OWLNamedIndividual>> entry = iterator.next();
			// compute a set of equivalent individuals in the original ABox
			Set<Integer> equivalentOriginalInds = new HashSet<Integer>();
			// for the key
			OWLNamedIndividual u = entry.getKey();
			Set<Integer> originalInds_of_u = this.dataForTransferingEntailments.getOriginalIndividuals(u);
			equivalentOriginalInds.addAll(originalInds_of_u);
			// for the equivalent individuals of the key.
			Set<OWLNamedIndividual> equivalentAbstractInds_of_u = entry.getValue();
			for (OWLNamedIndividual eachAbstractInd : equivalentAbstractInds_of_u) {
				equivalentOriginalInds
						.addAll(this.dataForTransferingEntailments.getOriginalIndividuals(eachAbstractInd));
			}

			if (equivalentOriginalInds.size() > 1) {
				if (this.orarOntology.addSameasAssertion(equivalentOriginalInds)) {
					this.isABoxExtended = true;
					
					this.isABoxExtendedWithNewSameasAssertions=true;
					if (this.config.getDebuglevels().contains(DebugLevel.TRANSFER_SAMEAS)) {
						logger.info("***DEBUG***TRANSFER_SAMEAS:");
						PrintingHelper.printSet(equivalentOriginalInds);
						logger.info("updated=true");
					}
					this.newSameasAssertions.add(equivalentOriginalInds);
				}
			}

		}

	}

	@Override
	protected void tranferRoleAssertionsBetweenUX() {
		transferRoleAssertionBetweenUAndXAbstract();
		transferRoleAssertionBetweenNominalAndU();
		transferRoleAssertionBetweenUAndNominals();
	}

	private void transferRoleAssertionBetweenUAndXAbstract() {
		RoleAssertionList roleAssertionList = this.abstractRoleAssertionBox.getUxRoleAssertionsForCTypeAndType();
		int size = roleAssertionList.getSize();
		for (int index = 0; index < size; index++) {
			OWLNamedIndividual abstractSubject = roleAssertionList.getSubject(index);
			OWLObjectProperty role = roleAssertionList.getRole(index);
			OWLNamedIndividual abstractObject = roleAssertionList.getObject(index);

			Set<Integer> originalSubjects = this.dataForTransferingEntailments
					.getOriginalIndividuals(abstractSubject);
			Set<Integer> originalObjects = this.dataForTransferingEntailments
					.getOriginalIndividuals(abstractObject);

			for (Integer originalSubject : originalSubjects) {
				for (Integer originalObject : originalObjects) {
					if (this.orarOntology.addRoleAssertion(originalSubject, role, originalObject)) {
						this.isABoxExtended = true;
						
						this.newRoleAssertions.addRoleAssertion(originalSubject, role, originalObject);

						if (this.config.getDebuglevels().contains(DebugLevel.TRANSFER_ROLEASSERTION)) {
							logger.info("***DEBUG***TRANSFER_ROLEASSERTION:");
							logger.info(originalSubject + ", " + role + ", " + originalObject);
							logger.info("updated=true");
						}
					}
				}

			}
		}
	}

	private void transferRoleAssertionBetweenUAndNominals() {
		RoleAssertionList roleAssertionList = this.abstractRoleAssertionBox.get_UandNominal_RoleAssertions();
		int size = roleAssertionList.getSize();
		for (int index = 0; index < size; index++) {
			OWLNamedIndividual abstractSubject = roleAssertionList.getSubject(index);
			OWLObjectProperty role = roleAssertionList.getRole(index);
			OWLNamedIndividual nominal = roleAssertionList.getObject(index);
			Integer nominalInteger = indexer.getIndexOfOWLIndividual(nominal);

			Set<Integer> originalSubjects = this.dataForTransferingEntailments
					.getOriginalIndividuals(abstractSubject);

			for (Integer originalSubject : originalSubjects) {

				if (this.orarOntology.addRoleAssertion(originalSubject, role, nominalInteger)) {
					this.isABoxExtended = true;
					this.newRoleAssertions.addRoleAssertion(originalSubject, role, nominalInteger);
					
					if (this.config.getDebuglevels().contains(DebugLevel.TRANSFER_ROLEASSERTION)) {
						logger.info("***DEBUG***TRANSFER_ROLEASSERTION:");
						logger.info(originalSubject + ", " + role + ", " + nominal);
						logger.info("updated=true");
					}
				}

			}
		}
	}

	private void transferRoleAssertionBetweenNominalAndU() {
		RoleAssertionList roleAssertionList = this.abstractRoleAssertionBox.get_NominalAndU_RoleAssertions();
		int size = roleAssertionList.getSize();
		for (int index = 0; index < size; index++) {
			OWLNamedIndividual nominal = roleAssertionList.getSubject(index);
			Integer nominalInteger = indexer.getIndexOfOWLIndividual(nominal);
			OWLObjectProperty role = roleAssertionList.getRole(index);
			OWLNamedIndividual abstractObject = roleAssertionList.getObject(index);

			Set<Integer> originalObjects = this.dataForTransferingEntailments
					.getOriginalIndividuals(abstractObject);

			for (Integer eachOriginalObject : originalObjects) {

				if (this.orarOntology.addRoleAssertion(nominalInteger, role, eachOriginalObject)) {
					this.isABoxExtended = true;
					this.newRoleAssertions.addRoleAssertion(nominalInteger, role, eachOriginalObject);
					
					if (this.config.getDebuglevels().contains(DebugLevel.TRANSFER_ROLEASSERTION)) {
						logger.info("***DEBUG***TRANSFER_ROLEASSERTION:");
						logger.info(nominal + ", " + role + ", " + eachOriginalObject);
						logger.info("updated=true");
					}
				}

			}
		}
	}
}
