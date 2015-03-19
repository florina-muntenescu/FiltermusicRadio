package filtermusic.net.common.database;

import java.sql.SQLException;

/**
 * Describes an error occured during a CRUD operation
 */
public class DbError extends RuntimeException {
    private static final long serialVersionUID = 7073612581244389501L;

    /**
     * Constructs a new {@code DbError} with the current stack trace,
     * the specified detail message and the specified cause.
     *
     * @param detailMessage
     *            the detail message for this exception.
     * @param throwable
     *            the cause of this exception.
     */
    public DbError(String detailMessage, SQLException throwable) {
        super(detailMessage, throwable);
    }
}
