package orar.normalization.ALCHOIF;

import orar.normalization.NormalizerTemplate;

import org.semanticweb.owlapi.model.OWLOntology;

public class ALCHOIF_Normalizer extends NormalizerTemplate {
	public ALCHOIF_Normalizer(OWLOntology inputOntology) {
		super(inputOntology);

	}

	@Override
	public void initSubClassAndSuperClassNormalizers() {
		this.subClassNormalizer = new ALCHOIF_SubClassNormalizer(
				subClassAxiomStack);
		this.superClassNormalizer = new ALCHOIF_SuperClassNormalizer(
				subClassAxiomStack);

		this.superClassNormalizer.setSubClassNormalizer(subClassNormalizer);
		this.subClassNormalizer.setSuperClassNormalizer(superClassNormalizer);

	}

}
