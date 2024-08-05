package exeptions;

public class ManagerSaveException extends RuntimeException {
    private static final String message = "Произошла ошибка во время работы с файлом";

    public ManagerSaveException(Exception e) {
        super(message, e);
    }
}
