package orar.config;

import java.util.HashSet;
import java.util.Set;

import orar.dlfragmentvalidator.*;

public class Configuration {
	private String KONCLUDE_BINARY_PATH;
	private DLFragment targetProfile;
	private static Configuration instance;
	private final Set<LogInfo> logInfos;
	private final Set<DebugLevel> debuglevels;
	private final String koncludeConfigFileName;
	private final String savedOntologyFileName;

	/*
	 * number of types per abstraction ontology. Negative numbers are used for
	 * non-splitting, e.g. put all types in one abstraction---there will be only
	 * one resulting abstraction.
	 */
	private int numberOfTypePerOntology = -1;// Default value

	private Configuration() {
		/*
		 * default name for Konclude is Konclude. This is the stable release.
		 * Other Konclude with date-suffix is up-to-dated implementation and might be buggy.
		 */
		this.KONCLUDE_BINARY_PATH = "/Users/kien/konclude/Konclude";
		

		/*
		 * MacOS
		 */
//		koncludeConfigFileName = "/Users/kien/konclude/konclude-load-config.xml";
//		savedOntologyFileName = "/Users/kien/konclude/ontologyForKonclude.funcionalsyntax.owl";
		
		
		/*
		 * samwise server. Use the following paths when running in samwise.
		 */
		koncludeConfigFileName = "/var/tmp/kien/iswc16benchmark/software/konclude-load-config.xml";
		savedOntologyFileName = "/var/tmp/kien/iswc16benchmark/software/ontologyForKonclude.funcionalsyntax.owl";

		this.logInfos = new HashSet<LogInfo>();
		this.debuglevels = new HashSet<DebugLevel>();
	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public String getKONCLUDE_BINARY_PATH() {
		return KONCLUDE_BINARY_PATH;
	}

	public void setKONCLUDE_BINARY_PATH(String kONCLUDE_BINARY_PATH) {
		KONCLUDE_BINARY_PATH = kONCLUDE_BINARY_PATH;
	}

	public DLFragment getTargetProfile() {
		return targetProfile;
	}

	public void setTargetProfile(DLFragment targetProfile) {
		this.targetProfile = targetProfile;
	}

	public Set<LogInfo> getLogInfos() {
		return logInfos;
	}

	public Set<DebugLevel> getDebuglevels() {
		return debuglevels;
	}

	public void addDebugLevels(DebugLevel... someDebuglevels) {
		for (DebugLevel lv : someDebuglevels) {
			this.debuglevels.add(lv);
		}

	}

	public void addLoginfoLevels(LogInfo... infos) {
		for (LogInfo info : infos) {
			this.logInfos.add(info);
		}
	}

	public void clearDebugLevels() {
		this.debuglevels.clear();
	}

	public void clearLogInfoLevels() {
		this.logInfos.clear();
	}

	public int getNumberOfTypePerOntology() {
		return numberOfTypePerOntology;
	}

	public void setNumberOfTypePerOntology(int numberOfTypePerOntology) {
		this.numberOfTypePerOntology = numberOfTypePerOntology;
	}

	public void addAllDebugInfos() {
		addDebugLevels(DebugLevel.ABSTRACTION_CREATION, DebugLevel.TYPE_COMPUTING,
				DebugLevel.UPDATING_CONCEPT_ASSERTION, DebugLevel.REASONING, DebugLevel.MERGING_INDIVIDUALS_DIRECTLY,
				DebugLevel.MERGING_INDIVIDUALS_BYABSTRACTION);
	}

	public String getKoncludeConfigFileName() {
		return koncludeConfigFileName;
	}

	public String getSavedOntologyFileName() {
		return savedOntologyFileName;
	}

}
