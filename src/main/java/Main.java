public class Main {

    static FormSolutionValidator form;

    private static void checkOperatingSystem() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (!isWindows) {
            System.err.println("This software can be run only on Windows.");
            System.exit(777);
        }
    }

    public static void main(String[] args) {
        checkOperatingSystem();
        form = new FormSolutionValidator();
    }
}
