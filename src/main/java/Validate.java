public class Validate {
    private final StringBuilder errorMessages = new StringBuilder();

    void addError(String message) {
        if (errorMessages.length() > 0) {
            errorMessages.append(", ");
        }
        errorMessages.append(message);
    }

    boolean isValid() {
        return errorMessages.length() == 0;
    }

    String getErrorMessages() {
        return errorMessages.toString();
    }
}
