package orar.strategyIdentifying;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import junit.framework.Assert;
import orar.strategyindentifying.StrategyIdentifier;
import orar.strategyindentifying.StrategyIdentifierImpl;
import orar.strategyindentifying.StrategyName;
import orar.util.PrintingHelper;

public class StrategyIdentifierTest {

	@Test
	public void shouldReturnDLLiteExtensionStrategy() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/dlliteExtension.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.DLLITE_EXTENSION_STRATEGY, name);
	}
	
	@Test
	public void shouldReturnHornSHIFStrategy1() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/hornSHIF1.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.HORN_SHIF_STRATEGY, name);
	}
	@Test
	public void shouldReturnHornSHIFStrategy2() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/hornSHIF2.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.HORN_SHIF_STRATEGY, name);
	}

	@Test
	public void shouldReturnHornSHOIFStrategy1() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/hornSHOIF1.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.HORN_SHOIF_STRATEGY, name);
	}
	
	@Test
	public void shouldReturnHornSHOIFStrategy2() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/dlfragmentvalidator/dlLiteExtension/hornSHOIF2.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.HORN_SHOIF_STRATEGY, name);
	}

	@Test
	public void testImdb() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/strategy/hshoif_imdb.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.DLLITE_EXTENSION_STRATEGY, name);
	}
	@Test
	public void testDBPedia() throws OWLOntologyCreationException {
		String tboxFileName="src/test/resources/strategy/hshoif_dbpedia.owl";
		StrategyName name = getStrategyName(tboxFileName);
		System.out.println(name);
		Assert.assertEquals(StrategyName.HORN_SHOIF_STRATEGY, name);
	}
	
	private StrategyName getStrategyName(String tboxFileName) throws OWLOntologyCreationException{
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
		OWLOntology owlOntology=manager.loadOntologyFromOntologyDocument(new File(tboxFileName));
		Set<OWLAxiom> tboxAxioms= new HashSet<OWLAxiom>();
		tboxAxioms.addAll(owlOntology.getTBoxAxioms(true));
		tboxAxioms.addAll(owlOntology.getRBoxAxioms(true));
		StrategyIdentifier strategyIdentifier= new StrategyIdentifierImpl(tboxAxioms);
		PrintingHelper.printSet(strategyIdentifier.getDLConstructors());
		return strategyIdentifier.getStrategyName();
		
	}

}
