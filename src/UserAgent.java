public class UserAgent {
    final String operatingSystem;
    final String browser;
    final boolean isReal;

    public UserAgent(String string) {
        this.isReal = !string.toUpperCase().contains("BOT");

        if (string.equals("-") || string.equals("")) {
            browser = "";
            operatingSystem = "";
            return;
        }

        int startIndex, endIndex;
        String browser, operatingSystem;
        try {
            endIndex = string.indexOf(' ');
            browser = string.substring(0, endIndex);

            startIndex = string.indexOf('(') + 1;
            endIndex = string.indexOf(')');
            operatingSystem = string.substring(startIndex, endIndex);

        } catch (Exception e) {
            browser = "";
            operatingSystem = "";
        }

        this.browser = browser;
        this.operatingSystem = operatingSystem;
    }
}
