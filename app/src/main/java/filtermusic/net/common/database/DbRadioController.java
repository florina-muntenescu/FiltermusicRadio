package filtermusic.net.common.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Provides CRUD operations to the database
 */
public class DbRadioController {

    private DatabaseHelper mDatabaseHelper;

    /**
     * Constructs the controller.
     *
     * @param databaseHelper db helper that opens the DB
     */
    public DbRadioController(DatabaseHelper databaseHelper) {
        this.mDatabaseHelper = databaseHelper;
    }

    public List<DbRadio> getRadioList() {
        Dao<DbRadio, Integer> dao = mDatabaseHelper.getRadioDao();
        try {
            return dao.queryForAll();
        } catch (SQLException ex) {
            throw new DbError("Error providing radio list", ex);
        }
    }

    public List<DbRadio> getFavorites() {
        Dao<DbRadio, Integer> dao = mDatabaseHelper.getRadioDao();
        try {
            return dao.queryForEq(
                    DbRadio.IS_FAVORITE_FILED_NAME, true);
        } catch (SQLException ex) {
            throw new DbError("Error providing favorite radios list", ex);
        }
    }

    public DbRadio getRadioById(final int radioId) {
        Dao<DbRadio, Integer> dao = mDatabaseHelper.getRadioDao();
        try {
            return dao.queryForId(radioId);
        } catch (SQLException ex) {
            throw new DbError("Error providing radio by id", ex);
        }
    }

    public DbRadio addOrUpdateRadio(DbRadio dbRadio) {
        Dao<DbRadio, Integer> dao = mDatabaseHelper.getRadioDao();
        try {
            dao.createOrUpdate(dbRadio);
            return dbRadio;
        } catch (SQLException ex) {
            throw new DbError("Error adding or updating radio", ex);
        }
    }

    public void deleteRadios(List<DbRadio> dbRadios) {
        Dao<DbRadio, Integer> dao = mDatabaseHelper.getRadioDao();
        try {
            dao.delete(dbRadios);
        } catch (SQLException ex) {
            throw new DbError("Error deleting radio collection", ex);
        }
    }

    /**
     * Retrieve from the database all the fields ordered by their play date
     */
    public List<DbRadio> getLastPlayedList() {
        Dao<DbRadio, Integer> dao = mDatabaseHelper.getRadioDao();

        QueryBuilder<DbRadio, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy(DbRadio.PLAYED_DATE_FILED_NAME, false);
        List<DbRadio> dbRadios = Collections.emptyList();
        try {
            Where<DbRadio, Integer> where = queryBuilder.where();
            where.isNotNull(DbRadio.PLAYED_DATE_FILED_NAME);
            dbRadios = dao.query(queryBuilder.prepare());
        } catch (SQLException exception) {
            throw new DbError("Error providing last played radios", exception);
        }

        return dbRadios;
    }

    /**
     * Performs operations in the {@code callable} in the database transaction. This means that
     * if on of the operations fail, all other will be reverted.
     *
     * @param callable contains operations to be performed
     * @throws filtermusic.net.common.database.DbError
     */
    public <T> T runInTransaction(Callable<T> callable) {
        ConnectionSource source = mDatabaseHelper.getConnectionSource();

        try {
            return TransactionManager.callInTransaction(source, callable);
        } catch (SQLException e) {
            throw new DbError("Error while running a callable in transaction", e);
        }
    }

}