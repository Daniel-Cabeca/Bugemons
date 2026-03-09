package ulb.repository.inmemory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import ulb.model.HasId;

public class IdSetTest {
	class Entry implements HasId {
		private String id;

		public Entry(String id) {
			this.id = id;
		}

		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}

			if (!(o instanceof Entry)) {
				return false;
			}

			Entry b = (Entry) o;

			if (this.getId().equals(b.getId())) {
				return true;
			}

			return false;
		}
	}

	@Test
	public void testGetNotPresent() {
		IdSet<Entry> set = new IdSet<>();
		assertThrows(NoSuchElementException.class, () -> { set.get("doesnotexist"); });
	}

	@Test
	public void testAdd() {
		IdSet<Entry> set = new IdSet<>();
		Entry a = new Entry("a");

		set.add(a);
		assertEquals(a, set.get("a"));
	}

	@Test
	public void testAddAlreadyPresent() {
		IdSet<Entry> set = new IdSet<>();
		Entry a1 = new Entry("a");
		Entry a2 = new Entry("a");

		set.add(a1);
		assertThrows(IllegalArgumentException.class, () -> { set.add(a2); });
	}

	@Test
	public void testIterateEmpty() {
		IdSet<Entry> set = new IdSet<>();

		for (Entry entry: set) {
			assertTrue(false);
		}
	}

	@Test
	public void testIteratorWithEntries() {
		IdSet<Entry> set = new IdSet<>();
		Entry a = new Entry("a");
		Entry b = new Entry("b");

		set.add(a);
		set.add(b);

		boolean foundA = false;
		boolean foundB = false;

		for (Entry entry: set) {
			if (a.equals(entry)) {
				assertFalse(foundA);
				foundA = true;
			}
			else if (b.equals(entry)) {
				assertFalse(foundB);
				foundB = true;
			}
			else {
				assertTrue(false);
			}
		}

		assertTrue(foundA);
		assertTrue(foundB);
	}
}
