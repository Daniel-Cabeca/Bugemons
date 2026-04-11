package ulb.repository.database;

import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInitializer;

import java.sql.Connection;

public class DatabaseManager {
    private final Database database;

    public DatabaseManager() {
        this.database = DatabaseInitializer.prepareDefaultDatabase();
    }

    public Connection getConnection() {
        return database.getConnection();
    }
}
