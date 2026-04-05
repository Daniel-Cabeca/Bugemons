package ulb.repository.database.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.Iterator;

class SqlScriptTest {
	private SqlScript getScript(String name) {
		URL url = SqlScriptTest.class.getResource("/sql/"+ name +".sql");
		return new SqlScript(url);
	}

	@Test
	public void readsSqlFile() {
		SqlScript script = getScript("select1");
		assertEquals("SELECT 1;\n", script.getSql());
	}

	@Test
	public void testGetStatementsWithOneQuery() {
		SqlScript script = getScript("select1");

		Iterable<String> queries = script.getStatements();
		Iterator<String> iterator = queries.iterator();

		String query1 = iterator.next();
		assertEquals("SELECT 1", query1);
	}

	@Test
	public void testGetStatementsWithTwoStatements() {
		SqlScript script = getScript("select1_select2");

		Iterable<String> queries = script.getStatements();
		Iterator<String> iterator = queries.iterator();

		String query1 = iterator.next();
		assertEquals("SELECT 1", query1);

		String query2 = iterator.next();
		assertEquals("SELECT 2", query2);
	}

	@Test
	public void testGetStatementsDoesNotIncludePadding() {
		SqlScript script = getScript("select1_select2_padded");

		Iterable<String> queries = script.getStatements();
		Iterator<String> iterator = queries.iterator();

		iterator.next();
		iterator.next();
		assertFalse(iterator.hasNext());
	}
}
