public class CommonHelpers {

    public static boolean isValidISODateTime(String date) {
        try {
            java.time.format.DateTimeFormatter.ISO_DATE_TIME.parse(date);
            return true;
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }
}
