package Exceptions;

public class NotEnoughCreditsException extends StudentException {
    public NotEnoughCreditsException(String message) {
        super(message);
    }

    public NotEnoughCreditsException() {
        super("You don't have enough money");
    }
}
