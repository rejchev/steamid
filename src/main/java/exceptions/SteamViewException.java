package exceptions;

public class SteamViewException extends Exception {

    public SteamViewException() {}

    public SteamViewException(String message) {
        super(message);
    }

    public SteamViewException(String message, Throwable cause) {
        super(message, cause);
    }
}
