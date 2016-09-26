package orar.config;

public enum LogInfo {
	NORMALIZATION_INFO, LANGUAGE_VALIDATION_INFO, ABSTRACT_ONT_INFO, ABSTRACTION_INFO, //
	INPUTONTOLOGY_INFO, TYPE_COMPUTING_INFO,
	/*
	 * loading time
	 */
	LOADING_TIME,
	/*
	 * reasoning time
	 */
	REASONING_TIME, TOTAL_TIME, //
	TYPE_INFO, COMPARED_RESULT_INFO,
	/*
	 * STATISTIC: size of abstraction, number of loops, number of types, number
	 * of individuals, size of ontology.
	 */
	STATISTIC,

	/*
	 * detailed information about the size of ontology, abstraction, number of
	 * types, individuals,....
	 */
	DETAILED_STATISTIC, ABSTRACT_EXPLANATION,
	/*
	 * Print detail time in each steps. It corresponds to <performance> option in
	 * the command line
	 */
	TIME_STAMP_FOR_EACH_STEP, TIME_FOR_EACH_STEP, PRINT_RESULT,
	TUNING_SAMEAS;
}
