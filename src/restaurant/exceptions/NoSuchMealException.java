package restaurant.exceptions;

/**
 *
 * @author igor
 */
public class NoSuchMealException extends Exception {

    public NoSuchMealException() {
    }

    /**
     * Constructs an instance of <code>NoSuchMealException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchMealException(String msg) {
        super(msg);
    }
}
