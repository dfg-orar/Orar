package orar.owlconverter;

import static org.junit.Assert.*;

import org.junit.Test;

public class HornSHOIF_Orar2TurtleConverter_Test {

	@Test
	public void test() {
		String tboxFile="src/test/resources/ontologyconverter/lubm/univ-bench.owl";
//		String aboxList="src/test/resources/ontologyconverter/lubm/aboxList";
		String aboxList="/Users/kien/benchmarks/generator-lubm-linux-v5/lubm25/aboxListLUBM25.txt";
		
		Orar2TurtleConverter converter= new HornSHOIF_Orar2TurtleConverter();
		converter.convert(tboxFile, aboxList, "src/test/resources/ontologyconverter/lubm/validatedLUBM.owl", "src/test/resources/ontologyconverter/lubm/ubmABox.ttl");
	}

}
