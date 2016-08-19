package orar.normalization.ALCHOI;

import orar.normalization.NormalizerTemplate;

import org.semanticweb.owlapi.model.OWLOntology;

public class ALCHOI_Normalizer extends NormalizerTemplate {

	public ALCHOI_Normalizer(OWLOntology inputOntology) {
		super(inputOntology);

	}

	@Override
	public void initSubClassAndSuperClassNormalizers() {
		this.subClassNormalizer = new ALCHOI_SubClassNormalizer(
				subClassAxiomStack);
		this.superClassNormalizer = new ALCHOI_SuperClassNormalizer(
				subClassAxiomStack);

		this.superClassNormalizer.setSubClassNormalizer(subClassNormalizer);
		this.subClassNormalizer.setSuperClassNormalizer(superClassNormalizer);

	}

}
