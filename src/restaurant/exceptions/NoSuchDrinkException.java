package restaurant.exceptions;

/**
 *
 * @author igor
 */
public class NoSuchDrinkException extends Exception {

    public NoSuchDrinkException() {
    }

    /**
     * Constructs an instance of <code>NoSuchDrinkException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchDrinkException(String msg) {
        super(msg);
    }
}
