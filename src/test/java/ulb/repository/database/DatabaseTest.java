package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.io.IOException;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

class DatabaseTest {
	@Test
	public void verifyGetUrl() {
		Database db = new Database("name");
		assertEquals("jdbc:sqlite:name.db", db.getUrl());
	}

	@Test
	public void verifyGetFilePath() {
		Database db = new Database("name");
		assertEquals(Path.of("name.db"), db.getFilePath());
	}
}
