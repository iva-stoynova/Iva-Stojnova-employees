package app;

public final class ApplicationConstants {

    private ApplicationConstants() {
    }

    public static final String FILE_PATH = ".\\src\\resources\\employees.txt";

    public static final String BEST_TEAM_PATTERN = "The pair of employees who have worked together on common projects for the longest period of time are:%n emplID: %d, emplID: %d, %d days";
    public static final String NO_TEAMS_MSG = "Doesn't exist pair of employees which are worked together on common projects.";

    public static final int EMPTY_COLLECTION_SIZE = 0;
    public static final int INDEX_ZERO = 0;
    public static final int ONE = 1;
    public static final int DEFAULT_OVERLAP_ZERO_DAYS = 0;
}
