package exeptions;

public class TaskOverlapException extends RuntimeException {
    private final int statusCode;

    public TaskOverlapException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}