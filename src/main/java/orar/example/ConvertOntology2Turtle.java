package orar.example;

import orar.owlconverter.HornSHOIF_Orar2TurtleConverter;
import orar.owlconverter.Orar2TurtleConverter;

public class ConvertOntology2Turtle {
	public static void main(String[] args) {
		Orar2TurtleConverter converter = new HornSHOIF_Orar2TurtleConverter();
		String coburn="/Users/kien/benchmarks/coburn/coburnSHOIF.owl";
		String corburnTBox="/Users/kien/benchmarks/coburn/hshoif_coburn_tbox.owl";
		String corburnABox="/Users/kien/benchmarks/coburn/coburn_abox.ttl";
		converter.convertCombinedOntologyToSaparatedFiles(coburn, corburnTBox, corburnABox);
	}
}
