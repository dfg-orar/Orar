package orar.config;

/**
 * Vocabulary used in log to printout statistic information. We need to unify
 * them and put some "mark",e.g Statistic-prefix, to make it easier to parse the
 * log files latter.
 * 
 * @author kien
 * 
 */
public class StatisticVocabulary {
	/*
	 * input
	 */
	public static String NUMBER_OF_TBOX_AXIOMS = "Statistic: Number_of_TBox_Axioms = ";
//	public static String FILE_NAME = "Statistic: Number_of_TBox_Axioms = ";

	public static String NUMBER_OF_INPUT_CONCEPTASSERTIONS = "Statistic: Number_of_input_concept_assertions = ";
	public static String NUMBER_OF_INPUT_ROLEASSERTIONS = "Statistic: Number_of_input_role_assertions = ";
	public static String NUMBER_OF_INPUT_ASSERTIONS = "Statistic: Number_of_input_concept_and_role_assertions = ";

	public static String NUMBER_OF_INPUT_INDIVIDUALS = "Statistic: Number_of_input_individuals = ";
	public static String NUMBER_OF_INPUT_CONCEPTNAMES = "Statistic: Number_of_input_concept_names = ";
	public static String NUMBER_OF_INPUT_ROLENAMES = "Statistic: Number_of_input_role_names = ";
	/*
	 * output
	 */
	public static String NUMBER_OF_MATERIALIZED_CONCEPTASSERTIONS = "Statistic: Number_of_materialized_concept_assertions = ";
	public static String NUMBER_OF_MATERIALIZED_ROLEASSERTIONS = "Statistic: Number_of_materialized_role_assertions = ";
	public static String NUMBER_OF_MATERIALIZED_ASSERTIONS = "Statistic: Number_of_materialized_concept_and_role_assertions = ";
	public static String NUMBER_OF_MATERIALIZED_EQUALITY_ASSERTIONS = "Statistic: Number_of_materialized_equality_assertions = ";
	public static String ONTOLOGY_CONSISTENCY = "Statistic: ontology consistency = ";
	public static String CONSISTENT = "CONSISTENT";
	public static String INCONSISTENT = "INCONSISTENT";
	/*
	 * types
	 */
	public static String NUMBER_OF_TYPES = "Statistic: Number_of_types = ";
	public static String NUMBER_OF_X = "Statistic: Number_of_x = ";
	public static String NUMBER_OF_U = "Statistic: Number_of_u = ";
	public static String NUMBER_OF_YZ = "Statistic: Number_of_yz = ";
	public static String NUMBER_OF_ABSTRACT_INDIVIDUALS = "Statistic: Number_of_abstract_individuals_xuyz = ";

	/*
	 * size of abstraction
	 */
	public static String NUMBER_OF_ABSTRACT_CONCEPTASSERTIONS = "Statistic: Number_of_abstract_concept_assertions = ";
	public static String NUMBER_OF_ABSTRACT_ROLEASSERTIONS = "Statistic: Number_of_abstract_role_assertions = ";
	public static String NUMBER_OF_ABSTRACT_ASSERTIONS = "Statistic: Number_of_abstract_concept_and_role_assertions = ";

	/*
	 * performance
	 */
	public static String NUMBER_OF_REFINEMENTS = "Statistic: Number_of_refinement_steps = ";
	public static String CURRENT_LOOP="Statistic: Current_loop = ";
	public static String TIME_LOADING_INPUT = "Statistic: Time_loading_input_ontology_in_seconds = ";
	public static String TIME_REASONING_USING_ABSRTACTION = "Statistic: Time_reasoning_using_abstraction_in_seconds = ";
	public static String TIME_REASONING_USING_DLREASONER = "Statistic: Time_reasoning_using_dlreasoner_in_seconds = ";
	public static String TIME_REASONING_RULE_ENGINE = "Statistic: Time_reasoning_using_rule_engine_in_seconds = ";
	public static String TIME_REASONING_ON_ABSTRACTION_ONTOLOGY = "Statistic: Time_reasoning_on_abstraction_ontology = ";

}
