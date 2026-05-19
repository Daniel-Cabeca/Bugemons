package ulb.repository.json;

import org.junit.jupiter.api.Test;
import ulb.exceptions.EntityNotFoundException;
import ulb.model.HasId;

import static org.junit.jupiter.api.Assertions.*;

public class IdSetTest {
	@Test
	public void testGetNotPresent() throws Exception {
		IdSet<Entry> set = new IdSet<>();
		assertThrows(EntityNotFoundException.class, () -> {
			set.get("doesnotexist");
		});
	}

	@Test
	public void testAdd() throws Exception {
		IdSet<Entry> set = new IdSet<>();
		Entry a = new Entry("a");

		set.add(a);
		assertEquals(a, set.get("a"));
	}

	@Test
	public void testAddAlreadyPresent() throws Exception {
		IdSet<Entry> set = new IdSet<>();
		Entry a1 = new Entry("a");
		Entry a2 = new Entry("a");

		set.add(a1);
		assertThrows(IllegalArgumentException.class, () -> {
			set.add(a2);
		});
	}

	@Test
	public void testIterateEmpty() throws Exception {
		IdSet<Entry> set = new IdSet<>();

		for (Entry entry : set) {
			assertTrue(false);
		}
	}

	@Test
	public void testIteratorWithEntries() throws Exception {
		IdSet<Entry> set = new IdSet<>();
		Entry a = new Entry("a");
		Entry b = new Entry("b");

		set.add(a);
		set.add(b);

		boolean foundA = false;
		boolean foundB = false;

		for (Entry entry : set) {
			if (a.equals(entry)) {
				assertFalse(foundA);
				foundA = true;
			} else if (b.equals(entry)) {
				assertFalse(foundB);
				foundB = true;
			} else {
				assertTrue(false);
			}
		}

		assertTrue(foundA);
		assertTrue(foundB);
	}

	class Entry implements HasId {
		private String id;

		public Entry(String id) {
			this.id = id;
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

		@Override
		public String getId() {
			return this.id;
		}
	}
}
